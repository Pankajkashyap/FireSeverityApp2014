package com.fireseverityapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class NotificationActivity extends Activity {
	
	TextView messageField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		
		//For fully exit application
		TerminateActivity.addActivity(this);
		
		Intent i = this.getIntent();
		Bundle bundle = i.getExtras();
		
		messageField = (TextView) this.findViewById(R.id.textView1);
		messageField.setText(bundle.getString("noticeMsg"));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification, menu);
		return true;
	}
	
	public void enterWelcomePage(View v){
		Intent i = new Intent(this,Welcomepage.class);
		this.startActivity(i);
	}	
	
	public void enterFinish(View v){
		TerminateActivity.onTerminate();
	}


}
