package com.michi_log.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.evernote.client.oauth.android.EvernoteSession;
import com.evernote.client.oauth.android.EvernoteUtil;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.thrift.TException;
import com.evernote.thrift.transport.TTransportException;
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

	private static final String CONSUMER_KEY = "ruibando-9832";
	private static final String CONSUMER_SECRET = "6f2ca756d285821e";
	private static final String EVERNOTE_HOST = EvernoteSession.HOST_SANDBOX;
	EvernoteSession mEvernoteSession;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top);
		mEvernoteSession = EvernoteSession.init(this, CONSUMER_KEY, CONSUMER_SECRET, EVERNOTE_HOST, null);
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
			manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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
		protected List<HashMap<String, String>> doInBackground(List<NameValuePair>... parama) {
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
					HttpGet objGet = new HttpGet("http://api.gnavi.co.jp/ver1/RestSearchAPI/?keyid=f739e5c65b1d237d97a90e757e177ee5&area=AREA110&pref=PREF13&id=g144600&sort=1");
					HttpResponse objResponse = objHttp.execute(objGet);
					if (objResponse.getStatusLine().getStatusCode() < 400) {
						InputStream objStream = objResponse.getEntity().getContent();
						InputStreamReader objReader = new InputStreamReader(objStream);
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
				postShopInfoToEvernote(result);
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
			Intent logListIntent = new Intent(MainLoadActivity.this, ActiveMapActivity.class);
			startActivity(logListIntent);
			finish();

		}

	}

	// post to Evernote
	public void postShopInfoToEvernote(List<HashMap<String, String>> res) {
		for (int i = 0; i < res.size(); i++) {
			Iterator<String> itr = res.get(i).keySet().iterator();
			String title = new String();
			String lat = new String();
			String lon = new String();
			String url = new String();
			String address = new String();
			String prS = new String();
			String tel = new String();
			String open = new String();
			String cou = new String();
			while (itr.hasNext()) {
				Object obj = itr.next();
				// Log.d("++++++ Response ++++++", res.get(i).get(obj));
				if (obj.toString().equals("name")) {
					title = res.get(i).get(obj);
				} else if (obj.toString().equals("latitude")) {
					lat = res.get(i).get(obj);
				} else if (obj.toString().equals("longitude")) {
					lon = res.get(i).get(obj);
				} else if (obj.toString().equals("url")) {
					url = res.get(i).get(obj);
				} else if (obj.toString().equals("address")) {
					address = res.get(i).get(obj);
				} else if (obj.toString().equals("pr_short")) {
					prS = res.get(i).get(obj);
				} else if (obj.toString().equals("tel")) {
					tel = res.get(i).get(obj);
				} else if (obj.toString().equals("opentime")) {
					open = res.get(i).get(obj);
				} else if (obj.toString().equals("pc_coupon")) {
					cou = res.get(i).get(obj);
				}
			}
			// Log.d("++++++", title + ", lat: " + lat + ", lon: " + lon);

			// Create Note
			try {
				Note note = new Note();
				note.setTitle(title);
				// note.setContent("test created test note");
				List<String> tags = new ArrayList<String>();
				tags.add("michi_log");
				if (cou.equals("1")) {
					tags.add("お得");
				}
				note.setTagNames(tags);
				String content = EvernoteUtil.NOTE_PREFIX + "<p>" + prS
						+ "</p>" + "<a href='" + url + "'>" + address + "</a>"
						// + "<p>" + open + "</p>"
						// + "<p>" + tel + "</p>"
						+ EvernoteUtil.NOTE_SUFFIX;
				note.setContent(content);
				NoteAttributes attribute = new NoteAttributes();
				attribute.setLatitude(Double.parseDouble(lat));
				attribute.setLongitude(Double.parseDouble(lon));

				Note createdNote = mEvernoteSession.createNoteStore().createNote(mEvernoteSession.getAuthToken(), note);
			} catch (TTransportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EDAMUserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EDAMSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EDAMNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				Thread.sleep(60000); // 3000ミリ秒Sleepする
			} catch (InterruptedException e) {
			}
		}

	}

	// Callback for authenticate
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("+++ callback +++", "authentication callback");
		switch (requestCode) {
		// Update UI when oauth activity returns result
		case EvernoteSession.REQUEST_CODE_OAUTH:
			if (resultCode == Activity.RESULT_OK) {
				// Authentication was successful, do what you need to do in your
				// app

				// get Note
				// try {
				// String guid = new String();
				// List<com.evernote.edam.type.Tag> tags =
				// mEvernoteSession.createNoteStore().listTags(mEvernoteSession.getAuthToken());
				// for (int i = 0; i < tags.size(); i++) {
				// Log.d("+++tags+++", tags.get(i).getGuid() + ":  " +
				// tags.get(i).getName());
				// if (tags.get(i).getName().equals("michi_log")) {
				// guid = tags.get(i).getGuid();
				// }
				// }
				//
				//
				// int maxNotes = 10;
				//
				// NoteFilter filter = new NoteFilter();
				// List<String> tagsList = new ArrayList<String>();
				// tagsList.add(guid);
				// filter.setTagGuids(tagsList);
				//
				// // NotesMetadataResultSpec spec = new
				// NotesMetadataResultSpec();
				// // spec.setIncludeTitle(true);
				// EvernoteSession session = EvernoteSession.init(this,
				// CONSUMER_KEY,
				// CONSUMER_SECRET, EVERNOTE_HOST, null);
				// // NoteList notelist =
				// mEvernoteSession.createNoteStore().findNotes(mEvernoteSession.getAuthToken(),
				// // filter, 0, maxNotes);
				// NoteList notelist =
				// session.createNoteStore().findNotes(mEvernoteSession.getAuthToken(),
				// filter, 0, maxNotes);
				// List<Note> notes = notelist.getNotes();
				// Log.d("+++ Num +++", String.valueOf(notes.size()));
				// for (int i = 0; i < notes.size(); i++) {
				// Log.d("+++ content +++", notes.get(i).getTitle());
				// }
				//
				// } catch (TTransportException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (EDAMUserException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (EDAMSystemException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (TException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (EDAMNotFoundException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				// Create Note
				// try {
				// Note note = new Note();
				// note.setTitle("test from android ver 2.0");
				// // note.setContent("test created test note");
				// List<String> tags = new ArrayList<String>();
				// tags.add("michi_log");
				// note.setTagNames(tags);
				// String content = EvernoteUtil.NOTE_PREFIX
				// + "<p>this is the second test content</p>"
				// + EvernoteUtil.NOTE_SUFFIX;
				// note.setContent(content);
				// NoteAttributes attribute = new NoteAttributes();
				// String lat = "35.657547";
				// String lon = "139.704895";
				// attribute.setLatitude(Double.parseDouble(lat));
				// attribute.setLongitude(Double.parseDouble(lon));
				//
				// Note createdNote = mEvernoteSession.createNoteStore()
				// .createNote(mEvernoteSession.getAuthToken(), note);
				// } catch (TTransportException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (EDAMUserException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (EDAMSystemException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (EDAMNotFoundException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (TException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
			break;
		}
	}

	// public void chooseNoteBook() {
	// try {
	// final List<Notebook> notebooks =
	// getNoteStore().listNotebooks(getAuthToken());
	// String[] noteNames = new String[notebooks.size()];
	// for (int i = 0; i < notebooks.size(); ++i) {
	// Notebook notebook = notebooks.get(i);
	// noteNames[i] = notebook.getName();
	// }
	// new
	// AlertDialog.Builder(this).setTitle("ノートを選択してください").setItems(noteNames,
	// new OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// Notebook selectedNotebook = notebooks.get(which);
	// setTargetNotebook(selectedNotebook);
	// }
	// });
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

}