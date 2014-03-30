package com.fireseverityapp;

import android.content.Context;
import android.net.ConnectivityManager;

public class Utills {

	public static boolean isNetworkconn(Context cons) {
		ConnectivityManager conMgr = (ConnectivityManager) cons
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}
}
