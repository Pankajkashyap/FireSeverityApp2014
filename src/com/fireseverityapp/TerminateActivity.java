package com.fireseverityapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/*
 * @Author Ling An Lin 08/09/2013
 * 
 * Fully terminate all activities in application
 */
public class TerminateActivity {  
	  
	private static List<Activity> activities = new ArrayList<Activity>();  
  
	/* 	TerminateActivity.addActivity(this) should be called in every activity(onCreate())
	 *  in order to record how many activities have been executed.
	 */
    public static void addActivity(Activity activity) {  
        activities.add(activity);  
    }  
  
    /*
     * onTerminate() will be called when fully exit game application
     */
    public static void onTerminate() {  
          
        for (Activity activity : activities) {  
            activity.finish();  
        }  
                    
        android.os.Process.killProcess(android.os.Process.myPid()); 
    }  
    
}  