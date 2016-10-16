package com.app.azazte.azazte.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.azazte.azazte.Beans.Bubble;
import com.app.azazte.azazte.Beans.NewsCard;

import java.util.ArrayList;
import java.util.List;

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
    private static final String NEWS_IMPACT_LABEL = "impactLabel";
    private static final String NEWS_CREATED_TIME_EPOCH = "createdTimeEpoch";

    private static final String BUBBLE_KEY = "key";
    private static final String BUBBLE_VALUE = "value";
    private static final String BUBBLE_STORY_ID = "storyId";
    private static final String BUBBLE_ID = "id";
    private static final String BUBBLE_TWITTER = "twitter";

    public static Connector connector;

    public Connector(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, 12);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table news (id text primary key, imageUrl text,memoryImageUrl text,newsHeading text,newsContent text, newsSourceUrl text,newsSourceName text,date text,createdTimeEpoch text,place text,category text,isBookmarked integer,author text,impact text,impactLabel text,sentiment integer)");
        db.execSQL("create table bubble (id text primary key, storyId text,key text,value text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS news");
        db.execSQL("DROP TABLE IF EXISTS bubble");
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
        contentValues.put(NEWS_CREATED_TIME_EPOCH, newsCard.createdTimeEpoch);
        contentValues.put(NEWS_COLUMN_CATEGORY, newsCard.category);
        contentValues.put(NEWS_AUTHOR, newsCard.author);
        contentValues.put(NEWS_SENTIMENT, newsCard.sentiment);
        contentValues.put(NEWS_IMPACT, newsCard.impact);
        contentValues.put(NEWS_IMPACT_LABEL, newsCard.impactLabel);

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
        Cursor res = db.rawQuery("select * from news where id=\"" + id + "\"", null);
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

    public ArrayList<Bubble> getBubbles(String storyId) {
        ArrayList<Bubble> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from bubble where " + BUBBLE_STORY_ID + "=\"" + storyId + "\"", null);
        res.moveToFirst();
        Bubble bubble;
        while (res.isAfterLast() == false) {
            bubble = fillBubble(res);
            array_list.add(bubble);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<Bubble> getBubble(String storyId, String key) {
        ArrayList<Bubble> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from bubble where " + BUBBLE_STORY_ID + "=\"" + storyId + "\"" + "and " + BUBBLE_KEY + "=\"" + key + "\"", null);
        res.moveToFirst();
        Bubble bubble;
        while (res.isAfterLast() == false) {
            bubble = fillBubble(res);
            array_list.add(bubble);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    private Bubble fillBubble(Cursor res) {
        Bubble bubble = new Bubble();
        bubble.setStoryId(res.getString(res.getColumnIndex(BUBBLE_STORY_ID)));
        bubble.setQuestion(res.getString(res.getColumnIndex(BUBBLE_KEY)));
        bubble.setAnswer(res.getString(res.getColumnIndex(BUBBLE_VALUE)));
        bubble.setId(res.getString(res.getColumnIndex(BUBBLE_ID)));
        return bubble;
    }

    public boolean insertBubble(Bubble bubble) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BUBBLE_ID, bubble.getId());
        contentValues.put(BUBBLE_STORY_ID, bubble.getStoryId());
        contentValues.put(BUBBLE_KEY, bubble.getQuestion());
        contentValues.put(BUBBLE_VALUE, bubble.getAnswer());
        db.insert("bubble", null, contentValues);
        return true;
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
        newsCard.impactLabel = res.getString(res.getColumnIndex(NEWS_IMPACT_LABEL));
        newsCard.createdTimeEpoch = res.getString(res.getColumnIndex(NEWS_CREATED_TIME_EPOCH));
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

    public void saveBubbles(List<Bubble> bubbleList, String storyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM bubble where " + BUBBLE_STORY_ID + "=\"" + storyId + "\"");
        for (Bubble bubble : bubbleList) {
            insertBubble(bubble);
        }
    }

    public ArrayList<NewsCard> getAllNewsByCategory(List<NewsCard> allNews, Integer category) {
        ArrayList<NewsCard> list = new ArrayList<>();
        for (NewsCard newsCard : allNews) {
            int cardCategory = 0;
            String[] split = newsCard.category.split(",");

            for (String categoryString : split) {
                try {
                    cardCategory = Integer.valueOf(categoryString);
                } catch (Exception e) {
                    cardCategory = 0;
                }
                if (category == cardCategory || category == 0) {
                    list.add(newsCard);
                    break;
                }
            }
        }
        return list;
    }

    public ArrayList<NewsCard> getAllNewsByCategory(Integer category) {
        return getAllNewsByCategory(this.getAllNews(), category);
    }
}