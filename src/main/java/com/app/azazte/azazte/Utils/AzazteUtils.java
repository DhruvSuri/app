package com.app.azazte.azazte.Utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

/**
 * Created by home on 16/10/16.
 */
public class AzazteUtils {
    private static AzazteUtils instance = new AzazteUtils();
    private AppCompatActivity context;
    private static Integer imageViewHeight;
    private static Integer bubbleHeight;
    private static Integer bubbleWidth;

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

    public void intialize(AppCompatActivity context) {
        this.context = context;
        getDimensions();
    }

    public void getDimensions() {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        imageViewHeight = (int)(height*0.37);
        bubbleHeight = (int)(height*0.13);
        bubbleWidth = (int) (bubbleHeight *1.4);
    }
}
