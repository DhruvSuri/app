package com.app.azazte.azazte.Utils;

import android.content.SharedPreferences;

/**
 * Created by sprinklr on 09/04/16.
 */
public class SharedPreferencesUtils {
    public static SharedPreferences sharedPreferences;

    public static void storePref(String key, String value) {
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }
}
