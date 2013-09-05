package com.fireseverityapp;

import com.fireseverityapp.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Button btnShow;
	GPSLocationCapture gpsLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnShow = (Button) findViewById(R.id.btnfindLocation);
		btnShow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gpsLocation = new GPSLocationCapture(MainActivity.this);
				
				if(gpsLocation.isLocationAvailable()){
					double latitude = gpsLocation.getLatitude();
                    double longitude = gpsLocation.getLongitude();
                    String provider = gpsLocation.getProvider();
                    
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude + "\nProvider:"+provider, Toast.LENGTH_LONG).show();  
				}else{
					gpsLocation.showSettingsAlert();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
