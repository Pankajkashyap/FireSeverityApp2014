package com.fireseverityapp;

import java.util.ArrayList;
import java.util.List;

import com.fireseverityapp.MainActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class Welcompage extends Activity {
	
	
	private SharedPreferences mSettings;
	
	DatabaseHandler db;

	TextView userNameField;
	TextView userEmailField;
	Button btnClear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcompage);
		// Show the Up button in the action bar.
		setupActionBar();
		
		userNameField = (TextView)this.findViewById(R.id.userField);
		userEmailField = (TextView)this.findViewById(R.id.userEmailField);
		
		boolean isExist = isUserExist(this);
		// If User First Login
		if(!isExist){
			//Intent i = new Intent(this,RegisterActivity.class);
			Intent i = new Intent(this, RegistrationActivity.class);
			this.startActivity(i);
		}
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	private boolean isUserExist(Context mContext){
		boolean checked = false;
		db = new DatabaseHandler(mContext);
		
		int count = db.getContactsCount();
		List<Reg> contacts = new ArrayList<Reg>();

		if(count > 0){
			contacts = db.getAllContacts();
			
			// get the first contact info
			Reg cn = contacts.get(0);
			
			userNameField.setText(cn.getName().toString());
			userEmailField.setText(cn.get_email().toString());
			
			String log = "Id: " + cn.getID() + " ,Name: " + cn.getName()
					+ " ,email: " + cn.get_email() + " ,org: " + cn.get_org()
					+ " ,desig: " + cn.get_desig();
			Log.d("Name: ", log);
			
			checked = true;
		}

		return checked;
	}
	
	/*
	private boolean isUserExist(){
		boolean checked = false;
		
		Context mContext = this.getApplicationContext();
		
		mSettings = mContext.getSharedPreferences(RegisterActivity.PREF_NAME, Context.MODE_PRIVATE);
		String storeName = mSettings.getString(RegisterActivity.USER_NAME, null);
		if(storeName!=null){
			userNameField.setText(mSettings.getString(RegisterActivity.USER_NAME, null));
			userEmailField.setText(mSettings.getString(RegisterActivity.USER_EMAIL, null));
			checked = true;
		}
		
		return checked;
	}
	*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcompage, menu);
		return true;
	}

	public void enterApp(View v){
		Intent i = new Intent(this,MainActivity.class);
		this.startActivity(i);
	}
	
	public void clearUser(View v){
		/*
		Editor preEditor = mSettings.edit();
		preEditor.remove(RegisterActivity.USER_NAME);
		preEditor.remove(RegisterActivity.USER_EMAIL);
		preEditor.clear();
		preEditor.commit();
		
		String testing = mSettings.getString(RegisterActivity.USER_NAME, null);
		*/
		db.deleteTable();
		
	}
	
}
