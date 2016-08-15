package com.app.azazte.azazte.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.app.azazte.azazte.Beans.FCMRequestDTO;
import com.app.azazte.azazte.GCM.GCMUtils;
import com.app.azazte.azazte.Utils.Api.ApiExecutor;
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
    public static final String CATEGORY = "CATEGORY : ";
    public static final String NEWS = "NEWS : ";

    public static MixpanelAPI mixpanelAPI;
    public static Context mixPanelContext;

    public static void init(Context context) {
        try {
            mixpanelAPI = MixpanelAPI.getInstance(context, APP_KEY);
            mixPanelContext = context;
        } catch (Exception e) {
            Crashlytics.log("initialization failed" + e.getMessage());
        }
    }

    public static void setIdentity(String emailAddress) {
        try {
            String id = getUniqueId();
            mixpanelAPI.identify(id);
            mixpanelAPI.getPeople().identify(id);
            mixpanelAPI.getPeople().set("$email", emailAddress);
        } catch (Exception e) {
            Crashlytics.log("Failed : setting identity for mixpanel" + e.getMessage());
        }
    }

    @NonNull
    private static String getUniqueId() {
        return Build.BRAND + "_" + Build.SERIAL + "_" + Build.ID;
    }


    public static void track(String s) {
        try {
            mixpanelAPI.track(s);
        } catch (Exception e) {
            Log.d("", "track: faield to track" + s);
            Crashlytics.log("failed : tracking failed for mixpanel : " + s + "  " + e.getMessage());
        }
    }

    public static void trackCategories(String categoryName) {
        try {
            mixpanelAPI.track(CATEGORY + categoryName);
        } catch (Exception e) {
            Crashlytics.log("failed : tracking failed for mixpanel : " + categoryName + "  " + e.getMessage());
        }
    }

    public static void trackNews(String newsHeadline){
        try {
            mixpanelAPI.track(NEWS + newsHeadline);
        } catch (Exception e) {
            Crashlytics.log("failed : tracking failed for mixpanel : " + newsHeadline + "  " + e.getMessage());
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
            String id = getUniqueId();
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
                        ApiExecutor.getInstance().sendIdToServer(new FCMRequestDTO(getUniqueId(),regId));
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
