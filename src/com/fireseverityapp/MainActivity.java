package com.fireseverityapp;

import com.fireseverityapp.R;
import com.fireseverityapp.mail.MailAccount;

import android.graphics.Bitmap;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MainActivity extends Activity {
	
	final int CAM_RESULT = 333333;
	static String FIRE_LEVEL_ATTRIBUTE = null;
	
	private Handler handler= new Handler();
		
	TextView latitude;
	TextView longitude;
	Button btnShow;
	ImageView imgPhoto;
	GPSLocationCapture gpsLocation;
	Spinner spinnerFSA;
	
	Context mContext = this;
	
	DatabaseHandler db;
	Reg reg = null;
	
	//ErrorCode
	String camCanceled = "CAM_CANCELED";
	//Email Details
	String tmppath = null; // Photo path
	String emailSenderId = null;
	String emailSenderPWD = null;
	String emailReceiver = null;
	String emailSubject = null;
	//Notification msg
	String notificationMsg = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//For fully exit application
		TerminateActivity.addActivity(this);
		
		// Initial Email Account Info - add by Ling An - 17-10-2013
		MailAccount mailAccount = new MailAccount();
		emailSenderId = mailAccount.getAccountID();
		emailSenderPWD = mailAccount.getPassword();
		emailReceiver = mailAccount.getReceiverEmail();
		emailSubject = mailAccount.getSubject();

		imgPhoto = (ImageView)this.findViewById(R.id.imageViewPhotoShot);
		reg = (Reg) this.getIntent().getSerializableExtra("Reg");
		// TODO: should fixed by error msg or direct error page
		if(reg == null){
			Toast.makeText(mContext, "Data error, cannot read user details!", Toast.LENGTH_SHORT).show();
			finish();
		}
				
		btnShow = (Button) findViewById(R.id.btnfindLocation);
		
		startCamera();
		
		btnShow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(tmppath != null){
					
					if (Utills.isNetworkconn(getApplicationContext())) {
						
						final ProgressDialog pDialog = new ProgressDialog(mContext);
						pDialog.setMessage("Sending Email...");
						pDialog.setCancelable(false);
						pDialog.show();

						Toast.makeText(MainActivity.this, "Sending Email from Gmail Sender...",
								Toast.LENGTH_LONG).show();
						
						new AsyncTask<Void, Void, Void>(){

							@Override
							protected Void doInBackground(Void... params) {
								sendEmailFromGmail();
								return null;
							}
							@Override
							protected void onPostExecute(Void result){
								pDialog.dismiss();
								directToNotificationPage();
							}
							
						}.execute();
					}

					else {
						saveDBWithoutSend();
						directToNotificationPage();
					}
					
					
				}else{
					Toast.makeText(mContext, "Data error, cannot send email!", Toast.LENGTH_SHORT).show();
				}
				
			}

		});
		
		
	}
	
	public void directToNotificationPage(){
		Intent i = new Intent(this,NotificationActivity.class);
		i.putExtra("noticeMsg", notificationMsg);
		this.startActivity(i);
	}
	
	public void listenerOnSpinner(){
		spinnerFSA = (Spinner) findViewById(R.id.spinnerFSA);
		spinnerFSA.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());
	}

	public void startCamera(){
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		this.startActivityForResult(camera, CAM_RESULT);
	}
	
	public void getLocation(){
		latitude = (TextView)this.findViewById(R.id.tvLatitude);
		longitude = (TextView)this.findViewById(R.id.tvLongitude);
		gpsLocation = new GPSLocationCapture(MainActivity.this);
		
		if(gpsLocation.isLocationAvailable()){
			double lat = gpsLocation.getLatitude();
            double lgit = gpsLocation.getLongitude();
            String provider = gpsLocation.getProvider();
            
            latitude.setText("Latitude:"+String.valueOf(lat));
            longitude.setText("Longitude:"+String.valueOf(lgit));
            
            Toast.makeText(this.getApplicationContext(), "Provider selected:"+provider, Toast.LENGTH_SHORT).show();
            
		}else{
			gpsLocation.showSettingsAlert();
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data){
		if(reqCode == CAM_RESULT && resCode == RESULT_OK){
			String[] proj = { MediaStore.Images.Media.DATA };
			
			Bitmap pic = null;
			
			if(data != null){
				Uri contentUri = data.getData();
				Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
			    if(cursor.moveToFirst()){;
			       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			       tmppath = cursor.getString(column_index);
			    }
				Log.e("Camera photo:path", "" + tmppath);
				pic = (Bitmap)data.getExtras().get("data");
				imgPhoto.setImageBitmap(pic);
			}
			
			getLocation();
			listenerOnSpinner();
			
		}
		if(resCode == Activity.RESULT_CANCELED){
			Intent errPage = new Intent(this, ErrorPage.class);
			errPage.putExtra("ErrorCode", camCanceled);
			this.startActivity(errPage);
		}
	}
	
	/*
	 * Data table structure
		String imagepath, 
		String priority,
		String latitude, 
		String longitude,
		String email,
		String name,
		String organization,
		String desig ) {
	*/
	public void saveDBWithoutSend() {
		DataBase DB = new DataBase(getApplicationContext());
		try {
			DB.open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DB.insert__loc(
				tmppath, 
				FIRE_LEVEL_ATTRIBUTE, 
				latitude.getText().toString(),
				longitude.getText().toString(),
				reg.get_email(),
				reg.getName(),
				reg.get_org(),
				reg.get_desig()
				);
		DB.close();
		notificationMsg = "Information has been stored in database, will be sent when in network connection!";
		Toast.makeText(MainActivity.this, notificationMsg,
				Toast.LENGTH_LONG).show();
	}

	
	public void sendEmailFromGmail(){
		GMailSender mailsender = new GMailSender(emailSenderId, emailSenderPWD);

		String[] toArr = {emailReceiver};
		mailsender.set_to(toArr);
		mailsender.set_from(emailSenderId);
		mailsender.set_subject(emailSubject + " From " + reg.getName());
		mailsender
				.setBody("Data Sent From "
						+ reg.getName()
						+ "\nLatitude = "
						+ latitude.getText().toString()
						+ "\nLongitude = "
						+ longitude.getText().toString()
						+ "\nFire Level Severity Attribute = "
						+ FIRE_LEVEL_ATTRIBUTE
						+ "\nOrganisation = "
						+ reg.get_org()
						+ " \nDesignation = "
						+ reg.get_desig()
						+ " \nEmail_id = "
						+ reg.get_email());

		try {
			try {
				
				if (!(tmppath.equalsIgnoreCase(""))) {
					mailsender.addAttachment(tmppath);
				}
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			if (mailsender.send()) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						notificationMsg = "Email was sent successfully.";
						Toast.makeText(
								MainActivity.this,
								notificationMsg,
								Toast.LENGTH_LONG)
								.show();

					}

				});
			} else {

				handler.post(new Runnable() {

					@Override
					public void run() {
						notificationMsg = "Email was not sent.";
						Toast.makeText(
								MainActivity.this,
								"Email was not sent.",
								Toast.LENGTH_LONG)
								.show();

					}

				});

			}
		} catch (Exception e) {

			 Log.e("MailApp",""+e.getMessage());
		}
	}

}
