package com.fireseverityapp;

import com.fireseverityapp.R;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	final int CAM_RESULT = 333333;
	
	private SharedPreferences mSettings;

	//ErrorCode
	String camCanceled = "CAM_CANCELED";
	
	String userNameValue = null;
	String userEmailValue = null;

	TextView latitude;
	TextView longitude;
	Button btnShow;
	ImageView imgPhoto;
	GPSLocationCapture gpsLocation;
	Spinner spinnerFSA;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imgPhoto = (ImageView)this.findViewById(R.id.imageViewPhotoShot);
				
		btnShow = (Button) findViewById(R.id.btnfindLocation);
		btnShow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getLocation();
				
			}
		});
		
		startCamera();
		
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
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
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
			Bitmap pic = (Bitmap)data.getExtras().get("data");
			imgPhoto.setImageBitmap(pic);
			getLocation();
			listenerOnSpinner();
		}
		if(resCode == Activity.RESULT_CANCELED){
			Intent errPage = new Intent(this, ErrorPage.class);
			errPage.putExtra("ErrorCode", camCanceled);
			this.startActivity(errPage);
		}
	}
	
	public void sendEmail(){
		
		mSettings = this.getSharedPreferences(RegisterActivity.PREF_NAME, MODE_PRIVATE);
		
		userNameValue = mSettings.getString(RegisterActivity.USER_NAME, null);
		userEmailValue = mSettings.getString(RegisterActivity.USER_EMAIL, null);
		
		Intent mailIntent = new Intent(Intent.ACTION_SEND);
			mailIntent.setType("application/image");
			mailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"snowneco@gmail.com"});
			mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Fire Severity Level App");
			mailIntent.putExtra(Intent.EXTRA_TEXT   , "Bush Fire reporter, /n Latitude:"+ latitude + "/n Longitude:"+ longitude);
		
		try {
		    startActivity(Intent.createChooser(mailIntent, "Choose an Email client :"));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
		
	}


}
