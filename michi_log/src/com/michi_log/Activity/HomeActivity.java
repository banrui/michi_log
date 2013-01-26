package com.michi_log.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.michi_log.MainActivity;
import com.michi_log.R;

public class HomeActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top);
		Intent homeIntent = new Intent(HomeActivity.this, MainActivity.class);
		startActivity(homeIntent);
	}

}
