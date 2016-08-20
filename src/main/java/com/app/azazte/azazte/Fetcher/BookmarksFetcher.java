package com.app.azazte.azazte.Fetcher;

import android.os.AsyncTask;

import com.app.azazte.azazte.Adapters.NewsAdapter;
import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.ListAdapter;
import com.app.azazte.azazte.Utils.Registry.Registry;
import com.app.azazte.azazte.Utils.azUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sprinklr on 26/03/16.
 */
public class BookmarksFetcher extends AsyncTask {
    private static NewsAdapter newsAdapter;
    private static Connector connector;
    public static Set<String> bookmarkSet = new HashSet<>();

    public BookmarksFetcher(NewsAdapter newsAdapter, Connector connector) {
        this.connector = connector;
        this.newsAdapter = newsAdapter;
    }

    public static void indexBookmarks(){
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
                Connector connector = Connector.getInstance();
                ArrayList<NewsCard> newsCards = connector.getAllBookmarks();
                for (NewsCard newsCard : newsCards) {
                    bookmarkSet.add(newsCard.id);
                }
                return null;
            }
        }.execute();
    }

    @Override
    protected Object doInBackground(Object[] params) {

        return null;
    }

    public void _execute(){
        ArrayList<NewsCard> allBookmarks = connector.getAllBookmarks();
        Collections.reverse(allBookmarks);
        newsAdapter.clear();
        newsAdapter.addAll(allBookmarks);
    }
    public static void refresh(){
        ArrayList<NewsCard> allBookmarks = Connector.connector.getAllBookmarks();
        Collections.reverse(allBookmarks);
        final ListAdapter registeredBookmarkAdapter = Registry.getInstance().getRegisteredBookmarkAdapter();
        if (registeredBookmarkAdapter != null){
            registeredBookmarkAdapter.setNewsCardList(allBookmarks);
        }
        azUtils.refreshBookmarkFragment();
        azUtils.refreshFragment();
    }

    public static void init(){
        ArrayList<NewsCard> allBookmarks = Connector.connector.getAllBookmarks();
        Collections.reverse(allBookmarks);
        final ListAdapter registeredBookmarkAdapter = Registry.getInstance().getRegisteredBookmarkAdapter();
        if (registeredBookmarkAdapter != null){
            registeredBookmarkAdapter.setNewsCardList(allBookmarks);
        }
    }
}
