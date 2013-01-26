package com.michi_log.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.michi_log.Activity.ActiveMapActivity;
import com.michi_log.Util.XMLParser;
import com.michi_log.R;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

public class MainLoadActivity extends Activity implements LocationListener {
	List<HashMap<String, String>> result;

	boolean stopFlg = false;
	private LocationManager manager = null;
	String lati;
	String longi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top);

		// -----[非同期でAPIを実行しJSONを取得]
		BackgroundTask task = new BackgroundTask(this, "周辺情報取得中", "戻るボタンで終了");
		task.execute(null);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (manager != null) {
			manager.removeUpdates(this);
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (manager != null) {
			manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					this);
			// manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
			// 0, 0, this);
		}
		super.onResume();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		lati = "緯度：" + location.getLatitude();
		longi = "経度：" + location.getLongitude();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		switch (status) {
		case LocationProvider.AVAILABLE:
			break;
		case LocationProvider.OUT_OF_SERVICE:
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			break;
		}
	}

	// ///////
	/*
	 * 非同期処理
	 */
	public class BackgroundTask extends
			AsyncTask<List<NameValuePair>, Void, List<HashMap<String, String>>> {
		private Context context = null;
		private ProgressDialog dialog = null;
		private String title;
		private String msg;

		// コンストラクタ
		public BackgroundTask(Context context, String title, String msg) {
			this.context = context;
			this.title = title;
			this.msg = msg;
		}

		// 処理実行前メソッド
		protected void onPreExecute() {
			// プログレスダイアログ設定
			dialog = new ProgressDialog(context);
			dialog.setIndeterminate(true);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setTitle(title);
			dialog.setMessage(msg);
			dialog.show();
		}

		@Override
		protected List<HashMap<String, String>> doInBackground(
				List<NameValuePair>... parama) {
			/**
			 * ボタンが押されてSTOPが押されるまでは、While文で一定時間毎にAPIに接続し、データをEvernoteに格納
			 * ボタンを接続中に変更する
			 * */

			while (!stopFlg) {

				// GPS情報取得
				manager = (LocationManager) getSystemService(LOCATION_SERVICE);

				// ぐるなび
				HttpClient objHttp = new DefaultHttpClient();
				HttpParams params = objHttp.getParams();
				HttpConnectionParams.setConnectionTimeout(params, 1000); // 接続のタイムアウト
				HttpConnectionParams.setSoTimeout(params, 1000); // データ取得のタイムアウト
				String sReturn = "";
				try {
					HttpGet objGet = new HttpGet(
							"http://api.gnavi.co.jp/ver1/RestSearchAPI/?keyid=f739e5c65b1d237d97a90e757e177ee5&area=AREA110&pref=PREF13&id=g144600&sort=1");
					HttpResponse objResponse = objHttp.execute(objGet);
					if (objResponse.getStatusLine().getStatusCode() < 400) {
						InputStream objStream = objResponse.getEntity()
								.getContent();
						InputStreamReader objReader = new InputStreamReader(
								objStream);
						BufferedReader objBuf = new BufferedReader(objReader);
						StringBuilder objJson = new StringBuilder();
						String sLine;
						while ((sLine = objBuf.readLine()) != null) {
							objJson.append(sLine);
						}
						sReturn = objJson.toString();
						objStream.close();
						XMLParser xmlParser = new XMLParser();
						result = xmlParser.xmlParserFromString(sReturn);

					}
				} catch (IOException e) {
				}
				// sleep
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

				stopFlg = true;

			}
			return result;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {
			// プログレスダイアログ消去
			dialog.dismiss();
			Intent logListIntent = new Intent(MainLoadActivity.this,
					ActiveMapActivity.class);
			startActivity(logListIntent);
			finish();

		}

	}

}