package com.app.azazte.azazte.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import xdroid.toaster.Toaster;

/**
 * Created by sprinklr on 19/03/16.
 */
public class ConnectivityUtils {
    public static final String PLEASE_CONNECT_TO_INTERNET = "Please connect to internet";
    private static ConnectivityUtils INSTANCE = new ConnectivityUtils();

    public static ConnectivityUtils getConnectivityUtilsInstance() {
        return INSTANCE;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean checkAndDisplayToastOnInternetFailure(Context context){
        boolean networkAvailable = isNetworkAvailable(context);
        if (!networkAvailable) {
            Toaster.toast(PLEASE_CONNECT_TO_INTERNET);
        }
        return networkAvailable;
    }
}
