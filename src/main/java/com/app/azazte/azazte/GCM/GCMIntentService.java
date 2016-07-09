package com.app.azazte.azazte.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.app.azazte.azazte.MainActivity;
import com.app.azazte.azazte.R;
import com.app.azazte.azazte.Utils.MixPanelUtils;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by sprinklr on 24/03/16.
 */
public class GCMIntentService extends IntentService {
    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMIntentService() {
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        try {
            SharedPreferences shaPreferences = getSharedPreferences("ShaPreferences", Context.MODE_PRIVATE);
            if (!shaPreferences.getBoolean("notification", true)) {
                MixPanelUtils.track(MixPanelUtils.NOTIFICATION + "TURNED_OFF_NOTIFICATIONS");
                GcmBroadcastReceiver.completeWakefulIntent(intent);
                return;
            }
        } catch (Exception ignored) {
        }

        MixPanelUtils.init(this);
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        if (!extras.isEmpty()) { // has effect of unparcelling Bundle
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String recieved_message;
                if (intent.getExtras().containsKey("mp_message")) {
                    recieved_message = intent.getExtras().getString("mp_message");
                    //mp_message now contains the notification's text
                } else { // Not sent from mixpanel, you can do whatever you'd like
                    recieved_message = intent.getStringExtra("message");
                }
                sendNotification(recieved_message);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        MixPanelUtils.track(MixPanelUtils.NOTIFICATION);
        MixPanelUtils.track(MixPanelUtils.NOTIFICATION + msg);
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.round_logo)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                R.drawable.notification))
                        .setContentTitle("azazte")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg)).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).setTicker("azazte")
                        .setContentText(msg);
        mBuilder.setPriority(0);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        try {
            mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());
        } catch (Exception ignored) {

        }
    }
}
