package com.michi_log;

import java.util.HashMap;
import java.util.List;

import com.evernote.client.oauth.android.EvernoteSession;
import com.michi_log.Activity.HistoryActivity;
import com.michi_log.Activity.HomeActivity;
import com.michi_log.Activity.LogListActivity;
import com.michi_log.Activity.MainLoadActivity;
import com.michi_log.Activity.SearchActivity;
import com.michi_log.Activity.SettingActivity;
import com.michi_log.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {
	List<HashMap<String, String>> result;

	boolean stopFlg = false;
	String lati;
	String longi;

	private static final String CONSUMER_KEY = "ruibando-9832";
	private static final String CONSUMER_SECRET = "6f2ca756d285821e";
	private static final String EVERNOTE_HOST = EvernoteSession.HOST_SANDBOX;
	EvernoteSession mEvernoteSession;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top);

		mEvernoteSession = EvernoteSession.init(this, CONSUMER_KEY,
				CONSUMER_SECRET, EVERNOTE_HOST, null);

		ImageView michiLoadButton = (ImageView) findViewById(R.id.michi_load);
		michiLoadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mainLoadActivity = new Intent(MainActivity.this,
						MainLoadActivity.class);
				startActivity(mainLoadActivity);

			}
		});

		// 各項目
		ImageView homeButton = (ImageView) findViewById(R.id.home);
		homeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent homeIntent = new Intent(MainActivity.this,
						HomeActivity.class);
				startActivity(homeIntent);
				finish();
			}
		});

		ImageView histroyButton = (ImageView) findViewById(R.id.history);
		histroyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent historyIntent = new Intent(MainActivity.this,
						LogListActivity.class);
				startActivity(historyIntent);
				finish();
			}
		});

		ImageView searchButton = (ImageView) findViewById(R.id.search);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent searchIntent = new Intent(MainActivity.this,
						SearchActivity.class);
				startActivity(searchIntent);
				finish();
			}
		});

		ImageView settingButton = (ImageView) findViewById(R.id.setting);
		settingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent settingIntent = new Intent(MainActivity.this,
				// SettingActivity.class);
				// startActivity(settingIntent);
				startAuth(v);
				finish();

			}
		});

	}

	// Authenticate
	public void startAuth(View v) {
		if (!mEvernoteSession.isLoggedIn()) {
			mEvernoteSession.authenticate(this);
		} else {
			mEvernoteSession.logOut(getApplicationContext());
		}
	}

}