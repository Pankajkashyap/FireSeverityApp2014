package com.fireseverityapp;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class SpinnerOnItemSelectedListener implements OnItemSelectedListener{

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String attitude = parent.getItemAtPosition(pos).toString();
		Toast.makeText(parent.getContext(), "Selected Attribute:"+ attitude, Toast.LENGTH_SHORT).show();
		MainActivity.FIRE_LEVEL_ATTRIBUTE = attitude;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

}
