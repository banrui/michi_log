package com.michi_log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;

import com.michi_log.Activity.HistoryActivity;
import com.michi_log.Activity.HomeActivity;
import com.michi_log.Activity.LogListActivity;
import com.michi_log.Activity.SearchActivity;
import com.michi_log.Activity.SettingActivity;
import com.michi_log.Util.HttpClientUtil;
import com.michi_log.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;


public class MainActivity extends Activity {
	
	boolean stopFlg = false;
	ByteArrayOutputStream byteArrayOutputStream;

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
					//-----[POST送信するデータを格納]
			        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			        //--パラメータ↓入れる！！--
			        //nameValuePair.add(new BasicNameValuePair("test", tes));
			        
			        try {
		    		    byteArrayOutputStream = new ByteArrayOutputStream();

		    			//-----[POST送信]
		    		    HttpClientUtil httpClientUtil = new HttpClientUtil();
		    		    HttpResponse response = httpClientUtil.httpPostExecute("URLURLURL", nameValuePair);
		    	                    
		    	        //-----[サーバーからの応答を取得]
		    	        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
		    	        	response.getEntity().writeTo(byteArrayOutputStream);
		    	        } else {
		    	        	//Toast.makeText(this, "[error]: "+response.getStatusLine(), Toast.LENGTH_LONG).show();
		    	        }
		    	    } catch (UnsupportedEncodingException e) {
		    	            e.printStackTrace();
		    	    } catch (IOException e) {
		    	            e.printStackTrace();
		    	    }
			        //sleep
			        
			        String getXML = byteArrayOutputStream.toString();
			        
			        
				}
				Intent logListIntent = new Intent(MainActivity.this, LogListActivity.class);
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
