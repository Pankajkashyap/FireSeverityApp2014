package com.fireseverityapp;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fireseverityapp.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
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
	static String FIRE_LEVEL_ATTRIBUTE = null;
	
	//private SharedPreferences mSettings;

	//ErrorCode
	String camCanceled = "CAM_CANCELED";
	
	TextView latitude;
	TextView longitude;
	Button btnShow;
	ImageView imgPhoto;
	GPSLocationCapture gpsLocation;
	Spinner spinnerFSA;
	Content emailContent = null;
	
	Context mContext = this;
	
	DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imgPhoto = (ImageView)this.findViewById(R.id.imageViewPhotoShot);
				
		btnShow = (Button) findViewById(R.id.btnfindLocation);
		btnShow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(emailContent != null){
					sendEmail();
				}else{
					Toast.makeText(mContext, "Data error, cannot send email!", Toast.LENGTH_SHORT).show();
				}
				
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
			Uri uri = data.getData();
			Log.e("uri", uri.toString());
			
			Bitmap pic = (Bitmap)data.getExtras().get("data");
			imgPhoto.setImageBitmap(pic);
			getLocation();
			listenerOnSpinner();
			//setEmailContent(data);
			
		}
		if(resCode == Activity.RESULT_CANCELED){
			Intent errPage = new Intent(this, ErrorPage.class);
			errPage.putExtra("ErrorCode", camCanceled);
			this.startActivity(errPage);
		}
	}
	
	
	@SuppressLint("SimpleDateFormat")
	public void setEmailContent(Intent data){
		
		Bitmap pic = (Bitmap)data.getExtras().get("data");
        
		// convert Bitmap into byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.JPEG, 100, stream );
        byte bytes[] = stream.toByteArray();
        
        // convert byte into base64
        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT); 
        
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmm");
        String dateStamp = sdf.format(new Date());
        
        emailContent.setImgBase64(base64);
        emailContent.setDateStamp(dateStamp);
        emailContent.setLatitude(latitude.getText().toString());
        emailContent.setLongitude(longitude.getText().toString());
        emailContent.setFireLevelAttitude(FIRE_LEVEL_ATTRIBUTE);
        
	}
	
	/*
	public void saveImageToDB(Intent data){
		db = new DatabaseHandler(this);
		
		Bitmap pic = (Bitmap)data.getExtras().get("data");
        
		// convert Bitmap into byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.JPEG, 100, stream );
        byte bytes[] = stream.toByteArray();
        
        // convert byte into base64
        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT); 
        
        ContentValues cv = new ContentValues();
        cv.put("imgBase64", base64);
		
        long longNumber = db.insert("EmailContent", "", cv);
	}

	*/
	
	public void sendEmail(){
		
		byte bytes[] = Base64.decode(emailContent.getImgBase64(), Base64.DEFAULT);
		Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		
		Intent mailIntent = new Intent(Intent.ACTION_SEND);
			mailIntent.setType("application/image");
			mailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"snowneco@gmail.com"});
			mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Fire Severity Level App");
			mailIntent.putExtra(Intent.EXTRA_TEXT   , "Bush Fire reporter, /n Latitude:"+ emailContent.getLatitude() + "/n Longitude:"+ emailContent.getLongitude());
			mailIntent.putExtra(Intent.EXTRA_STREAM, bytes);
		
		try {
		    startActivity(Intent.createChooser(mailIntent, "Choose an Email client :"));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
		
	}

}
