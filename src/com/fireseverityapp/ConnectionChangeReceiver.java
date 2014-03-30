package com.fireseverityapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/*
 * Author Kavy 15/09/2013
 */

public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null) {
        	Intent	servicesIntent = new Intent(context,Chknet.class);
    		context.startService(servicesIntent);
        } else {
            Log.v("@@@","Receiver : " + "No network");
        }
    }
}