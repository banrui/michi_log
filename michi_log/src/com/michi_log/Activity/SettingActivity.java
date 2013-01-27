package com.michi_log.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.michi_log.R;

public class SettingActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		// 各項目
		ImageView homeButton = (ImageView) findViewById(R.id.home);
		homeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent homeIntent = new Intent(SettingActivity.this, HomeActivity.class);
				startActivity(homeIntent);
			}
		});

		ImageView histroyButton = (ImageView) findViewById(R.id.history);
		histroyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent historyIntent = new Intent(SettingActivity.this, HistoryActivity.class);
				startActivity(historyIntent);
			}
		});

		ImageView searchButton = (ImageView) findViewById(R.id.search);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent searchIntent = new Intent(SettingActivity.this, SearchActivity.class);
				startActivity(searchIntent);
			}
		});

		ImageView settingButton = (ImageView) findViewById(R.id.setting);
		settingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent settingIntent = new Intent(SettingActivity.this,
				// SettingActivity.class);
				// startActivity(settingIntent);

			}
		});
	}

}
