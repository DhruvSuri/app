package com.app.azazte.azazte.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.app.azazte.azazte.Beans.NotificationObject;
import com.app.azazte.azazte.MainActivity;
import com.app.azazte.azazte.PrefManager;
import com.app.azazte.azazte.R;
import com.app.azazte.azazte.Utils.MixPanelUtils;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by sprinklr on 24/03/16.
 */
public class GCMIntentService extends IntentService {
    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    public static String FINUP = "Finup";
    public static String ID = "id";

    public GCMIntentService() {
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        try {
            PrefManager.init(this.getApplicationContext());
            if (PrefManager.getInstance().getNotificationState().equals(PrefManager.NOTIFICATION_STATE_OFF)) {
                MixPanelUtils.track(MixPanelUtils.NOTIFICATION + "TURNED_OFF_NOTIFICATIONS");
                GcmBroadcastReceiver.completeWakefulIntent(intent);
                return;
            }
        } catch (Exception ignored) {
        }

        MixPanelUtils.init(this);
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String recieved_message;
                if (intent.getExtras().containsKey("mp_message")) {
                    recieved_message = intent.getExtras().getString("mp_message");
                    //mp_message now contains the notification's text
                } else {
                    recieved_message = intent.getStringExtra("message");
                    // Not sent from mixpanel, you can do whatever you'd like
                }
                sendNotification(recieved_message);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String jsonString) {
        String msg;
        NotificationObject notificationObject = new Gson().fromJson(jsonString, NotificationObject.class);
        msg = notificationObject.getHeadline();
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ID, notificationObject.getId());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        MixPanelUtils.track(MixPanelUtils.NOTIFICATION);
        MixPanelUtils.track(MixPanelUtils.NOTIFICATION + msg);
        NotificationCompat.Style finalStyle;
        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setSummaryText(msg);
        Bitmap bitmap = null;
        try {
            String imageUrl = notificationObject.getImageUrl();
            if (imageUrl == null){
                throw new Exception("Image not found");
            }
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
            notiStyle.bigPicture(bitmap);
            finalStyle = notiStyle;
        } catch (Exception e) {
            e.printStackTrace();
            finalStyle = new NotificationCompat.BigTextStyle().bigText(msg);
        }


        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.notification)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon))
                        .setContentTitle(FINUP)
                        .setStyle(finalStyle)
                        .setContentText(msg)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        try {
            mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());
        } catch (Exception ignored) {

        }
    }
}
