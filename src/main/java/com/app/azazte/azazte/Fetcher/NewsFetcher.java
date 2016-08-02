package com.app.azazte.azazte.Fetcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;

import com.app.azazte.azazte.Adapters.NewsAdapter;
import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Beans.NewsCardWrapper;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.GCM.GCMUtils;
import com.app.azazte.azazte.MainActivity;
import com.app.azazte.azazte.Utils.Api.ApiExecutor;
import com.app.azazte.azazte.Utils.Categories;
import com.app.azazte.azazte.Utils.ConnectivityUtils;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.List;

import xdroid.toaster.Toaster;

/**
 * Created by sprinklr on 20/03/16.
 */
public class NewsFetcher extends AsyncTask {
    private static int BASE_NEWS_FETCH_SIZE = 69;
    FragmentActivity newsFragmentActivity;
    List<NewsCard> inMemoryNews;
    NewsAdapter cardArrayAdapter;
    SwipeRefreshLayout swipe;
    String lastFetchedId;
    GoogleCloudMessaging gcmObject;
    ConnectivityUtils connectivityUtilsInstance;
    SharedPreferences sharedPreferences;
    Connector connector;
    public static final String LAST_FETCHED_ID = "lastFetchedId";
    public static final String NETWORK_UNAVAILABLE = "NETWORK_UNAVAILABLE";
    public static boolean networkUnavailable = false;
    public Object result;

    public NewsFetcher(FragmentActivity activity, List<NewsCard> inMemoryNews, NewsAdapter cardArrayAdapter, SwipeRefreshLayout swipe, Connector connector) {

        this.inMemoryNews = inMemoryNews;
        this.cardArrayAdapter = cardArrayAdapter;
        this.swipe = swipe;
        this.newsFragmentActivity = activity;
        connectivityUtilsInstance = ConnectivityUtils.getConnectivityUtilsInstance();
        this.connector = connector;
        sharedPreferences = newsFragmentActivity.getSharedPreferences("ShaPreferences", Context.MODE_PRIVATE);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        _execute();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        _onPostExecute();
    }

    public List<NewsCard> fetchNewsFromDb() {
        return connector.getAllNews();
    }

    public void saveNewsInDb(List<NewsCard> newsCards) {
        for (NewsCard newsCard : newsCards) {
            connector.insertNews(newsCard);
        }
    }

    public Object _execute() {
        HttpClient client = new DefaultHttpClient();
        HttpGet request;
        sharedPreferences = newsFragmentActivity.getSharedPreferences("ShaPreferences", Context.MODE_PRIVATE);
        lastFetchedId = sharedPreferences.getString(LAST_FETCHED_ID, "");
        if (MainActivity.emailAddress == null || MainActivity.emailAddress.isEmpty()) {
            MainActivity.emailAddress = "notFound";
        }
        if (GCMUtils.registerationId == null) {
            GCMUtils.registerationId = "notFound";
        }
        request = new HttpGet("http://azazte.com/news/fetchNewsOnRefreshV2/" + lastFetchedId + "/" + MainActivity.emailAddress + "/" + GCMUtils.registerationId);
        HttpResponse response;
        try {
            boolean networkAvailable = connectivityUtilsInstance.checkAndDisplayToastOnInternetFailure(newsFragmentActivity);
            if (!networkAvailable) {
                networkUnavailable = true;
                return null;
            }
            client.getParams().setParameter("http.connection.timeout", 10000);
            response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            result = in.readLine();
        } catch (SocketTimeoutException e) {
            Toaster.toast("Slow internet connection.Could not fetch from server");
        } catch (Exception e) {
            System.out.print("hello");
            //e.printStackTrace();
        }
        return null;
    }

    public void _onPostExecute() {

        if (result != null) {
            try {
                NewsCardWrapper newsCardWrapper = new Gson().fromJson((String) result, NewsCardWrapper.class);
                if (newsCardWrapper.newsCardList.size() == 0) {
                    Toaster.toast("News upto date");
                }
                if (newsCardWrapper.newsCardList.size() > 0) {
                    List<NewsCard> newsCardList = newsCardWrapper.newsCardList;
                    saveNewsInDb(newsCardList);
                }
            } catch (Exception e) {
                Crashlytics.log("Failed gson mapping" + e);
            }

        }


        Categories.filterNewsByCategory(cardArrayAdapter, connector);
        List<NewsCard> newsCards = fetchNewsFromDb();
        if (newsCards.size() == 0) {
            swipe.setRefreshing(false);
            return;
        }
        lastFetchedId = newsCards.get(0).id;
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_FETCHED_ID, lastFetchedId);
        editor.apply();
        swipe.setRefreshing(false);
    }

    public void fetchNews() {
        Toaster.toast("Fetching news");
        sharedPreferences = newsFragmentActivity.getSharedPreferences("ShaPreferences", Context.MODE_PRIVATE);
        lastFetchedId = sharedPreferences.getString(LAST_FETCHED_ID, "");
        if (MainActivity.emailAddress == null || MainActivity.emailAddress.isEmpty()) {
            MainActivity.emailAddress = "notFound";
        }
        //ApiExecutor.getInstance().getNews(MainActivity.emailAddress, swipe);



//        result = null;
//        _onPostExecute();
//
//
//        final Runnable runner = new Runnable() {
//            @Override
//            public void run() {
//                _execute();
//            }
//        };
//
//        int trial = 0;
//        try {
//            while (result == null) {
//                ExecutorService executor = Executors.newSingleThreadExecutor();
//                try {
//                    executor.submit(runner).get(5, TimeUnit.SECONDS); // Timeout of 5 seconds.
//                } catch (Exception e) {
//                    Log.d("slow internet", "fetchNews: stopped due to slow internet");
//                }
//                executor.shutdown();
//                if (networkUnavailable || trial == 3) {
//                    break;
//                }
//                trial++;
//                Thread.sleep(10);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        _onPostExecute();
//        result = null;
//
//        result = null;
    }


}
