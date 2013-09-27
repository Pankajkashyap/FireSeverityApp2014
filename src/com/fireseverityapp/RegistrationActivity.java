package com.fireseverityapp;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends Activity {
	EditText name, organisation, designation, email;
	String reg_name, reg_organisation, reg_designation, reg_email;
	Button submit;
	DatabaseHandler db;
	private final int PLEASE_WAIT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		name = (EditText) findViewById(R.id.tv_name);
		organisation = (EditText) findViewById(R.id.tv_organisation);
		designation = (EditText) findViewById(R.id.tv_Designation);
		email = (EditText) findViewById(R.id.tv_Email);
		submit = (Button) findViewById(R.id.btn_Submit);
		db = new DatabaseHandler(this);
		db.getWritableDatabase();

		// reg_name=name.getText().toString();

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addUser();
				Intent i=new Intent(RegistrationActivity.this,Welcompage.class);
				startActivity(i);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_registration, menu);
		return true;
	}

	
	
	public void addUser() {
		
		Log.d("Insert: ", "Inserting ..");
		// Reg structure: Name, Organization, Designation, Email
		db.addContact(new Reg(name.getText().toString(),organisation.getText().toString(),designation.getText().toString(),email.getText().toString()));
		
		
		Log.d("Reading: ", "Reading all contacts..");
		List<Reg> contacts = db.getAllContacts();

		for (Reg cn : contacts) {
			String log = "Id: " + cn.getID() + " ,Name: " + cn.getName()+ " ,email: " + cn.get_email()+ " ,org: " + cn.get_org()+ " ,desig: " + cn.get_desig();
		
			Log.d("Name: ", log);
							

		}
		
	
	}
	
	
	
	/*private void inFo(String message) {
		String help = message;
		AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
		builder.setMessage(help).setCancelable(true);
		builder.setTitle("xyz");
		builder.setIcon(R.drawable.ic_launcher);
		builder.setCancelable(true);
		builder.setIcon(null);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}

		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	*/
}
