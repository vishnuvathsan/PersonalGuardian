package com.vishnu.personalguardian.logic;

import java.util.ArrayList;
import java.util.List;

import com.vishnu.personalguardian.activity.Main;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Telephony.Sms;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class TrackingService extends IntentService {

	Location currLoc;
	boolean isMsgSent = false;
	double currDistance;
	List<Double> distance;
	private LocationReceiver receiver;
	int count = 0, deviationCount = 0;

	public TrackingService() {
		super("Tracking service");
		// TODO Auto-generated constructor stub
	}

	private double destLat = 0.00, destLon = 0.00;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		distance = new ArrayList<Double>();
		receiver = new LocationReceiver(this, 10, 1000);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		onHandleIntent(intent);
		Log.e("<<MyService1-onStart>>", "I am alive-1!");
		Log.e("<<MyService1-onStart>>", "I did something");

		// tracking activity starts here
		receiver.setOnLocationChangedListener(new OnLocationChangedListener() {

			@Override
			public void onLocationChanged(Location location) {
				// Do whatever you want here
				currDistance = getDistance(location, destLat, destLon);
				distance.add(currDistance);
				if (distance.size() > 1) {
					if (distance.get(count) > distance.get(count - 1)) {
						deviationCount++;
						Log.e("You are deviating from the target", ""
								+ currDistance);
						Log.e("Deviation Count", "" + deviationCount);
						Toast.makeText(
								getApplicationContext(),
								"You are deviating from the target\nDeviation Count: "
										+ deviationCount, Toast.LENGTH_SHORT)
								.show();
					}
					if (deviationCount > 0) {
						// sms alert code
						if (!isMsgSent) {
							sendMsg("0779848507", "Current Location:\n\tLat: "
									+ location.getLatitude() + "\n\tLong: "
									+ location.getLongitude()
									+ "\n\nDestination:\n\tLat: " + destLat
									+ "\n\tLong: " + destLon);
							isMsgSent = true;

							Log.e("ALERT!!!", "Sending message to guardian");
							Toast.makeText(
									getApplicationContext(),
									"ALERT!!!\nSending message to guardian\nCurrent Location:\n\tLat: "
											+ location.getLatitude()
											+ "\n\tLong: "
											+ location.getLongitude()
											+ "\n\nDestination:\n\tLat: "
											+ destLat + "\n\tLong: " + destLon,
									Toast.LENGTH_LONG).show();
						}
					}
				}
				Log.w("Distance", "" + currDistance);
				Log.w("Count", "" + count);
				Toast.makeText(
						getApplicationContext(),
						"Current Distance: " + currDistance + "\nCount: "
								+ count, Toast.LENGTH_SHORT).show();
				count++;

			}
		});
		receiver.start();

		/*
		 * currDistance = getDistance(destLat, destLon); int count = 0; do {
		 * Log.w("Distance", "" + currDistance);
		 * Toast.makeText(getApplicationContext(), "Current Distance: " +
		 * currDistance + "\nCount: " + count, Toast.LENGTH_LONG).show();
		 * prevDistance = currDistance; try { Thread.sleep(60000); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } gps.getLocation(); currDistance =
		 * getDistance(destLat, destLon); } while (prevDistance >= currDistance
		 * && currDistance > 0.2);
		 * 
		 * 
		 * if (prevDistance < currDistance) {
		 * Toast.makeText(getApplicationContext(),
		 * "You are deviating from the target", Toast.LENGTH_LONG).show(); }
		 */

		// tracking ends here

		// showNotification();
	}

	private double getDistance(Location location, double destLat2,
			double destLon2) {
		// TODO Auto-generated method stub
		Location loc = new Location(LOCATION_SERVICE);
		loc.setLatitude(destLat2);
		loc.setLongitude(destLon2);
		double dist = location.distanceTo(loc) / 1000;
		dist = dist - dist % 0.000001; // 6 decimal point
		return dist; // returns in kilometer
	}

	private void sendMsg(String number, String msg) {
		// TODO Auto-generated method stub
		// PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new
		// Intent("Message Sent"), 0);
		// PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new
		// Intent("Message Delivered"), 0);

		SmsManager smsMgr = SmsManager.getDefault();
		smsMgr.sendTextMessage(number, null,
				msg.length() < 160 ? msg : msg.substring(1, 158), null, null);
	}

	@Override
	public void onDestroy() {
		Log.e("<<MyService1-onDestroy>>", "I am dead-1");
	}

	/*
	 * public void showNotification() { NotificationManager manager =
	 * (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	 * 
	 * Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	 * PendingIntent pendingIntent = PendingIntent.getActivity(
	 * getApplicationContext(), 0, intent, 0);
	 * 
	 * Notification.Builder builder = new Notification.Builder(
	 * getApplicationContext()); builder.setContentTitle("Title here");
	 * builder.setTicker("Ticker text"); builder.setContentText("Content text");
	 * //builder.setSmallIcon(R.drawable.ic_launcher);
	 * builder.setContentIntent(pendingIntent);
	 * //builder.setFullScreenIntent(pendingIntent, false); manager.notify(1,
	 * builder.getNotification()); }
	 */

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			destLat = bundle.getDouble("Destination Latitude");
			destLon = bundle.getDouble("Destination Longitude");

			if (destLat != 0 && destLon != 0) {
				Log.d("Service data", destLat + "," + destLon);
			}
		}
	}
}
