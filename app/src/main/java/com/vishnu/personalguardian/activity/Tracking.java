package com.vishnu.personalguardian.activity;

import com.vishnu.personalguardian.R;
import com.vishnu.personalguardian.logic.TrackingService;
import com.vishnu.personalguardian.logic.XMLreaderDistance;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Tracking extends ActionBarActivity {

	private Intent serviceIntent;
	private TextView tvTrackingData;
	private Button btnStart;
	private XMLreaderDistance distanceReader;
	private String URL, destinationAddress, distance;
	private String travelMode = "Driving";
	private double currLatitude = 0.00, currLongitude = 0.00,
			destLatitude = 0.00, destLongitude = 0.00, directDistance = 0.00;
	ProgressDialog ringProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_tracking);

		ringProgressDialog = ProgressDialog.show(this, "Please wait...",
				"Retriving data from the internet...", true);
		ringProgressDialog.setCancelable(false);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			destLatitude = bundle.getDouble("Destination Latitude");
			destLongitude = bundle.getDouble("Destination Longitude");
			destinationAddress = bundle.getString("Address");			
		}

		tvTrackingData = (TextView) findViewById(R.id.tvTrackData);
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// start tracking service
				//btnStart.setEnabled(false);
				serviceIntent = new Intent(Tracking.this, TrackingService.class);
				serviceIntent.putExtra("Destination Latitude", destLatitude);
				serviceIntent.putExtra("Destination Longitude", destLongitude);
				startService(serviceIntent);

			}
		});

		// need to get curr location from GPS
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Service.LOCATION_SERVICE);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		currLatitude = location.getLatitude(); // 20.593;
		currLongitude = location.getLongitude(); // 78.962;
		
		directDistance = getDistance(location, destLatitude, destLongitude);

		Toast.makeText(
				this,
				"Your Current Location: \n\tLatitude  : " + currLatitude
						+ "\n\tLongitude : " + currLongitude,
				Toast.LENGTH_SHORT).show();

		String currLoc = currLatitude + "," + currLongitude;
		String destinationGeocode = destLatitude + "," + destLongitude;

		URL = "http://maps.googleapis.com/maps/api/distancematrix/xml?origins="
				+ currLoc + "&destinations=" + destinationGeocode;
		distanceReader = new XMLreaderDistance();
		new XmlReader().execute(URL);
	}
	
	private double getDistance(Location location, double destLat2, double destLon2) {
		// TODO Auto-generated method stub
		Location loc = new Location(LOCATION_SERVICE);
		loc.setLatitude(destLat2);
		loc.setLongitude(destLon2);
		double dist = location.distanceTo(loc)/1000;
		dist = dist - dist % 0.000001;	//6 decimal point
		return dist; //returns in kilometer
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
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
			// Intent intent1 = new Intent(this, PrefActivity.class);
			startActivity(intent1);
			return true;
		case R.id.about_author:
			Intent intent2 = new Intent(this, AboutAuthor.class);
			startActivity(intent2);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class XmlReader extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			String temp = distanceReader.readURL(urls[0]);
			return temp;
		}

		@Override
		protected void onPostExecute(String result) {
			distance = result;
			tvTrackingData.setText("Current Position:\n\n\t\tLatitude    : "
					+ currLatitude + "\n\t\tLongitude : " + currLongitude
					+ "\n\nDestination:\n\n" + destinationAddress
					+ "\n\t\tLatitude    : " + currLatitude
					+ "\n\t\tLongitude : " + currLongitude
					+ "\n\nDirect distance: " + directDistance
					+ " km\nJourney distance: " + distance + "\nTravel Mode : "
					+ travelMode);

			ringProgressDialog.dismiss();
		}

	}
}
