package com.michi_log.Activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.michi_log.R;

public class RecieverActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    setContentView(R.layout.top);
	
	Intent intent = getIntent();
	showIntentData(intent);
	
	}
	
	@Override
	public void onNewIntent (Intent intent) {
		showIntentData(intent);
	}

	private void showIntentData(Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		Bundle bundle = intent.getExtras();
		
		Location location = (Location) bundle.get("location");
		
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
	}
    
    
}
