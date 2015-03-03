package com.vishnu.personalguardian.activity;

import com.vishnu.personalguardian.R;
import com.vishnu.personalguardian.logic.XMLreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Vishnuvathsasarma
 *
 */
public class Splash extends Activity {

	private String country;
	private XMLreader locationReader;
	ProgressDialog ringProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		if (isLocationServiceAvailable()) {
			if (isNetworkAvailable()) {

				// need to get curr location from GPS
				LocationManager locationManager = (LocationManager) this
						.getSystemService(Service.LOCATION_SERVICE);
				Location location = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

				double latitude = location.getLatitude();		//20.593;
				double longitude = location.getLongitude();		//78.962; 


				String currLoc = latitude + "," + longitude;
				String URL = "https://maps.googleapis.com/maps/api/geocode/xml?latlng="
						+ currLoc
						+ "&result_type=country&key=AIzaSyCyc9xJr_8wXxrmjeadKVhpVp84nkleoyE";
				locationReader = new XMLreader();
				locationReader.setTAG("formatted_address");
				// locationReader.setTAG("formatted_address");

				ringProgressDialog = ProgressDialog.show(Splash.this,
						"Please wait...",
						"Retriving data from the internet...", true);
				ringProgressDialog.setCancelable(false);

				new XmlReader().execute(URL);

			} else {
				askToEnableNetwork();
			}
		} else {
			askToEnableLocationService();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	// Check Internet connection
	private boolean isNetworkAvailable() {
		// Log.i("&%$^#%^&#%&#%^#$%#^&", "iuyfbuytf");
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	// Show alert dialog to confirm and enable the network
	private void askToEnableNetwork() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"You need a network connection to use this application.\nPlease turn on mobile network or Wi-Fi in Settings.")
				.setTitle("Unable to connect")
				.setCancelable(false)
				.setPositiveButton("Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent i = new Intent(
										Settings.ACTION_WIRELESS_SETTINGS);
								startActivity(i);
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// Check LocationService
	private boolean isLocationServiceAvailable() {
		// Log.i("&%$^#%^&#%&#%^#$%#^&", "iuyfbuytf");
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	// Show alert dialog to confirm and enable the LocationService
	private void askToEnableLocationService() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"You need a to turn on Location Services to use this application.\nPlease turn on Location Service in Settings.")
				.setTitle("Unable to detect location")
				.setCancelable(false)
				.setPositiveButton("Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent i = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(i);
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private class XmlReader extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String temp = locationReader.readURL(urls[0]);
			return temp;
		}

		@Override
		protected void onPostExecute(String result) {
			country = result;
			if (country != null && country.length() > 0) {
				Toast.makeText(Splash.this,
						"Enter a destination in " + country, Toast.LENGTH_SHORT)
						.show();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent openMainActivity = new Intent(Splash.this, Main.class);
				openMainActivity.putExtra("Country", country);

				Log.i("%%%%COUNTER%%%%", "kuystgbfviuysrfgi");
				ringProgressDialog.dismiss();

				startActivity(openMainActivity);
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						Splash.this);
				builder.setMessage(
						"Your internet connection is not working.\nCannot use the application without internet.\nApplication is exiting...")
						.setTitle("Unable to retrive data from internet")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Splash.this.finish();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}

	}
}

/*
 * ProgressDialog bar ProgressDialog barProgressDialog = new
 * ProgressDialog(MainActivity.this);
 * barProgressDialog.setTitle("Downloading Image ...");
 * barProgressDialog.setMessage("Download in progress ...");
 * barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
 * barProgressDialog.setProgress(0); barProgressDialog.setMax(20);
 * barProgressDialog.show();
 * 
 * new Thread(new Runnable() {
 * 
 * @Override public void run() { try { // Here you should write your time
 * consuming task... while (barProgressDialog.getProgress() <=
 * barProgressDialog.getMax()) { Thread.sleep(2000); updateBarHandler.post(new
 * Runnable() { public void run() { barProgressDialog.incrementProgressBy(2); }
 * }); if (barProgressDialog.getProgress() == barProgressDialog.getMax()) {
 * barProgressDialog.dismiss(); } } } catch (Exception e) { } } }).start();
 */
