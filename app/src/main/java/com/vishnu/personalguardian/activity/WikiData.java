package com.vishnu.personalguardian.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnu.personalguardian.R;
import com.vishnu.personalguardian.logic.XMLWikiReader;

/**
 * The ACTIVITY of the application to show Wikipedia result in list view
 * 
 * @author Vishnuvathsasarma
 *
 */

public class WikiData extends ListActivity {

	private String URL;
	private XMLWikiReader reader;

	private List<String> description = new ArrayList<String>();
	String destination;
	ProgressDialog ringProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destination_list);

		ringProgressDialog = ProgressDialog.show(this, "Please wait...",
				"Retrieving data from the internet...", true);
		ringProgressDialog.setCancelable(false);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			destination = bundle.getString("Destination");
			destination = destination.replaceAll(" ", "%20");
			URL = "http://en.wikipedia.org/w/api.php?action=opensearch&search="
					+ destination + "&format=xml";
			reader = new XMLWikiReader();
			reader.setTAG("Description");
			new WikiReader().execute(URL);

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

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// get data of the destination selected
		String selectedResult = description.get(position).replaceAll(" () ", " ");
		Intent intent = new Intent(this, WikiResult.class);
		intent.putExtra("Description", selectedResult);
		startActivity(intent);
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

	private class WikiReader extends AsyncTask<String, Void, List<String>> {
		@Override
		protected List<String> doInBackground(String... urls) {
			return reader.readURL(urls[0]);
		}

		@Override
		protected void onPostExecute(List<String> result) {
			description = result;
			if (!description.isEmpty() && description != null) {
				List<String> temp = new ArrayList<String>();
				for (int i = 0; i < description.size(); i++) {
					temp.add((i+1) + ": " + (description.get(i).length() < 25 ? description.get(i).replaceAll(" () ", " ") : description.get(i).substring(0, 25).replaceAll(" () ", " ")) + "....");
				}
				ArrayAdapter<String> adapter = new MyAdapter(WikiData.this,
						android.R.layout.simple_list_item_1, temp);
				setListAdapter(adapter);

				ringProgressDialog.dismiss();
				
			} else {
				// invoked when no data received due to error in internet
				// connection
				AlertDialog.Builder builder = new AlertDialog.Builder(
						WikiData.this);
				builder.setMessage(R.string.internet_error_msg)
						.setTitle("Unable to retrive data from internet")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										WikiData.this.finish();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	}
}
