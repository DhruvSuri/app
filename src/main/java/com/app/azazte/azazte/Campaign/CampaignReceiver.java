package com.app.azazte.azazte.Campaign;

/**
 * Created by home on 24/11/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.azazte.azazte.Utils.MixPanelUtils;
import com.mixpanel.android.mpmetrics.MixpanelAPI;


public class CampaignReceiver extends BroadcastReceiver {

    // The name of the referrer string broadcast by Google Play Store.
    private static final String PLAY_STORE_REFERRER_KEY = "referrer";

    @Override
    public void onReceive(Context context, Intent intent) {

        String referrer = intent.getStringExtra(PLAY_STORE_REFERRER_KEY);
        try {
            MixpanelAPI api = MixpanelAPI.getInstance(context, MixPanelUtils.APP_KEY);
            api.track("REF");
            api.track(referrer);
        } catch (Exception ignored) {
        }
    }
}
