package com.vishnu.personalguardian.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class XMLreaderDistance {

	private String result;

	public String readURL(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				boolean isRowFound = false;
				boolean isStatusFound = false;
				boolean isDistanceFound = false;
				boolean isTextFound = false;
				try {
					XmlPullParserFactory factory = XmlPullParserFactory
							.newInstance();
					factory.setNamespaceAware(true);
					XmlPullParser xpp = factory.newPullParser();
					xpp.setInput(reader);
					int eventType = xpp.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						switch (eventType) {
						case XmlPullParser.START_TAG:
							// check whether got result or not
							if ("row".equalsIgnoreCase(xpp.getName())) {
								isRowFound = true;
							}
							if (isRowFound
									&& "status".equalsIgnoreCase(xpp.getName())) {
								isStatusFound = true;
							}

							// code to identify distance value
							if ("distance".equalsIgnoreCase(xpp.getName())) {
								isDistanceFound = true;
							}
							if (isDistanceFound
									&& "value".equalsIgnoreCase(xpp.getName())) {
								isTextFound = true;
							}
							break;
						case XmlPullParser.TEXT:
							// check whether got result or not
							if (isRowFound && isStatusFound) {
								result = xpp.getText();
								isRowFound = false;
								isStatusFound = false;
								if (result.equalsIgnoreCase("ZERO_RESULTS")) {
									//returns the string "ZERO_RESULTS" if no distance found
									return result;
								}
							}

							if (isDistanceFound && isTextFound) {
								result = xpp.getText();
								isDistanceFound = false;
								isTextFound = false;
								return result;
							}
							break;
						}
						eventType = xpp.next();
					}
				} catch (XmlPullParserException ex) {

				} catch (IOException ex) {
				}
			} else {
				// Log.e(MainActivity.class.toString(),
				// "Failed to download file");
			}
		} catch (ClientProtocolException e) {

		} catch (IOException e) {

		}

		return result;
	}
}
