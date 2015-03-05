package com.vishnu.personalguardian.activity;

import com.vishnu.personalguardian.R;
import com.vishnu.personalguardian.logic.TrackingService;
import com.vishnu.personalguardian.logic.Weather;
import com.vishnu.personalguardian.logic.XMLreaderDistance;
import com.vishnu.personalguardian.logic.XMLreaderWeather;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The TRACKING ACTIVITY of the application to see destination detail (address,
 * latitude, longitude, distance to destination and weather) and then user can
 * start TRACKING SERVICE
 * 
 * @author Vishnuvathsasarma
 *
 */

public class Tracking extends ActionBarActivity {

	// weather api
	// http://api.openweathermap.org/data/2.5/weather?lat=6.940933&lon=79.861141&mode=xml&units=metric
	// http://api.openweathermap.org/data/2.5/find?q=London&type=like&mode=xml
	// http://api.openweathermap.org/data/2.5/find?q=London&type=accurate&mode=xml

	private final double MIN_BATTERY_PERCENT_REQUIRED = 25.00;
	private Weather weather;
	private Intent serviceIntent;
	private TextView tvTrackingData;
	private Button btnStart, btnStop;
	private XMLreaderDistance distanceReader;
	private XMLreaderWeather weatherReader;
	private String URL, destinationAddress;
	private String travelMode = "Driving";
	private double currLatitude = 0.00, currLongitude = 0.00,
			destLatitude = 0.00, destLongitude = 0.00, directDistance = 0.00,
			distance = 0.00;
	private ProgressDialog ringProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracking);

		ringProgressDialog = ProgressDialog.show(this, "Please wait...",
				"Retrieving location detail from the internet...", true);
		ringProgressDialog.setCancelable(false);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			destLatitude = bundle.getDouble("Destination Latitude");
			destLongitude = bundle.getDouble("Destination Longitude");
			destinationAddress = bundle.getString("Address");
		}

		tvTrackingData = (TextView) findViewById(R.id.tvTrackData);

		// button id problem...working with swapped IDs
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStop = (Button) findViewById(R.id.btnStop);
		btnStop.setEnabled(false);

		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// start tracking service

				if (isBatteryLevelSufficient()) {

					serviceIntent = new Intent(Tracking.this,
							TrackingService.class);
					serviceIntent
							.putExtra("Destination Latitude", destLatitude);
					serviceIntent.putExtra("Destination Longitude",
							destLongitude);
					startService(serviceIntent);
					btnStart.setEnabled(false);
					btnStop.setEnabled(true);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Tracking.this);
					builder.setMessage(
							"Your batter percentage might not be sufficient enough track you until you reach the destination.\nYou might be running out of battery before reaching your destination.\nAre you still want to start tracking service?")
							.setTitle("Battery Warning!")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											serviceIntent = new Intent(
													Tracking.this,
													TrackingService.class);
											serviceIntent.putExtra(
													"Destination Latitude",
													destLatitude);
											serviceIntent.putExtra(
													"Destination Longitude",
													destLongitude);
											startService(serviceIntent);
											btnStart.setEnabled(false);
											btnStop.setEnabled(true);
										}
									}).setNegativeButton("No", null);
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});

		btnStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPasswordDialog();
			}
		});

		// need to get curr location from GPS
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Service.LOCATION_SERVICE);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}

		currLatitude = location.getLatitude(); // 20.593;
		currLongitude = location.getLongitude(); // 78.962;

		directDistance = getDistance(location, destLatitude, destLongitude);

		Toast.makeText(
				this,
				"Your Current Location: \n\tLatitude  : " + currLatitude
						+ "\n\tLongitude : " + currLongitude,
				Toast.LENGTH_SHORT).show();

		String currGeocode = currLatitude + "," + currLongitude;
		String destinationGeocode = destLatitude + "," + destLongitude;

		URL = "http://maps.googleapis.com/maps/api/distancematrix/xml?origins="
				+ currGeocode + "&destinations=" + destinationGeocode;
		distanceReader = new XMLreaderDistance();
		new XmlReader().execute(URL);
	}

	private double getDistance(Location location, double destLat2,
			double destLon2) {
		// TODO Auto-generated method stub
		Location loc = new Location(LOCATION_SERVICE);
		loc.setLatitude(destLat2);
		loc.setLongitude(destLon2);
		double dist = location.distanceTo(loc) / 1000; // get and convert to
														// kilometer
		dist = Double.parseDouble(String.format("%.3f", dist)); // 3 decimal
																// point
		return dist;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tracking, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent1 = new Intent(this, Setting.class);
			startActivity(intent1);
			return true;
		case R.id.about_author:
			Intent intent2 = new Intent(this, AboutAuthor.class);
			startActivity(intent2);
			return true;
		case R.id.action_result:
			if (weather == null) {
				ringProgressDialog = ProgressDialog.show(this,
						"Please wait...",
						"Retrieving weather detail from the internet...", true);
				ringProgressDialog.setCancelable(false);
				String url = "http://api.openweathermap.org/data/2.5/find?lat="
						+ destLatitude + "&lon=" + destLongitude
						+ "&mode=xml&units=metric&type=accurate";
				weatherReader = new XMLreaderWeather();
				new XmlWeatherReader().execute(url);
			} else {
				Intent intent = new Intent(Tracking.this, WeatherResult.class);
				intent.putExtra("Description", weather.getWeatherData());
				startActivity(intent);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// @Override
	// public void onBackPressed() {
	// // TODO Auto-generated method stub
	// super.onBackPressed();
	// if(btnStop.isEnabled()) {
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// builder.setTitle("Service Running")
	// .setMessage("You need to stop the service to go back?")
	// .setCancelable(false)
	// .setPositiveButton("OK",null);
	// AlertDialog alert = builder.create();
	// alert.show();
	// } else {
	// this.finish();
	// }
	// }

	private boolean isBatteryLevelSufficient() {
		// Get the battery level to decide whether to use the tracking service
		// or not
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = Tracking.this.registerReceiver(null, ifilter);
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		double batteryPercent = 100 * level / (double) scale;
		Log.i("BatteryLevel", "" + level);
		Log.i("BatteryScale", "" + scale);
		Log.i("Battery%", "" + batteryPercent);
		return ((batteryPercent > MIN_BATTERY_PERCENT_REQUIRED) && (distance
				/ batteryPercent < 1.5));
	}

	private void showPasswordDialog() {
		// method to verify the user and stop tracking service manually
		boolean isPasCorrect = false;
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		final View loginView = inflater.inflate(
				R.layout.alert_activity_password, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(loginView).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						EditText etxtPwd = (EditText) loginView
								.findViewById(R.id.etxtAlertPassword);
						String password = etxtPwd.getText().toString();
						Log.i("Pwd", password);
						if (password.equalsIgnoreCase(Main.conf.getPassword())) {
							if (TrackingService.smsCancelTimer != null) {
								TrackingService.smsCancelTimer.cancel();
								TrackingService.isDestinationReached = true;
								Log.i("Timer", "Cancelled");
								if (serviceIntent != null) {
									stopService(serviceIntent);
								}
								btnStart.setEnabled(true);
								btnStop.setEnabled(false);
							}
						} else {
							showPasswordDialog();
							Toast.makeText(Tracking.this, "Password Incorrect",
									Toast.LENGTH_SHORT).show();
						}
					}
				}).setNegativeButton("Cancel", null);
		AlertDialog alert = builder.create();
		alert.show();
	}

	private class XmlReader extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			String temp = distanceReader.readURL(urls[0]);
			return temp;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null && result.length() > 0) {
				try {
					distance = Double.parseDouble(result) / 1000;
				} catch (NumberFormatException e) {
					// invoked when no result received due invalid destination
					// here, the value of received String(result) would be
					// "ZERO_RESULT"
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Tracking.this);
					builder.setMessage(R.string.destination_error_msg)
							.setTitle("Destination ERROR!")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											Tracking.this.finish();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();

					e.printStackTrace();
					Log.e(result, "No driving path");
				}
				tvTrackingData
						.setText("Current Position:\n\n\t\tLatitude    : "
								+ currLatitude + "\n\t\tLongitude : "
								+ currLongitude + "\n\nDestination:\n\n"
								+ destinationAddress + "\n\t\tLatitude    : "
								+ destLatitude + "\n\t\tLongitude : "
								+ destLongitude + "\n\nDirect distance: "
								+ String.format("%.3f", directDistance)
								+ " km\nJourney distance: "
								+ String.format("%.3f", distance)
								+ " km\nTravel Mode : " + travelMode);

				ringProgressDialog.dismiss();
			} else {
				// invoked when no data received due to error in internet
				// connection
				AlertDialog.Builder builder = new AlertDialog.Builder(
						Tracking.this);
				builder.setMessage(R.string.internet_error_msg)
						.setTitle("Unable to retrive data from internet")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Tracking.this.finish();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	}

	private class XmlWeatherReader extends AsyncTask<String, Void, Weather> {

		@Override
		protected Weather doInBackground(String... urls) {
			// TODO Auto-generated method stub
			return weatherReader.readURL(urls[0]);
		}

		@Override
		protected void onPostExecute(Weather result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			weather = result;
			ringProgressDialog.dismiss();
			Intent intent = new Intent(Tracking.this, WeatherResult.class);
			intent.putExtra("Description", result.getWeatherData());
			startActivity(intent);
		}
	}
}
