package com.app.azazte.azazte.GCM;

import android.content.Context;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by sprinklr on 24/03/16.
 */
public class GCMUtils {
    public String getRegistrationId(Context context) {
        GoogleCloudMessaging instance = GoogleCloudMessaging.getInstance(context);
        return null;
    }

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String EMAIL = "email";
    public static final String USER_NAME = "dhruv";

    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public final static String SENDER_ID = "527444607031";

    public static String base_url = "http://192.168.1.33/gcm_demo/";

    public final static String register_url = base_url + "register.php";
    public final static String send_chat_url = base_url + "sendChatmessage.php";

    public static String registerationId;
}
