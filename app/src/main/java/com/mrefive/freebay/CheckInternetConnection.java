package com.mrefive.freebay;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by mrefive on 5/18/16.
 */
public class CheckInternetConnection {


    private Context context;

    public CheckInternetConnection(Context context) {
        this.context = context;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }



}
