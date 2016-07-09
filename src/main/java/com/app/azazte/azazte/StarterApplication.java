package com.app.azazte.azazte;

import android.app.Application;

import com.app.azazte.azazte.Utils.ParseUtils;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by home on 04/07/16.
 */
public class StarterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseUtils.registerParse(this);
    }
}
