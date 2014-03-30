package com.fireseverityapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	// Preference Setting
	public static final String PREF_NAME = "FSLApp_UserInfo";
	public static final String USER_NAME = "user_name";
	public static final String USER_EMAIL = "user_email";
	
	private UserLoginTask mAuthTask = null;

	private SharedPreferences mSettings;
	Context mContext = this;
	
	Button btn_register;
	EditText user_name;
	EditText user_email;
	
	String nameField;
	String emailField;
	
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		btn_register = (Button)this.findViewById(R.id.btnRegister);
		user_name = (EditText)this.findViewById(R.id.regNameField);
		user_email = (EditText)this.findViewById(R.id.regEmailField);

		mSettings = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		editor = mSettings.edit();

		btn_register.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				nameField = user_name.getText().toString();
				emailField = user_email.getText().toString();
				attemptRegister();
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	
	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptRegister() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		user_name.setError(null);
		user_email.setError(null);

		boolean cancel = false;
		View focusView = null;

		// Check for a valid user name.
		if (TextUtils.isEmpty(nameField)) {
			user_name.setError(getString(R.string.error_field_required));
			focusView = user_name;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(emailField)) {
			user_email.setError(getString(R.string.error_field_required));
			focusView = user_email;
			cancel = true;
		} else if (!emailField.contains("@")) {
			user_email.setError(getString(R.string.error_invalid_email));
			focusView = user_email;
			cancel = true;
		}

		if (cancel) {
			// form field with an error.
			focusView.requestFocus();
		} else {
			// perform the user login attempt.
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}
	
	public void gotoWelcomePage(){
		Intent i = new Intent(this,Welcomepage.class);
		this.startActivity(i);
	}
	
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			
			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			// Store User Info to Preference
			editor.putString(USER_NAME, nameField);
			editor.putString(USER_EMAIL, emailField);
			editor.commit();

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;

			if (success) {
				Toast.makeText(mContext, "User Register Success!!", Toast.LENGTH_SHORT).show();
				gotoWelcomePage();
				finish();
			} 
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
	}
	

}
