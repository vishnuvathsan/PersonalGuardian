package com.vishnu.personalguardian.activity;

import com.vishnu.personalguardian.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//http://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&sensor=true
//http://maps.googleapis.com/maps/api/geocode/xml?address=%22apple%22&sensor=true&components=country:Sri%20Lanka
//http://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&sensor=true&key=AIzaSyCyc9xJr_8wXxrmjeadKVhpVp84nkleoyE
//https://maps.googleapis.com/maps/api/geocode/xml?latlng=6.9720785,79.8704806&result_type=country&key=AIzaSyCyc9xJr_8wXxrmjeadKVhpVp84nkleoyE
/**
 * @author Vishnuvathsasarma
 *
 */
public class Main extends Activity {
	private Button btnSearch;
	private EditText etxtDestination;
	private String country;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// temp code
		// when deleting, delete every file relate to preferences
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		String number = prefs.getString("number", null);
		if (number == null) {
			Intent intent1 = new Intent(Main.this, Pref.class);
			startActivity(intent1);
		}

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			country = bundle.getString("Country");
			// Log.i("&%$^#%^&#%&#%^#$%#^&", country);
		}

		btnSearch = (Button) findViewById(R.id.btnSearch);
		etxtDestination = (EditText) findViewById(R.id.etxtDestination);

		etxtDestination.setHint("Enter a destination in " + country);

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String destination = etxtDestination.getText().toString();
				if (destination != null && destination.length() > 0) {
					Intent intent = new Intent(Main.this,
							DestinationList.class);
					intent.putExtra("Destination", destination);
					intent.putExtra("Country", country);
					startActivity(intent);

				} else {
					Toast.makeText(Main.this,
							"Please enter a destination", Toast.LENGTH_SHORT)
							.show();
				}
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
			// Intent intent1 = new Intent(this, PrefActivity.class);
			startActivity(intent1);
			return true;
		case R.id.about_author:
			Intent intent2 = new Intent(Main.this, AboutAuthor.class);
			startActivity(intent2);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setTitle("Exit")
				.setMessage("Do you want to close the application?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								Intent intent = new Intent(Intent.ACTION_MAIN);
								intent.addCategory(Intent.CATEGORY_HOME);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
								finish();
							}
						}).setNegativeButton("No", null).show();
	}
}
