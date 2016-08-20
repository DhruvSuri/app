package com.app.azazte.azazte.Utils.Api;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.app.azazte.azazte.Beans.NotificationConfig;
import com.app.azazte.azazte.Beans.FCMServerResponse;
import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Beans.NewsCardWrapper;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Event.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xdroid.toaster.Toaster;

/**
 * Created by sprinklr on 14/05/16.
 */
public class ApiExecutor {
    public static final String BASE_URL = "http://aws.azazte.com";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static ApiExecutor apiExecutor;
    private static AzazteApi azazteApiService;

    private ApiExecutor() {
    }

    public static ApiExecutor getInstance() {
        if (apiExecutor == null) {
            apiExecutor = new ApiExecutor();
        }
        if (azazteApiService == null) {
            azazteApiService = retrofit.create(AzazteApi.class);
        }
        return apiExecutor;
    }

    public void getNews(String email, final SwipeRefreshLayout swipe) {
        final Call<NewsCardWrapper> newsCardWrapperCall = azazteApiService.getNews(0, 200);
        newsCardWrapperCall.enqueue(new Callback<NewsCardWrapper>() {


            @Override
            public void onResponse(Call<NewsCardWrapper> call, Response<NewsCardWrapper> response) {
                final NewsCardWrapper newsCardWrapper = response.body();
                if (newsCardWrapper == null) {
                    return;
                }
                if (newsCardWrapper.newsCardList.size() > 0) {
                    List<NewsCard> newsCardList = newsCardWrapper.newsCardList;
                    Connector.getInstance().saveNewsInDb(newsCardList);
                    Toaster.toast("Refreshed News");
                    EventBus.getDefault().post(new MessageEvent("Hello everyone!"));
                }
            }

            @Override
            public void onFailure(Call<NewsCardWrapper> call, Throwable t) {
                Toaster.toast("Failed to fetch news");
                EventBus.getDefault().post(new MessageEvent("Hello everyone!"));
//                if (swipe != null) {
//                    swipe.setRefreshing(false);
//                }
//                MainActivity.setRefreshActionButtonState(false);
            }
        });
    }

    public FCMServerResponse sendIdToServer(NotificationConfig config){
        Call<FCMServerResponse> call = azazteApiService.saveFCMId(config);
        call.enqueue(new Callback<FCMServerResponse>() {
            @Override
            public void onResponse(Call<FCMServerResponse> call, Response<FCMServerResponse> response) {

            }

            @Override
            public void onFailure(Call<FCMServerResponse> call, Throwable t) {

            }
        });
        return null;
    }
}
