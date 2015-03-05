package com.vishnu.personalguardian.activity;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vishnu.personalguardian.R;
import com.vishnu.personalguardian.logic.MyLocation;
import com.vishnu.personalguardian.logic.XMLreaderLocation;

import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * The MAP ACTIVITY of the application to select destination on google maps
 * 4 types of Map views available
 * 
 * @author Vishnuvathsasarma
 *
 */

public class Map extends ActionBarActivity {

	private GoogleMap mMap;
	private MarkerOptions markerOption;
	private Marker marker;
	private XMLreaderLocation locationReader;
	private String country;
	private double currLat, currLon;
	private ProgressDialog ringProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		Intent curr = getIntent();
		Bundle bundle = curr.getExtras();
		if (bundle != null) {
			country = bundle.getString("Country");
			currLat = bundle.getDouble("Lat");
			currLon = bundle.getDouble("Lon");
		}

		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		// googleMapOptions = new GoogleMapOptions();
		if (mMap != null) {
			LatLng latLng = new LatLng(currLat, currLon);
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
					mMap.getMaxZoomLevel() - 5));
			markerOption = new MarkerOptions()
					.position(latLng)
					.title(String.format("%.4f", latLng.latitude) + ", "
							+ String.format("%.4f", latLng.longitude))
					.draggable(true)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_location_map));
			marker = mMap.addMarker(markerOption);
			marker.showInfoWindow();
			mMap.setMyLocationEnabled(true);
			mMap.getUiSettings().setMyLocationButtonEnabled(true);
			mMap.getUiSettings().setCompassEnabled(true);
			mMap.getUiSettings().setRotateGesturesEnabled(true);
			mMap.getUiSettings().setScrollGesturesEnabled(true);
			mMap.getUiSettings().setTiltGesturesEnabled(true);
			mMap.getUiSettings().setZoomControlsEnabled(true);
			mMap.getUiSettings().setZoomGesturesEnabled(true);

			mMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(LatLng result) {
					// TODO Auto-generated method stub
					marker.setPosition(result);
					marker.setTitle(String.format("%.4f", result.latitude)
							+ ", " + String.format("%.4f", result.longitude));
					marker.showInfoWindow();
				}
			});
			
			mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

				@Override
				public void onMapLongClick(LatLng result) {
					// TODO Auto-generated method stub
					marker.setPosition(result);
					marker.setTitle(String.format("%.4f", result.latitude)
							+ ", " + String.format("%.4f", result.longitude));
					marker.showInfoWindow();
					
					ringProgressDialog = ProgressDialog.show(Map.this, "Please wait...",
							"Retrieving location detail from the internet...", true);
					ringProgressDialog.setCancelable(false);
					
					String URL = "https://maps.googleapis.com/maps/api/geocode/xml?latlng="
							+ result.latitude
							+ ","
							+ result.longitude
							+ "&key=AIzaSyCyc9xJr_8wXxrmjeadKVhpVp84nkleoyE";

					locationReader = new XMLreaderLocation();
					new XmlReader().execute(URL);
				}
			});

			mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker result) {
					// TODO Auto-generated method stub
					
					ringProgressDialog = ProgressDialog.show(Map.this, "Please wait...",
							"Retrieving location detail from the internet...", true);
					ringProgressDialog.setCancelable(false);
					
					LatLng latlng = result.getPosition();
					String URL = "https://maps.googleapis.com/maps/api/geocode/xml?latlng="
							+ latlng.latitude
							+ ","
							+ latlng.longitude
							+ "&key=AIzaSyCyc9xJr_8wXxrmjeadKVhpVp84nkleoyE";

					locationReader = new XMLreaderLocation();
					new XmlReader().execute(URL);
					return false;
				}
			});

			mMap.setOnMarkerDragListener(new OnMarkerDragListener() {

				@Override
				public void onMarkerDragStart(Marker result) {
					// TODO Auto-generated method stub
					LatLng latlng = result.getPosition();
					result.setTitle(String.format("%.4f", latlng.latitude)
							+ ", " + String.format("%.4f", latlng.longitude));
					result.showInfoWindow();
				}

				@Override
				public void onMarkerDragEnd(Marker result) {
					// TODO Auto-generated method stub
					LatLng latlng = result.getPosition();
					result.setTitle(String.format("%.4f", latlng.latitude)
							+ ", " + String.format("%.4f", latlng.longitude));
					result.showInfoWindow();
				}

				@Override
				public void onMarkerDrag(Marker result) {
					// TODO Auto-generated method stub
					LatLng latlng = result.getPosition();
					result.setTitle(String.format("%.4f", latlng.latitude)
							+ ", " + String.format("%.4f", latlng.longitude));
					result.showInfoWindow();
				}
			});
			
			mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				
				@Override
				public void onInfoWindowClick(Marker result) {
					// TODO Auto-generated method stub
					ringProgressDialog = ProgressDialog.show(Map.this, "Please wait...",
							"Retrieving location detail from the internet...", true);
					ringProgressDialog.setCancelable(false);
					
					LatLng latlng = result.getPosition();
					String URL = "https://maps.googleapis.com/maps/api/geocode/xml?latlng="
							+ latlng.latitude
							+ ","
							+ latlng.longitude
							+ "&key=AIzaSyCyc9xJr_8wXxrmjeadKVhpVp84nkleoyE";

					locationReader = new XMLreaderLocation();
					new XmlReader().execute(URL);
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (mMap != null) {
			switch (item.getItemId()) {
			case R.id.map_normal:
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				return true;
			case R.id.map_satellite:
				mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				return true;
			case R.id.map_hybrid:
				mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				return true;
			case R.id.map_terrain:
				mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private class XmlReader extends AsyncTask<String, Void, List<MyLocation>> {
		@Override
		protected List<MyLocation> doInBackground(String... urls) {
			return locationReader.readURL(urls[0]);
		}

		@Override
		protected void onPostExecute(List<MyLocation> result) {
			if (!result.isEmpty() && result != null) {
				Intent tracker = new Intent(Map.this, Tracking.class);
				tracker.putExtra("Destination Latitude", result.get(0)
						.getLattitude());
				tracker.putExtra("Destination Longitude", result.get(0)
						.getLongitude());
				tracker.putExtra("Address", result.get(0).getAddress());
				ringProgressDialog.dismiss();
				startActivity(tracker);
			} else {
				// invoked when no data received due to error in internet
				// connection
				AlertDialog.Builder builder = new AlertDialog.Builder(Map.this);
				builder.setMessage(R.string.internet_error_msg)
						.setTitle("Unable to retrive data from internet")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Map.this.finish();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	}
}
