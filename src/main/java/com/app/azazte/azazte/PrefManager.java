package com.app.azazte.azazte;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String NEWS_CARD_ID = "NEWS_CARD_ID";
    private static PrefManager instance;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    public static final String KEY_NOTIFICATION = "KEY_NOTIFICATION";
    public static final String KEY_IMAGE = "KEY_IMAGE";
    public static final String NOTIFICATION_STATE_ON = "ON";
    public static final String NOTIFICATION_STATE_OFF = "OFF";
    public static final String IMAGE_STATE_OFF = "IMAGE_OFF";
    public static final String IMAGE_STATE_ON = "IMAGE_ON";

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "finup";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new PrefManager(context);
        }
    }

    public static PrefManager getInstance() {
        return instance;
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setNotificationOff() {
        editor.putString(KEY_NOTIFICATION, NOTIFICATION_STATE_OFF);
        editor.commit();
    }

    public void setNotificationOn() {
        editor.putString(KEY_NOTIFICATION, NOTIFICATION_STATE_ON);
        editor.commit();
    }

    //returns true if notification is off
    public String getNotificationState() {
        return pref.getString(KEY_NOTIFICATION, NOTIFICATION_STATE_ON);
    }

    public void setImageOff() {
        editor.putString(KEY_IMAGE, IMAGE_STATE_OFF);
        editor.commit();
    }

    public void setImageOn() {
        editor.putString(KEY_IMAGE, IMAGE_STATE_ON);
        editor.commit();
    }

    //returns true if notification is off
    public String getImageState() {
        return pref.getString(KEY_IMAGE, IMAGE_STATE_ON);
    }


    public void saveNewsCardId(String id) {
        editor.putString(NEWS_CARD_ID, id);
        editor.commit();
    }

    public void unsetNewsCardId() {
        editor.putString(NEWS_CARD_ID, "null");
        editor.commit();
    }

    public String getNewsCardId() {
        return pref.getString(NEWS_CARD_ID, "null");
    }
}
