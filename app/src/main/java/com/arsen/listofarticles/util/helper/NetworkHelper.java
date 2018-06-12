package com.arsen.listofarticles.util.helper;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

public final class NetworkHelper {
    private NetworkHelper() {}

    public static boolean isNetworkAvailable(AppCompatActivity appCompatActivity) {
        ConnectivityManager cm =
                (ConnectivityManager) appCompatActivity.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        return false;
    }

}
