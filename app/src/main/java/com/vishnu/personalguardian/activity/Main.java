package com.vishnu.personalguardian.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.vishnu.personalguardian.R;
import com.vishnu.personalguardian.logic.TrackingService;
import com.vishnu.personalguardian.logic.UserConfDAO;
import com.vishnu.personalguardian.logic.UserConfiguration;

//http://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&sensor=true
//http://maps.googleapis.com/maps/api/geocode/xml?address=%22apple%22&sensor=true&components=country:Sri%20Lanka
//http://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&sensor=true&key=AIzaSyCyc9xJr_8wXxrmjeadKVhpVp84nkleoyE
//https://maps.googleapis.com/maps/api/geocode/xml?latlng=6.9720785,79.8704806&result_type=country&key=AIzaSyCyc9xJr_8wXxrmjeadKVhpVp84nkleoyE
/**
 * The MAIN ACTIVITY of the application to enter destination detail
 * 
 * @author Vishnuvathsasarma
 *
 */
public class Main extends Activity {

	public static UserConfiguration conf;
	private Button btnSearch, btnMap;
	private EditText etxtDestination;
	private String country;
	private double currLat, currLon;

	private void openMapView() {
		// this open a google map view on a new window
		Intent map = new Intent(Main.this, Map.class);
		map.putExtra("Country", country);
		map.putExtra("Lat", currLat);
		map.putExtra("Lon", currLon);

		startActivity(map);

	}

	private void openSettings() {
		// opens settings page
		Intent intent1 = new Intent(Main.this, Setting.class);
		startActivity(intent1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// to configure preferences if it is not configured before
		conf = UserConfDAO.getInstance().loadConfiguration(this);
		if (conf == null) {
			openSettings();
		} 

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			country = bundle.getString("Country");
			currLat = bundle.getDouble("Lat");
			currLon = bundle.getDouble("Lon");
			// Log.i("&%$^#%^&#%&#%^#$%#^&", country);
		}

		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnMap = (Button) findViewById(R.id.btnMap);
		etxtDestination = (EditText) findViewById(R.id.etxtDestination);
		etxtDestination.setSelectAllOnFocus(true);
		etxtDestination.setHint("Enter a destination in " + country);

		etxtDestination.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				openMapView();
				return true;
			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String destination = etxtDestination.getText().toString();
				if (destination != null && destination.length() > 0) {
					Intent intent = new Intent(Main.this, DestinationList.class);
					intent.putExtra("Destination", destination);
					intent.putExtra("Country", country);
					startActivity(intent);

				} else {
					Toast.makeText(Main.this, "Please enter a destination",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openMapView();
			}
		});
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
			startActivity(intent1);
			return true;
		case R.id.about_author:
			Intent intent2 = new Intent(this, AboutAuthor.class);
			startActivity(intent2);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

//	@Override
//	public void onBackPressed() { // show alert dialog for confirmation to exit
//									// the application
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("Exit")
//				.setMessage("Do you want to exit?\nIf you exit, the tracking service will be stopped(instead, click CANCEL and press HOME)")
//				.setCancelable(false)
//				.setPositiveButton("Yes",
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								showPasswordDialog();
//							}
//						}).setNegativeButton("No", null);
//		AlertDialog alert = builder.create();
//		alert.show();
//	}
	
	private void showPasswordDialog() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		final View loginView = inflater.inflate(
				R.layout.alert_activity_password, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(loginView)
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {

								EditText etxtPwd = (EditText) loginView
										.findViewById(R.id.etxtAlertPassword);
								String password = etxtPwd.getText().toString();
								Log.i("Pwd", password);
								if (password.equalsIgnoreCase(Main.conf.getPassword())) {
									if (TrackingService.smsCancelTimer != null) {
										TrackingService.smsCancelTimer.cancel();
										Log.i("Timer", "Cancelled");
									}
									Main.this.finish();
								} else {
									showPasswordDialog();
									Toast.makeText(Main.this,
											"Password Incorrect",
											Toast.LENGTH_SHORT).show();
								}
							}
						})
				.setNegativeButton("Cancel", null);
		AlertDialog alert = builder.create();
		alert.show();
	}
}
