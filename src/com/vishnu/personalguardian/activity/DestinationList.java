package com.vishnu.personalguardian.activity;

import java.util.ArrayList;
import java.util.List;

import com.vishnu.personalguardian.R;
import com.vishnu.personalguardian.logic.MyLocation;
import com.vishnu.personalguardian.logic.XMLreaderLocation;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
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
 * @author Vishnuvathsasarma
 *
 */
public class DestinationList extends ListActivity {

	private List<String> data = new ArrayList<String>();
	private List<MyLocation> location = new ArrayList<MyLocation>();
	String destination;
	// private XMLreader reader;
	private XMLreaderLocation locationReader;
	private String URL, country;
	ProgressDialog ringProgressDialog ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destination_list);
		
		ringProgressDialog = ProgressDialog.show(
				this, "Please wait...",
				"Retriving data from the internet...", true);
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

		}
		locationReader = new XMLreaderLocation();
		// locationReader.setTAG("formatted_address");
		new XmlReader().execute(URL);

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
		switch(item.getItemId()) {
		case R.id.action_settings :
			Intent intent1 = new Intent(this, Setting.class);
			//Intent intent1 = new Intent(this, PrefActivity.class);
			startActivity(intent1);
			return true;
		case R.id.about_author :
			Intent intent2 = new Intent(this, AboutAuthor.class);
			startActivity(intent2);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// String dest = data.get(position);
		MyLocation dest = location.get(position);

		Toast.makeText(
				this,
				dest.getAddress() + "\n\t\tLatitude    : " + dest.getLattitude()
						+ "\n\t\tLongitude : " + dest.getLongitude(),
				Toast.LENGTH_LONG).show();

		// remove this
		
		/*
		 * double latitude = 0.00, longitude = 0.00; if (gps.canGetLocation()) {
		 * latitude = gps.getLatitude(); longitude = gps.getLongitude();
		 * Toast.makeText( this, "Your Location is - \nLat: " + latitude +
		 * "\nLong: " + longitude, Toast.LENGTH_LONG).show(); }
		 */
		
		//

		Intent tracker = new Intent(this, Tracking.class);
		tracker.putExtra("Destination Latitude", dest.getLattitude());
		tracker.putExtra("Destination Longitude", dest.getLongitude());
		tracker.putExtra("Address", dest.getAddress());
		startActivity(tracker);
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
			ArrayAdapter<String> adapter = new MyAdapter(DestinationList.this,
					android.R.layout.simple_list_item_1, data);
			setListAdapter(adapter);
			
			ringProgressDialog.dismiss();
		}

	}
}
