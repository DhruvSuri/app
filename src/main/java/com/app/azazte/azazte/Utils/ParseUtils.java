package com.app.azazte.azazte.Utils;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by home on 04/07/16.
 */
public class ParseUtils {
    private static String APPLICATION_ID = "jnhPhocaM3UeVBeZgIAxrKh6VMdcKWsbft4diy8G";
    private static String CLIENT_ID = "r0QUgNiKTrO5y6gOolAz8UnD8t5QVKjDdsRxrJVA";


    public static void registerParse(Context context) {
        // initializing parse library
        //Parse.enableLocalDatastore(context);
        Parse.initialize(context, APPLICATION_ID, CLIENT_ID);
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                String deviceToken = (String) ParseInstallation.getCurrentInstallation().get("deviceToken");
                //Log.e(ParseUtils.class.getSimpleName(), deviceToken);
            }
        });

        ParsePush.subscribeInBackground("Parse", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.e(ParseUtils.class.getSimpleName(), "Successfully subscribed to Parse!");
            }
        });


        //ParseUser.enableAutomaticUser();
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
    }
}
