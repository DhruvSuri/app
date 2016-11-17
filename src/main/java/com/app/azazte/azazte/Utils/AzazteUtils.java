package com.app.azazte.azazte.Utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by home on 16/10/16.
 */
public class AzazteUtils {
    private static AzazteUtils instance = new AzazteUtils();
    private AppCompatActivity context;
    private static Integer imageViewHeight;
    private static Integer imageViewWidth;
    private static Integer bubbleHeight;
    private static Integer bubbleWidth;
    private static String CHANNEL_LINK_DOMAIN = "http://aws.azazte.com/service/rest/bubble/redirect?storyId=%s&channelName=%s";
    private static String CHANNEL_IMAGE_DOMAIN = "http://aws.azazte.com/images/%s";

    private AzazteUtils() {
    }

    public static AzazteUtils getInstance() {
        return instance;
    }

    public Integer getBubbleWidth() {
        return bubbleWidth;
    }

    public Integer getBubbleHeight() {
        return bubbleHeight;
    }

    public Integer getImageViewHeight() {
        return imageViewHeight;
    }

    public Integer getImageViewWidth(){
        return imageViewWidth;
    }

    public void intialize(AppCompatActivity context) {
        this.context = context;
        getDimensions();
    }

    public void getDimensions() {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;
        int screenWidth = metrics.widthPixels;
        imageViewHeight = (int) (screenHeight * 0.37);
        imageViewWidth = (int) screenWidth;

        bubbleHeight = (int) (screenHeight * 0.13);
        bubbleWidth = (int) (bubbleHeight * 1.4);
    }

    public String getBubbleLinkURL(String id, String channel) {
        return String.format(CHANNEL_LINK_DOMAIN, id, channel);
    }

    public String getBubbleChannelURL(String channel) {
        return String.format(CHANNEL_IMAGE_DOMAIN, channel);
    }

    public void setImageIntoView(Context context, ImageView imageView, String imageUrl, int placeholder) {
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .placeholder(placeholder) // can also be a drawable
                .into(imageView);
    }
}
