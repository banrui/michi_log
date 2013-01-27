package com.michi_log.Activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import com.michi_log.Adapter.LogListAdapter;
import com.michi_log.Dto.LogListDto;
import com.michi_log.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

/*
 * リスト表示用Activity
 * author by Rui Bando 
 */
public class LogListActivity extends ListActivity {

	LogListAdapter logListAdapter = null;
	ArrayList<LogListDto> logList = null;
	ByteArrayOutputStream byteArrayOutputStream;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.log_list);

		// -----[POST送信するデータを格納]
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
		// --パラメータ↓d入れる！！--
		// nameValuePair.add(new BasicNameValuePair("test", tes));

		// -----[非同期でAPIを実行しJSONを取得]
		BackgroundTask task = new BackgroundTask(this, "データ処理中", "しばらくお待ち下さい♪");
		task.execute(nameValuePair);

		final ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView listView = (ListView) parent;
				LogListDto logListDto = (LogListDto) listView.getItemAtPosition(position);
				String logName = logListDto.getLogName().toString();

				Intent logDetailIntent = new Intent(LogListActivity.this, ActiveMapActivity.class);
				startActivity(logDetailIntent);
			}
		});
	}

	/*
	 * 非同期処理
	 */
	public class BackgroundTask extends
			AsyncTask<List<NameValuePair>, Void, String> {
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
		protected String doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePair = params[0];
			// try {
			// byteArrayOutputStream = new ByteArrayOutputStream();
			//
			// //-----[POST送信]
			// HttpClientUtil httpClientUtil = new HttpClientUtil();
			// HttpResponse response =
			// httpClientUtil.httpPostExecute("URLURLURL", nameValuePair);
			//
			// //-----[サーバーからの応答を取得]
			// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			// {
			// response.getEntity().writeTo(byteArrayOutputStream);
			// } else {
			// //Toast.makeText(this, "[error]: "+response.getStatusLine(),
			// Toast.LENGTH_LONG).show();
			// }
			// } catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// return byteArrayOutputStream.toString();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// String jsonData = result;
			logList = new ArrayList<LogListDto>();
			// JSONから取得したデータを詰める & SQLiteに詰める
			// setLogListData(jsonData, logList);
			setLogListData(null, logList);
			if (logList.isEmpty() == true || logList == null) {
				LogListDto noLogListDto = new LogListDto();
				noLogListDto.setLogName("データが見つかりません");
				logList.add(noLogListDto);
			}
			logListAdapter = new LogListAdapter(LogListActivity.this, R.layout.log_list_row, logList);
			setListAdapter(logListAdapter);
			// プログレスダイアログ消去
			dialog.dismiss();
		}

	}

	/*
	 * company_listテーブルに新しいデータを追加 setCompanyListData
	 * 
	 * @param String jsonData ArrayList<CompanyListDto> companyList
	 */
	public ArrayList<LogListDto> setLogListData(String jsonData, ArrayList<LogListDto> companyList) {
		// try {
		// JSONObject rootObject = new JSONObject(jsonData);
		// int jsonResult =
		// Integer.parseInt(rootObject.get("status").toString());
		// if(jsonResult == 1){
		//
		// String jsonString = rootObject.get("unit_list").toString();
		// JSONArray jArray = new JSONArray(jsonString);
		// for(int i = 0; i < jArray.length(); i++){
		//
		// String testtest = jArray.getJSONObject(i).getString("id").toString();
		String[] map = { "学芸大学〜渋谷:2013/01/26", "渋谷〜銀座:2013/01/26",
				"銀座〜渋谷:2013/01/26", "渋谷〜学芸大学:2013/01/27", "学芸大学〜銀座:2013/01/27" };
		for (int i = 0; i < 5; i++) {
			// ListDtoに詰める
			LogListDto LogListDto = new LogListDto();
			LogListDto.setLogName(map[i]);

			// ArrayListに詰める
			companyList.add(LogListDto);
		}

		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		return companyList;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
