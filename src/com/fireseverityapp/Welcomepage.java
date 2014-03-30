package com.fireseverityapp;

import java.util.ArrayList;
import java.util.List;

import com.fireseverityapp.MainActivity;
import com.fireseverityapp.TerminateActivity;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class Welcomepage extends Activity {
	
	
	private SharedPreferences mSettings;
	
	DatabaseHandler db;

	TextView userNameField;
	//TextView userEmailField;
	Button btnClear;
	Reg cn = new Reg();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//For fully exit application
		TerminateActivity.addActivity(this);

		setContentView(R.layout.activity_welcomepage);
		// Show the Up button in the action bar.
		setupActionBar();
		
		userNameField = (TextView)this.findViewById(R.id.userField);
		//userEmailField = (TextView)this.findViewById(R.id.regEmailField);
		
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
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
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
			cn = contacts.get(0);
			
			userNameField.setText(cn.getName().toString());
			//userEmailField.setText(cn.get_email().toString());
			
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
		getMenuInflater().inflate(R.menu.welcomepage, menu);
		return true;
	}

	public void enterApp(View v){
		
		AlertDialog alertDialog = new AlertDialog.Builder(Welcomepage.this).create();
		
		alertDialog.setMessage("Photo required, please take a bush fire image!");
		alertDialog.setCancelable(false);
		alertDialog.setButton(-1, "OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Welcomepage.this.enterMainActivity();
			}

		});
		alertDialog.show();

	}
	
	public void enterMainActivity(){
		Intent i = new Intent(this,MainActivity.class);
		if(cn != null){
			Bundle bundle = new Bundle();
			bundle.putSerializable("Reg", cn);
			i.putExtras(bundle);
		}
		this.startActivity(i);
	}

	public void enterUsermanual(View v){
		Intent i = new Intent(this,Usermanual.class);
		this.startActivity(i);
	}
	public void enterAbout(View v){
		Intent i = new Intent(this,About.class);
		this.startActivity(i);
	}
	public void enterExit(View v){
		TerminateActivity.onTerminate();
		//this.clearUser(v);
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
