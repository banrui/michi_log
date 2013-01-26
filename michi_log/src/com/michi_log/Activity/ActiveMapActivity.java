package com.michi_log.Activity;

import android.app.Activity;

import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

import com.michi_log.R;
import com.michi_log.Util.WebViewClient;

public class ActiveMapActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activemap);
        
//        WebView webView = (WebView)findViewById(R.id.map_view);
//		webView.setWebViewClient(new WebViewClient());
//		webView.loadUrl("https://maps.google.co.jp/maps/ms?msid=212175728393895714573.0004d43554e62f7cc427a&msa=0&ll=35.662736,139.730301&spn=0.088562,0.15501");

	
	}

}
