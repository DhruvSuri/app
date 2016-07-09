package com.app.azazte.azazte.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.app.azazte.azazte.GCM.GCMUtils;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.IOException;

/**
 * Created by sprinklr on 22/04/16.
 */
public class MixPanelUtils {
    public static final String APP_KEY = "4e9a7029258e8a7e57d88b463561adf9";
    public static final String ON_MORE_AT = "MORE_AT_EVENT : ";
    public static final String SHARE = "SHARE_EVENT :";
    public static final String BOOKMARK = "BOOKMARK_EVENT : ";
    public static final String NOTIFICATION = "NOTIFICATION : ";
    public static final String ALL_NEWS = "ALL_NEWS";
    public static final String INCOME_TAX = "INCOME_TAX";
    public static final String SERVICE_TAX_VAT = "SERVICE_TAX_VAT";
    public static final String EXCISE_CUSTOMS = "EXCISE_CUSTOMS";
    public static final String COMPANY_LAW = "COMPANY_LAW";
    public static final String ECONOMY_FINANCE = "ECONOMY_FINANCE";
    public static final String REGULATORY = "REGULATORY";
    public static final String OTHERS = "OTHERS";
    public static final String COMMENT = "COMMENT";
    public static final String HEART = "HEART";

    public static MixpanelAPI mixpanelAPI;
    public static Context mixPanelContext;

    public static void init(Context context) {
        try {
            mixpanelAPI = MixpanelAPI.getInstance(context, APP_KEY);
            mixPanelContext = context;
        } catch (Exception e) {
            Log.d("init", "init: initialization failed");
            Crashlytics.log("initialization failed" + e.getMessage());
        }
    }

    public static void setIdentity(String emailAddress) {
        try {
            String id = Build.BRAND + "_" + Build.SERIAL + "_" + Build.ID;
            mixpanelAPI.identify(id);
            mixpanelAPI.getPeople().identify(id);
            mixpanelAPI.getPeople().set("$email", emailAddress);
        } catch (Exception e) {
            Crashlytics.log("Failed : setting identity for mixpanel" + e.getMessage());
        }
    }


    public static void track(String s) {
        try {
            mixpanelAPI.track(s);
        } catch (Exception e) {
            Log.d("", "track: faield to track" + s);
            Crashlytics.log("failed : tracking failed for mixpanel : " + s + "  " + e.getMessage());
        }
    }

    public static void timeEvent(String s) {
        try {
            mixpanelAPI.timeEvent(s);
        } catch (Exception e) {
            Crashlytics.log("failed : tracking event failed : " + s + "  " + e.getMessage());
        }
    }

    public static void setGCM() {
        try {
            String id = Build.BRAND + "_" + Build.SERIAL + "_" + Build.ID;
            mixpanelAPI.getPeople().identify(id);
            mixpanelAPI.getPeople().initPushHandling(GCMUtils.SENDER_ID);
        } catch (Exception e) {
            Log.d("", "setting up gcm");
        }
    }


    public static void fetchRegistrationId() {

        try {
            final SharedPreferences shaPreferences = mixPanelContext.getSharedPreferences("ShaPreferences", Context.MODE_PRIVATE);

            new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    String msg = "";

                    try {
                        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(mixPanelContext);
                        String regId = gcm.register(GCMUtils.SENDER_ID);
                        mixpanelAPI.getPeople().set("ADF", regId);
                        mixpanelAPI.getPeople().setPushRegistrationId(regId);
                    } catch (IOException ex) {
                        msg = "Error :" + ex.getMessage();
                    }
                    return msg;
                }
            }.execute();
        } catch (Exception ignored) {

        }

    }
}
