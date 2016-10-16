package com.app.azazte.azazte.Beans;

/**
 * Created by home on 12/08/16.
 */
public class NotificationObject {
    private String headline;
    private String id;
    private String imageUrl;

    public String getImageUrl() {


        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
