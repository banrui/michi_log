package com.michi_log.Activity;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

import com.michi_log.MainActivity;
import com.michi_log.R;
import com.michi_log.Util.WebViewClient;

public class ActiveMapActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activemap);

		// WebView webView = (WebView)findViewById(R.id.map_view);
		// webView.setWebViewClient(new WebViewClient());
		// webView.loadUrl("https://maps.google.co.jp/maps/ms?msid=212175728393895714573.0004d43554e62f7cc427a&msa=0&ll=35.662736,139.730301&spn=0.088562,0.15501");

		// 各項目
		ImageView homeButton = (ImageView) findViewById(R.id.home);
		homeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent homeIntent = new Intent(ActiveMapActivity.this,
						HomeActivity.class);
				startActivity(homeIntent);
				finish();
			}
		});

		ImageView histroyButton = (ImageView) findViewById(R.id.history);
		histroyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent historyIntent = new Intent(ActiveMapActivity.this,
						LogListActivity.class);
				startActivity(historyIntent);
				finish();
			}
		});

		ImageView searchButton = (ImageView) findViewById(R.id.search);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent searchIntent = new Intent(ActiveMapActivity.this,
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
				// startAuth(v);
				finish();

			}
		});

	}

}
