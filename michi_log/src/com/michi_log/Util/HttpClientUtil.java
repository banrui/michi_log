package com.michi_log.Util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientUtil {
	//-----[クライアント設定]
	HttpClient httpclient;
	HttpPost httppost;
	HttpResponse response;
	
	public HttpResponse httpPostExecute(String createCompanyUrl, List<NameValuePair> nameValuePair){
		httpclient = new DefaultHttpClient();
        httppost = new HttpPost(createCompanyUrl);

        try {
        	//-----[POST送信]
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			response = httpclient.execute(new HttpHost("recruit.bridgesnote.com", 443, "https"),httppost);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

}
