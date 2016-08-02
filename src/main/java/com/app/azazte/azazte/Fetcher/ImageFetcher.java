package com.app.azazte.azazte.Fetcher;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Utils.SharedPreferencesUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sprinklr on 21/03/16.
 */
public class ImageFetcher extends AsyncTask<String, Void, Bitmap> {
    static Context context;
    List<NewsCard> newsCards;
    public static ImageFetcher imageFetcherInstace;
    public static Map<Integer, Bitmap> imageMap = new HashMap<>();

    public ImageFetcher(Activity activity) {
        context = activity;
    }

    public ImageFetcher() {

    }

    public void indexImages() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Connector connector = Connector.getInstance();
                ArrayList<NewsCard> newsCards = connector.getAllNews();
                for (NewsCard newsCard : newsCards) {
                    storeImageInMemory(newsCard.id, downloadImage(newsCard.imageUrl));
                }
                return null;
            }
        }.execute();
    }

    protected Bitmap doInBackground(String... urls) {
        Connector connector = Connector.getInstance();
        ArrayList<NewsCard> newsCards = connector.getAllNews();
        for (NewsCard newsCard : newsCards) {
            if (newsCard.memoryImageUrl == null) {
                storeImageInSharedPreferences(newsCard.id, downloadImage(newsCard.imageUrl));
            }
        }
        return null;
    }

    protected void onPostExecute(Bitmap result) {
        //bmImage.setImageBitmap(result);
    }

    public Bitmap downloadImage(String imageUrl) {
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(imageUrl).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    public static String storeImageInMemory(String id, Bitmap bitmap) {

        //imageMap.put(id, bitmap);
        String memoryImageUrl = id + "_azazte_image.png";
        FileOutputStream fos = null;
        // Writing the bitmap to the output stream
        try {
            fos = context.openFileOutput(memoryImageUrl, Context.MODE_WORLD_READABLE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            storeImageURLInDb(id, memoryImageUrl);
            return memoryImageUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String storeImageInSharedPreferences(String id, Bitmap bitmap) {

        //imageMap.put(id, bitmap);
        SharedPreferences.Editor editor = SharedPreferencesUtils.sharedPreferences.edit();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        editor.putString(id + "azazte", Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
        editor.apply();
        return null;
    }

    public static void storeImageURLInDb(String id, String memoryImageUrl) {
        Connector connector = Connector.getInstance();
        connector.insertImageUrl(id, memoryImageUrl);
    }

    public Bitmap fetchImageFromMemory(String imageUrl) {
        File file = null;
        try {
            file = context.getFileStreamPath(imageUrl);
        } catch (Exception e) {
            //return downloadImage(imageUrl);
            return null;
        }

        FileInputStream streamIn = null;
        try {
            streamIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(streamIn);
    }

    public void storeImageList(List<NewsCard> newsCardList) {
        this.newsCards = newsCardList;
        imageFetcherInstace.execute();

    }

    public void storeImage(NewsCard newsCard) {
        storeImageInMemory(newsCard.id, downloadImage(newsCard.imageUrl));
    }
}
