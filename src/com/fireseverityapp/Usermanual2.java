package com.fireseverityapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class Usermanual2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usermanual2);
		
		//For fully exit application
		TerminateActivity.addActivity(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.usermanual2, menu);
		return true;
	}

	public void onClickBack(View v) {
		Intent activityIntent =  new Intent(this, Usermanual.class);
		this.startActivity(activityIntent);
	}
	public void onClickClose(View v) {
		Intent activityIntent =  new Intent(this, Welcomepage.class);
		this.startActivity(activityIntent);
	}
}
