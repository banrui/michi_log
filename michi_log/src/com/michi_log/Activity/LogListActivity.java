package com.michi_log.Activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.michi_log.Adapter.LogListAdapter;
import com.michi_log.Dto.LogListDto;
import com.michi_log.Util.HttpClientUtil;
import com.michilog.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

/*
 * ���X�g�\���pActivity
 * author by Rui Bando 
 */
public class LogListActivity extends ListActivity{
    
	LogListAdapter logListAdapter = null;  
	ArrayList<LogListDto> logList = null;
	ByteArrayOutputStream byteArrayOutputStream;
	 
	@SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.log_list);
    	
      //-----[POST���M����f�[�^���i�[]
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
//      --�p�����[�^--
        //nameValuePair.add(new BasicNameValuePair("test", tes));

        //-----[�񓯊���API�����s��JSON���擾]
        BackgroundTask task = new BackgroundTask(this,"�f�[�^������","���΂炭���҂���������");
        task.execute(nameValuePair);
        
		final ListView lv = getListView();
	    lv.setTextFilterEnabled(true);
	    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
            	ListView listView = (ListView) parent;
            	LogListDto logListDto = (LogListDto) listView.getItemAtPosition(position);
            	String logName = logListDto.getLogName().toString();

            	Intent logDetailIntent = new Intent(LogListActivity.this, LogDetailActivity.class);
            	//logDetailIntent.putExtra("test", logName);
                startActivity(logDetailIntent);
                }
        });
    }
	
	/*
	 * �񓯊�����
	 * */
	public class BackgroundTask extends AsyncTask<List<NameValuePair>, Void, String>{
		private Context context = null;
		private ProgressDialog dialog = null;
		private String title;
		private String msg;
		//�R���X�g���N�^
		public BackgroundTask(Context context,String title,String msg)
		{
			this.context = context;
			this.title = title;
			this.msg = msg;
		}
		//�������s�O���\�b�h
		protected void onPreExecute()
		{
			//�v���O���X�_�C�A���O�ݒ�
			dialog = new ProgressDialog(context);
			dialog.setIndeterminate(true);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setTitle(title);
			dialog.setMessage(msg);
			dialog.show();
		} 
		
    	@Override
		protected String doInBackground(List<NameValuePair>... params) {
    		List<NameValuePair> nameValuePair = params[0];
    		try {
    		    byteArrayOutputStream = new ByteArrayOutputStream();

    			//-----[POST���M]
    		    HttpClientUtil httpClientUtil = new HttpClientUtil();
    		    HttpResponse response = httpClientUtil.httpPostExecute("URLURLURL", nameValuePair);
    	                    
    	        //-----[�T�[�o�[����̉������擾]
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
			return byteArrayOutputStream.toString();
		}

    	@Override
    	protected void onPostExecute(String result) {
    		String jsonData = result;
    		logList = new ArrayList<LogListDto>();   	
    		//JSON����擾�����f�[�^���l�߂� & SQLite�ɋl�߂�
    		setLogListData(jsonData, logList);
    		if (logList.isEmpty() == true || logList == null) {
            	LogListDto noLogListDto = new LogListDto();
            	noLogListDto.setLogName("�f�[�^��������܂���");
            	logList.add(noLogListDto);
            }   		
            logListAdapter = new LogListAdapter(LogListActivity.this, R.layout.log_list_row, logList);
            setListAdapter(logListAdapter);
          //�v���O���X�_�C�A���O����
    		dialog.dismiss();
    	}
    
    }
	
	/*
	 * company_list�e�[�u���ɐV�����f�[�^��ǉ�
	 * setCompanyListData
	 * @param
	 * String jsonData
	 * ArrayList<CompanyListDto> companyList
	 * */
	public ArrayList<LogListDto> setLogListData(String jsonData, ArrayList<LogListDto> companyList) {
		try {
			JSONObject rootObject = new JSONObject(jsonData);
			int jsonResult = Integer.parseInt(rootObject.get("status").toString());
			if(jsonResult == 1){
			
				String jsonString = rootObject.get("unit_list").toString();
				JSONArray jArray = new JSONArray(jsonString);
				for(int i = 0; i < jArray.length(); i++){

					String testtest = jArray.getJSONObject(i).getString("id").toString();
					
					//ListDto�ɋl�߂�
					LogListDto LogListDto = new LogListDto();
					LogListDto.setLogName(testtest);

					//ArrayList�ɋl�߂�
					companyList.add(LogListDto);
				}
			}
        } catch (JSONException e) {
			e.printStackTrace();
		} 
        	return companyList;
	}
	

	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}

    
}
	
	
