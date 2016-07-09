package com.app.azazte.azazte.Beans;

/**
 * Created by sprinklr on 20/03/16.
 */
public class NewsCard {
    public int id;
    public String imageUrl;
    public String memoryImageUrl;
    public String newsHead;
    public String newsBody;
    public String newsSourceUrl;
    public String newsSourceName;
    public int isBookmarked;
    public String date;
    public String category;
    public String author;
    public String impact;
    public String sentiment;


    public NewsCard() {
    }

    public NewsCard(int id, String imageUrl, String newsHead, String newsBody, String newsSource) {
        this.newsSourceUrl = newsSource;
        this.newsHead = newsHead;
        this.newsBody = newsBody;
        this.imageUrl = imageUrl;
        this.id = id;
    }
}
