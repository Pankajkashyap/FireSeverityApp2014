package com.fireseverityapp;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/*
 * @Author Ling An Lin 08/09/2013
 * @Attributes enum ErrorCode - CAM_CANCELED
 * 
 * ErrorPage Activity defines action if received errorCode
 * Enum ErrorCode can be extended
 */
public class ErrorPage extends Activity {
	
	Context context;
	ErrorCode errorCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//For fully exit application
		TerminateActivity.addActivity(this);

		setContentView(R.layout.activity_errorpage);
		
		Bundle datas = this.getIntent().getExtras();
		errorCode = ErrorCode.valueOf((String)datas.getString("ErrorCode"));
		
		
		switch(errorCode){
		case CAM_CANCELED:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Please Capture Bush Fire Image!!")
			       .setCancelable(false)
			       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   //Forward back to Welcome page			              
			        	   Intent intent = new Intent();
			               intent.setClass(ErrorPage.this, Welcomepage.class);
			               startActivity(intent);
			               ErrorPage.this.finish();

			           }
			       });
			//create alert dialog
			AlertDialog alert = builder.create();
			
			//show alert dialog
			alert.show();
			
		}
		
	
	}

	
	public enum ErrorCode{
		
		CAM_CANCELED
		
	}

}
