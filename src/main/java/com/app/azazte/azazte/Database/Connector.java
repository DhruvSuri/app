package com.app.azazte.azazte.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.azazte.azazte.Beans.NewsCard;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by sprinklr on 24/03/16.
 */
public class Connector extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "azazte.news.new";
    public static final String NEWS_TABLE_NAME = "news";
    public static final String NEWS_COLUMN_ID = "id";
    public static final String NEWS_COLUMN_IMAGE_URL = "imageUrl";
    public static final String NEWS_COLUMN_MEMORY_IMAGE_URL = "memoryImageUrl";
    public static final String NEWS_COLUMN_HEADING = "newsHeading";
    public static final String NEWS_COLUMN_BODY = "newsContent";
    public static final String NEWS_COLUMN_SOURCE_URL = "newsSourceUrl";
    public static final String NEWS_COLUMN_SOURCE_NAME = "newsSourceName";
    public static final String NEWS_COLUMN_IS_BOOKMARKED = "isBookmarked";
    public static final String NEWS_COLUMN_DATE = "date";
    public static final String NEWS_COLUMN_CATEGORY = "category";
    public static final String NEWS_AUTHOR = "author";
    public static final String NEWS_IMPACT = "impact";
    public static final String NEWS_SENTIMENT = "sentiment";

    public static Connector connector;

    public Connector(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table news (id text primary key, imageUrl text,memoryImageUrl text,newsHeading text,newsContent text, newsSourceUrl text,newsSourceName text,date text,place text,category text,isBookmarked integer,author text,impact text,sentiment integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS news");
        onCreate(db);
    }

    public boolean insertNews(NewsCard newsCard) {

        final int count = getData(newsCard.id);
        if (count > 0) {
            return true;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NEWS_COLUMN_ID, newsCard.id);
        contentValues.put(NEWS_COLUMN_IMAGE_URL, newsCard.imageUrl);
        contentValues.put(NEWS_COLUMN_HEADING, newsCard.newsHead);
        contentValues.put(NEWS_COLUMN_BODY, newsCard.newsBody);
        contentValues.put(NEWS_COLUMN_SOURCE_URL, newsCard.newsSourceUrl);
        contentValues.put(NEWS_COLUMN_SOURCE_NAME, newsCard.newsSourceName);
        contentValues.put(NEWS_COLUMN_DATE, newsCard.date);
        contentValues.put(NEWS_COLUMN_CATEGORY, newsCard.category);
        contentValues.put(NEWS_AUTHOR, newsCard.author);
        contentValues.put(NEWS_SENTIMENT, newsCard.sentiment);
        contentValues.put(NEWS_IMPACT, newsCard.impact);

        db.insert("news", null, contentValues);
        return true;
    }

    public boolean insertImageUrl(String id, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NEWS_COLUMN_MEMORY_IMAGE_URL, imageUrl);
        db.update("news", contentValues, "id = ? ", new String[]{id});
        return true;
    }

    public boolean setBookmarked(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NEWS_COLUMN_IS_BOOKMARKED, 1);
        db.update("news", contentValues, "id = ? ", new String[]{id});
        return true;
    }

    public boolean unsetBookmarked(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NEWS_COLUMN_IS_BOOKMARKED, 0);
        db.update("news", contentValues, "id = ? ", new String[]{id});
        return true;
    }

    public int getData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from news where id=\"" + id + "\"", null);
        final int count = res.getCount();
        res.close();
        return count;
    }

    public ArrayList<NewsCard> getAllNews() {
        ArrayList<NewsCard> array_list = new ArrayList<NewsCard>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from news", null);
        res.moveToFirst();
        NewsCard newsCard;
        while (res.isAfterLast() == false) {
            newsCard = fillNewsCard(res);
            array_list.add(newsCard);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public NewsCard getNewsById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from news where id=" + id, null);
        res.moveToFirst();
        NewsCard newsCard = fillNewsCard(res);
        res.close();
        return newsCard;
    }

    private NewsCard fillNewsCard(Cursor res) {
        NewsCard newsCard = new NewsCard();
        newsCard.id = res.getString(res.getColumnIndex(NEWS_COLUMN_ID));
        newsCard.newsHead = res.getString(res.getColumnIndex(NEWS_COLUMN_HEADING));
        newsCard.newsBody = res.getString(res.getColumnIndex(NEWS_COLUMN_BODY));
        newsCard.newsSourceUrl = res.getString(res.getColumnIndex(NEWS_COLUMN_SOURCE_URL));
        newsCard.newsSourceName = res.getString(res.getColumnIndex(NEWS_COLUMN_SOURCE_NAME));
        newsCard.imageUrl = res.getString(res.getColumnIndex(NEWS_COLUMN_IMAGE_URL));
        newsCard.memoryImageUrl = res.getString(res.getColumnIndex(NEWS_COLUMN_MEMORY_IMAGE_URL));
        newsCard.isBookmarked = res.getInt(res.getColumnIndex(NEWS_COLUMN_IS_BOOKMARKED));
        newsCard.date = res.getString(res.getColumnIndex(NEWS_COLUMN_DATE));
        newsCard.category = res.getString(res.getColumnIndex(NEWS_COLUMN_CATEGORY));
        newsCard.impact = res.getString(res.getColumnIndex(NEWS_IMPACT));
        newsCard.sentiment = res.getString(res.getColumnIndex(NEWS_SENTIMENT));
        newsCard.author = res.getString(res.getColumnIndex(NEWS_AUTHOR));
        return newsCard;
    }

    public static Connector getInstance() {
        return connector;
    }

    public ArrayList<NewsCard> getAllBookmarks() {
        ArrayList<NewsCard> array_list = new ArrayList<NewsCard>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from news where isBookmarked = 1", null);
        res.moveToFirst();
        NewsCard newsCard;
        while (res.isAfterLast() == false) {
            newsCard = fillNewsCard(res);
            array_list.add(newsCard);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public void saveNewsInDb(List<NewsCard> newsCards) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<NewsCard> allBookmarks = Connector.getInstance().getAllBookmarks();
        db.execSQL("DELETE FROM news");
        for (NewsCard newsCard : newsCards) {
            connector.insertNews(newsCard);
        }
        for (NewsCard newsCard : allBookmarks) {
            setBookmarked(newsCard.id);
        }
    }
}
