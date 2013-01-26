package com.michi_log.Util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class XMLParser {

	final static String HIT = "hit_per_page";
	final static String NAME = "name";
	final static String LAT = "latitude";
	final static String LON = "longitude";
	final static String CAT = "category";
	final static String ADDR = "address";
	final static String TEL = "tel";
	final static String OPEN = "opentime";
	final static String HOLIDAY = "holiday";
	final static String ACCESS = "access";
	final static String IMG = "shop_image1";

	public List<HashMap<String, String>> xmlParserFromString(String str) {
		XmlPullParser xmlPullParser = Xml.newPullParser();

		try {
			xmlPullParser.setInput(new StringReader(str));
		} catch (XmlPullParserException e) {
			Log.d("XmlPullParserSample", "Error");
		}

		List<String> restaurantList = new ArrayList<String>();
		List<String> latList = new ArrayList<String>();
		List<String> lonList = new ArrayList<String>();
		List<String> catList = new ArrayList<String>();
		List<String> addrList = new ArrayList<String>();
		List<String> imgList = new ArrayList<String>();
		String title = null;
		try {
			int eventType;
			eventType = xmlPullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					Log.d("XmlPullParserSample", "Start document");
				} else if (eventType == XmlPullParser.END_DOCUMENT) {
					Log.d("XmlPullParserSample", "End document");
				} else if (eventType == XmlPullParser.START_TAG) {
					if (xmlPullParser.getName().equals(NAME)) {
						title = NAME;
					} else if (xmlPullParser.getName().equals(LAT)) {
						title = LAT;
					} else if (xmlPullParser.getName().equals(LON)) {
						title = LON;
					} else if (xmlPullParser.getName().equals(CAT)) {
						title = CAT;
					} else if (xmlPullParser.getName().equals(ADDR)) {
						title = ADDR;
					} else if (xmlPullParser.getName().equals(IMG)) {
						title = IMG;
					}
					Log.d("XmlPullParserSample",
							"Start tag " + xmlPullParser.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					title = null;
					Log.d("XmlPullParserSample",
							"End tag " + xmlPullParser.getName());
				} else if (eventType == XmlPullParser.TEXT) {
					Log.d("XmlPullParserSample",
							"Text " + xmlPullParser.getText());
					if (title != null) {
						if (title.equals(NAME)) {
							restaurantList.add(xmlPullParser.getText());
						} else if (title.equals(LAT)) {
							latList.add(xmlPullParser.getText());
						} else if (title.equals(LON)) {
							lonList.add(xmlPullParser.getText());
						} else if (title.equals(CAT)) {
							catList.add(xmlPullParser.getText());
						} else if (title.equals(ADDR)) {
							addrList.add(xmlPullParser.getText());
						} else if (title.equals(IMG)) {
							imgList.add(xmlPullParser.getText());
						}
					}
				}
				eventType = xmlPullParser.next();
			}
		} catch (Exception e) {
			Log.d("XmlPullParserSample", "Error");
		}

		List<HashMap<String, String>> res = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < restaurantList.size(); i++) {
			HashMap<String, String> restaurantMap = new HashMap<String, String>();
			restaurantMap.put(NAME, restaurantList.get(i));
			restaurantMap.put(LAT, latList.get(i));
			restaurantMap.put(LON, lonList.get(i));
			restaurantMap.put(CAT, catList.get(i));
			restaurantMap.put(ADDR, addrList.get(i));
			restaurantMap.put(IMG, imgList.get(i));
			res.add(restaurantMap);
		}
		return res;

		// これで中身のデータを舐める事ができる
		// for (int i = 0; i < res.size(); i++) {
		// Iterator<String> itr = res.get(i).keySet().iterator();
		// while (itr.hasNext()) {
		// Object obj = itr.next();
		// Log.d("++++++ Response ++++++", res.get(i).get(obj));
		// }
		// }

	}
}
