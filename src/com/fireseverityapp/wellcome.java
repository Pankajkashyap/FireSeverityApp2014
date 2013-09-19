package com.fireseverityapp;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class wellcome extends Activity {
	TextView name, organisation, designation, email;
	DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		db = new DatabaseHandler(this);
		name = (TextView) findViewById(R.id.name);
		organisation = (TextView) findViewById(R.id.org);
		designation = (TextView) findViewById(R.id.desig);
		email = (TextView) findViewById(R.id.email);

		Log.d("Reading: ", "Reading all contacts..");

		List<Reg> contacts = db.getAllContacts();

		for (Reg cn : contacts) {

			name.setText(cn.getName().toString());
			email.setText(cn.get_email().toString());
			organisation.setText(cn.get_org().toString());
			designation.setText(cn.get_desig().toString());

			String log = "Id: " + cn.getID() + " ,Name: " + cn.getName()
					+ " ,email: " + cn.get_email() + " ,org: " + cn.get_org()
					+ " ,desig: " + cn.get_desig();
			Log.d("Name: ", log);
		}

	}

}
