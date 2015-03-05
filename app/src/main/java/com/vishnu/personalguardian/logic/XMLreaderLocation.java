package com.vishnu.personalguardian.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

import com.vishnu.personalguardian.activity.Main;

import android.util.Log;

/**
 * @author Vishnuvathsasarma
 * 
 */
public class XMLreaderLocation {

	public List<MyLocation> readURL(String url) {
		List<MyLocation> list = new ArrayList<MyLocation>();
		List<String> addressComponents = new ArrayList<String>();
		boolean isAddressLineFound = false;
		boolean isLocationFound = false;
		boolean isLattitudeFound = false;
		boolean isLongitudeFound = false;
		double lattitude = 0.0D;
		double longitude = 0.0D;

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
							String tag = xpp.getName();
							if ("long_name".equals(tag)) {
								isAddressLineFound = true;
							}
							if ("location".equals(tag)) {
								isLocationFound = true;
							}
							if ("lat".equals(tag)) {
								isLattitudeFound = true;
							}
							if ("lng".equals(tag)) {
								isLongitudeFound = true;
							}
							break;
						case XmlPullParser.END_TAG:
							String tag2 = xpp.getName();
							if ("long_name".equals(tag2)) {
								isAddressLineFound = false;
							}
							if ("location".equals(tag2)) {
								isLocationFound = false;
							}
							if ("lat".equals(tag2)) {
								isLattitudeFound = false;
							}
							if ("lng".equals(tag2)) {
								isLongitudeFound = false;
							}
							if ("result".equals(tag2)) {
								StringBuilder builder = new StringBuilder();
								for (int i = 0; i < addressComponents.size(); i++) {
									builder.append(addressComponents.get(i));
									if (i != addressComponents.size() - 1) {
										builder.append("\n"); // Line breaker
									}
								}
								list.add(new MyLocation(builder.toString(),
										lattitude, longitude));
								//Log.i("#######", list.get(0).getAddress());
								addressComponents.clear();
							}
							break;
						case XmlPullParser.TEXT:
							if (isAddressLineFound) {
								String addressLine = xpp.getText();
								if (addressLine != null) {
									if (addressComponents.size() == 0) {
										addressComponents.add(addressLine);
									} else {
										if (!addressComponents.get(
												addressComponents.size() - 1)
												.equals(addressLine)) {
											addressComponents.add(addressLine);
										}
									}

								}
							} else if (isLocationFound) {
								if (isLattitudeFound) {
									String lat = xpp.getText();
									if (lat != null) {
										lattitude = Double.parseDouble(lat);
									}
								}
								if (isLongitudeFound) {
									String lng = xpp.getText();
									if (lng != null) {
										longitude = Double.parseDouble(lng);
									}
								}
							}
						}
						eventType = xpp.next();
					}
				} catch (XmlPullParserException ex) {

				} catch (IOException ex) {
				}
			} else {
				Log.e(Main.class.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {

		} catch (IOException e) {

		}

		return list;
	}
}
