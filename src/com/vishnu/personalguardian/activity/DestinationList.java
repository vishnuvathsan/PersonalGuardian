package com.vishnu.personalguardian.activity;

import java.util.ArrayList;
import java.util.List;

import com.vishnu.personalguardian.R;
import com.vishnu.personalguardian.logic.MyLocation;
import com.vishnu.personalguardian.logic.XMLreaderLocation;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the ACTIVITY that displays a list of possible destinations as
 * requested by the user user can select his/her intended destination to track
 * 
 * @author Vishnuvathsasarma
 *
 */
public class DestinationList extends ListActivity {

	private List<String> data = new ArrayList<String>();
	private List<MyLocation> location = new ArrayList<MyLocation>();
	private MyLocation dest;
	private String destination;
	private XMLreaderLocation locationReader;
	private String URL, country;
	private ProgressDialog ringProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destination_list);

		ringProgressDialog = ProgressDialog.show(this, "Please wait...",
				"Retrieving location details from the internet...", true);
		ringProgressDialog.setCancelable(false);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			destination = bundle.getString("Destination");
			destination = destination.replaceAll(" ", "%20");

			// Log.i("vishnuvathsan", destination);
			country = bundle.getString("Country");
			country = country.replaceAll(" ", "%20");
			// Log.d("++++++++++++++++", "the local is " + country);
			URL = "http://maps.googleapis.com/maps/api/geocode/xml?address="
					+ destination + "&sensor=true&components=country:"
					+ country;

			locationReader = new XMLreaderLocation();
			new XmlReader().execute(URL);
		} else {
			Toast.makeText(getApplicationContext(),
					"Error in getting data from previous activity",
					Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.destination, menu);
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
			Intent intent3 = new Intent(this, WikiData.class);
			intent3.putExtra("Destination", destination);
			startActivity(intent3);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// get data of the destination selected
		dest = location.get(position);

		AlertDialog.Builder builder = new AlertDialog.Builder(
				DestinationList.this);
		builder.setMessage(
				"Do you want to open google map to verify your location?")
				.setTitle("Open Google Map")
				.setCancelable(true)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// this open a google map view on a new window
								Intent map = new Intent(DestinationList.this,
										Map.class);
								map.putExtra("Country", country);
								map.putExtra("Lat", dest.getLattitude());
								map.putExtra("Lon", dest.getLongitude());

								startActivity(map);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// opens tracking activity
						Intent tracker = new Intent(DestinationList.this,
								Tracking.class);
						tracker.putExtra("Destination Latitude",
								dest.getLattitude());
						tracker.putExtra("Destination Longitude",
								dest.getLongitude());
						tracker.putExtra("Address", dest.getAddress());
						startActivity(tracker);
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	class MyAdapter extends ArrayAdapter<String> {
		private List<String> values;

		public MyAdapter(Context context, int resource, List<String> objects) {
			super(context, resource, objects);
			this.values = objects;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.list_item, parent, false);
			}
			TextView text = (TextView) view.findViewById(R.id.textView);
			text.setText(values.get(position));
			return view;
		}
	}

	private class XmlReader extends AsyncTask<String, Void, List<String>> {
		@Override
		protected List<String> doInBackground(String... urls) {
			List<String> temp = new ArrayList<String>();
			location = locationReader.readURL(urls[0]);
			for (int i = 0; i < location.size(); i++) {
				temp.add(location.get(i).getAddress());
			}
			return temp;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			data = result;
			if (data.isEmpty() || data == null) {
				// invoked when no data received due to error in internet
				// connection
				AlertDialog.Builder builder = new AlertDialog.Builder(
						DestinationList.this);
				builder.setMessage(R.string.internet_error_msg)
						.setTitle("Unable to retrive data from internet")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										DestinationList.this.finish();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			} else {
				ArrayAdapter<String> adapter = new MyAdapter(
						DestinationList.this,
						android.R.layout.simple_list_item_1, data);
				setListAdapter(adapter);

				ringProgressDialog.dismiss();
			}
		}
	}
}
