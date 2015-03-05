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

import android.util.Log;

public class XMLreaderWeather {
	public Weather readURL(String url) {
		Weather weather = new Weather();
		boolean isCityFound = false;
		boolean isSunFound = false;
		boolean isTempratureFound = false;
		boolean isHumidityFound = false;
		boolean isPressureFound = false;
		boolean isWindFound = false;
		boolean isSpeedFound = false;
		boolean isDirectionFound = false;
		boolean isCloudsFound = false;
		boolean isPrecipitationFound = false;
		boolean isWeatherFound = false;
		boolean isLastUpdateFound = false;

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
					int count = 0;
					while (eventType != XmlPullParser.END_DOCUMENT) {
						switch (eventType) {
						case XmlPullParser.START_TAG:
							String tag = xpp.getName();
							if ("city".equals(tag)) {
								isCityFound = true;
							}
							if ("sun".equals(tag) && isCityFound) {
								isSunFound = true;
							}
							if ("temperature".equals(tag)) {
								isTempratureFound = true;
							}
							if ("humidity".equals(tag)) {
								isHumidityFound = true;
							}
							if ("pressure".equals(tag)) {
								isPressureFound = true;
							}
							if ("wind".equals(tag)) {
								isWindFound = true;
							}
							if ("speed".equals(tag) && isWindFound) {
								isSpeedFound = true;
							}
							if ("direction".equals(tag) && isWindFound) {
								isDirectionFound = true;
							}
							if ("clouds".equals(tag)) {
								isCloudsFound = true;
							}
							if ("precipitation".equals(tag)) {
								isPrecipitationFound = true;
							}
							if ("weather".equals(tag)) {
								isWeatherFound = true;
							}
							if ("lastupdate".equals(tag)) {
								isLastUpdateFound = true;
							}

							if (isCityFound) {
								String city = xpp.getAttributeValue(null, "name");
								
								if (city != null) {
									weather.setWeatherStation(city);
									Log.i("City", city);
									Log.i("CountCity", count + "");
								}
							}
							if(isSunFound) {
								String rise = xpp.getAttributeValue(null, "rise");
								String set = xpp.getAttributeValue(null, "set");
								
								if(rise != null) {
									weather.setSunRise(rise);
								}
								if(set != null) {
									weather.setSunSet(set);
								}
							}
							if(isTempratureFound) {
								String temp = xpp.getAttributeValue(null, "value");
								String min = xpp.getAttributeValue(null, "min");
								String max = xpp.getAttributeValue(null, "max");
								String unit = xpp.getAttributeValue(null, "unit");
								
								if(temp != null) {
									weather.setTemprature(temp);
								}
								if(min != null) {
									weather.setTempratureMin(min);
								}
								if(max != null) {
									weather.setTempratureMax(max);
								}
								if(unit != null) {
									weather.setTempUnit(unit);
								}
							}
							if(isHumidityFound) {
								String value = xpp.getAttributeValue(null, "value");
								String unit = xpp.getAttributeValue(null, "unit");
								
								if(value != null) {
									weather.setHumidity(value);
								}
								if(unit != null) {
									weather.setHumidityUnit(unit);
								}
							}
							if(isPressureFound) {
								String value = xpp.getAttributeValue(null, "value");
								String unit = xpp.getAttributeValue(null, "unit");
								
								if(value != null) {
									weather.setPressure(value);
								}
								if(unit != null) {
									weather.setPressureUnit(unit);
								}
							}
							if(isSpeedFound) {
								String value = xpp.getAttributeValue(null, "value");
								String name = xpp.getAttributeValue(null, "name");
								
								if(value != null) {
									weather.setWindSpeed(value);
								}
								if(name != null) {
									weather.setWindName(name);
								}
							}
							if(isDirectionFound) {
								String value = xpp.getAttributeValue(null, "value");
								String code = xpp.getAttributeValue(null, "code");
								
								if(value != null) {
									weather.setWindDirection(value);
								}
								if(code != null) {
									weather.setWindCode(code);
								}
							}
							if(isCloudsFound) {
								String name = xpp.getAttributeValue(null, "name");
								if(name != null) {
									weather.setClouds(name);
								}
							}
							if(isPrecipitationFound) {
								String mode = xpp.getAttributeValue(null, "mode");
								String unit = xpp.getAttributeValue(null, "unit");
								
								if(mode != null) {
									weather.setPrecipitation(mode);
								}
								if(unit != null) {
									weather.setPrecipitationUnit(unit);
								}
							}
							if(isWeatherFound) {
								String value = xpp.getAttributeValue(null, "value");
								if(value != null) {
									weather.setClouds(value);
								}
							}
							if(isLastUpdateFound) {
								String value = xpp.getAttributeValue(null, "value");
								if(value != null) {
									weather.setLastUpdate(value);
								}
							}

							break;
						case XmlPullParser.END_TAG:
							String tag2 = xpp.getName();
							if("item".equalsIgnoreCase(tag2)){
								//returns the first result
								return weather;
							}
							if ("city".equals(tag2)) {
								isCityFound = false;
							}
							if ("sun".equals(tag2) && isCityFound) {
								isSunFound = false;
							}
							if ("temperature".equals(tag2)) {
								isTempratureFound = false;
							}
							if ("humidity".equals(tag2)) {
								isHumidityFound = false;
							}
							if ("pressure".equals(tag2)) {
								isPressureFound = false;
							}
							if ("wind".equals(tag2)) {
								isWindFound = false;
							}
							if ("speed".equals(tag2) && isWindFound) {
								isSpeedFound = false;
							}
							if ("direction".equals(tag2) && isWindFound) {
								isDirectionFound = false;
							}
							if ("clouds".equals(tag2)) {
								isCloudsFound = false;
							}
							if ("precipitation".equals(tag2)) {
								isPrecipitationFound = false;
							}
							if ("weather".equals(tag2)) {
								isWeatherFound = false;
							}
							if ("lastupdate".equals(tag2)) {
								isLastUpdateFound = false;
							}
							break;
						}
						eventType = xpp.next();
						count++;
					}
				} catch (XmlPullParserException ex) {

				} catch (IOException ex) {
				}
			} else {
				Log.e(XMLreaderWeather.class.toString(),
						"Failed to download file");
			}
		} catch (ClientProtocolException e) {

		} catch (IOException e) {

		}

		return weather;
	}

}
