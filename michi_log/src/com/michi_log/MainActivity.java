package com.michi_log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.michi_log.Activity.ActiveMapActivity;
import com.michi_log.Activity.HistoryActivity;
import com.michi_log.Activity.HomeActivity;
import com.michi_log.Activity.LogListActivity;
import com.michi_log.Activity.RecieverActivity;
import com.michi_log.Activity.SearchActivity;
import com.michi_log.Activity.SettingActivity;
import com.michi_log.Util.HttpClientUtil;
import com.michi_log.R;

import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;


public class MainActivity extends Activity {
	
	boolean stopFlg = false;
	 private LocationManager mLocationManager;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top);
        Log.d("test1", "test1");
        Button michiLoadButton = (Button) findViewById(R.id.michi_load);
        michiLoadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			Log.d("test", "test");
				/**
				 * ボタンが押されてSTOPが押されるまでは、While文で一定時間毎にAPIに接続し、データをEvernoteに格納
				 * ボタンを接続中に変更する
				 * */
			
				
				
				while (!stopFlg) {
					LocationManager loc;
					PendingIntent pendingIntent;
					
					
					
					//位置情報を取得
//					Intent geoIntent = new Intent (MainActivity.this, RecieverActivity.class);
//					PendingIntent geoPendingIntent = PendingIntent.getActivity(MainActivity.this, 0, geoIntent, 0);
//
//					Criteria criteria = new Criteria();
//					criteria.setAccuracy(Criteria.ACCURACY_FINE);
//					
//					loc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//					loc.requestSingleUpdate(criteria, geoPendingIntent);
					
					//mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
					
					//ぐるなび
					HttpClient objHttp = new DefaultHttpClient();  
			        HttpParams params = objHttp.getParams();  
			        HttpConnectionParams.setConnectionTimeout(params, 1000); //接続のタイムアウト  
			        HttpConnectionParams.setSoTimeout(params, 1000); //データ取得のタイムアウト  
			        String sReturn = "";  
			        try {  
			        	HttpGet objGet   = new HttpGet("http://api.gnavi.co.jp/ver1/RestSearchAPI/?keyid=f739e5c65b1d237d97a90e757e177ee5&area=AREA110&pref=PREF13&id=g144600&sort=1");  
			        	HttpResponse objResponse = objHttp.execute(objGet);  
			        	        if (objResponse.getStatusLine().getStatusCode() < 400){  
			        	            InputStream objStream = objResponse.getEntity().getContent();  
			        	            InputStreamReader objReader = new InputStreamReader(objStream);  
			        	            BufferedReader objBuf = new BufferedReader(objReader);  
			        	            StringBuilder objJson = new StringBuilder();  
			        	            String sLine;  
			        	            while((sLine = objBuf.readLine()) != null){  
			        	                objJson.append(sLine);  
			        	            }  
			        	            sReturn = objJson.toString();  
			        	            objStream.close();  
			        	        }  
			        	    } catch (IOException e) {  
			        	    }     
			        //sleep			
			        	    Log.d("test", sReturn.toString());
			        	    
			        	    stopFlg = true;
				}
				Intent logListIntent = new Intent(MainActivity.this, ActiveMapActivity.class);
                startActivity(logListIntent);

			}
		});
        
//        Button michiLoadStopButton = (Button) findViewById(R.id.michi_load);
//        michiLoadStopButton.setOnClickListener(new OnClickListener() {
//        	@Override
//			public void onClick(View v) {
//        		stopFlg = true;
//			}
//        });
        
        //各項目
        Button homeButton = (Button) findViewById(R.id.home);
        homeButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
        		startActivity(homeIntent);
        	}
        });
        
        Button histroyButton = (Button) findViewById(R.id.history);
        histroyButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
        		startActivity(historyIntent);
        	}
        });
        
        Button searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
        		startActivity(searchIntent);
        	}
        });
        
        Button settingButton = (Button) findViewById(R.id.setting);
        settingButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
        		startActivity(settingIntent);
        	}
        });
        	
    }
}