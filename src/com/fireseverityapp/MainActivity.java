package com.fireseverityapp;

import com.fireseverityapp.R;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	final int CAM_RESULT = 333333;

	//ErrorCode
	String camCanceled = "CAM_CANCELED";

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
		listenerOnSpinner();
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
		}
		if(resCode == Activity.RESULT_CANCELED){
			Intent errPage = new Intent(this, ErrorPage.class);
			errPage.putExtra("ErrorCode", camCanceled);
			this.startActivity(errPage);
		}
	}

}
