package com.vishnu.personalguardian.logic;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.vishnu.personalguardian.R;
import com.vishnu.personalguardian.activity.Main;
import com.vishnu.personalguardian.activity.PasswordAuthenticator;
import com.vishnu.personalguardian.activity.Splash;

import java.util.ArrayList;
import java.util.List;

import static com.vishnu.personalguardian.R.drawable.logo;

/**
 * The TRACKING SERVICE of the application to track and alert
 * 
 * @author Vishnuvathsasarma
 *
 */
public class TrackingService extends IntentService {

	public static boolean isDestinationReached = false;
	public static boolean isMsgSent = false;
	public static int deviationCount = 0;
    public static CountDownTimer smsCancelTimer;
    // The minimum distance to change location Updates in meters
	private final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 50;
	// The minimum time between location updates in milliseconds
	private final long MIN_TIME_BW_UPDATES = 30000;
	// maximum number of occurrence of target deviation
	private final int MAX_DEVIATION_OCCURANCE = 4;
	// minimum distance in kilometer to consider a deviation in target
	private final double MIN_DISTANCE_TO_DETECT_DEVIATION = 0.05;
	private final double MIN_DISTANCE_VALUE_FOR_DESTINATION_REACHED = 0.2; // kilometer
	private final double MIN_TRAVELLING_SPEED = 20.00; // kilometer per hour
	private final int MIN_COUNT_TO_ACHIEVE_AVERAGE_SPEED = 20;
	// time in milliseconds to cancel a SMS sending
	private final long MAX_TIME_TO_CANCEL_SMS = 120000;
    private XMLreaderDistance distanceReader;
    private String currGeocode, destinationGeocode, URL;
    private boolean isMsgDelivered = false;
    private double currDistance, currTravelledDist, directDistance; // kilometer
    private double currSpeed; // kilometer per hour
    private long currTravelledDuration; // hours
    // array list to store the path travelled which can be used to trace back
    // the path
    private List<MyLocation> locationTraces;
    private Location location;
    private LocationReceiver locationReceiver;
    private int count = 0;
    private String number;
	private Intent intent;

	private double destLat = 0.00, destLon = 0.00;

	public TrackingService() {
		super("Tracking service");
		// TODO Auto-generated constructor stub
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		number = Main.conf.getGuardianPhone();
		Log.i("Phone", number);
		locationTraces = new ArrayList<MyLocation>();
		locationReceiver = new LocationReceiver(this,
				MIN_DISTANCE_CHANGE_FOR_UPDATES, MIN_TIME_BW_UPDATES);

		smsCancelTimer = new CountDownTimer(MAX_TIME_TO_CANCEL_SMS, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				Log.i("CountdownTick", millisUntilFinished / 1000 + "s");
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				Log.i("CountdownFinish", "s");
				sendMsg("Current Location:\nLat: " + location.getLatitude()
						+ "\nLong: " + location.getLongitude()
						+ "\nDestination:\nLat: " + destLat + "\nLong: "
						+ destLon);
				isMsgSent = true;

				NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.cancel(0);
				Log.i("SMS", "SENT");
				showNotification("SMS sent", false, false, false);
				Log.i("NOTI", "SHOWN");
				
				deviationCount = 0;
			}
		};
	}

	@Override
	public void onStart(Intent intent, int startId) {
		onHandleIntent(intent);
		Log.e("<<Tracking-onStart>>", "I am alive");

		// tracking activity starts here
		locationReceiver
				.setOnLocationChangedListener(new OnLocationChangedListener() {

					@Override
					public void onLocationChanged(Location loc) {
						/*
						 * this method invoked on location change HTTP request
						 * will be sent to Google's Distance Matrix API and
						 * updated distance to destination will be retrieved
						 */
						location = loc;
						currGeocode = location.getLatitude() + ","
								+ location.getLongitude();

						URL = "http://maps.googleapis.com/maps/api/distancematrix/xml?origins="
								+ currGeocode
								+ "&destinations="
								+ destinationGeocode;
						distanceReader = new XMLreaderDistance();
						new XmlReader().execute(URL);
					}
				});
		locationReceiver.start();
	}

	private double getDistance(Location location, double destLat2,
			double destLon2) {
		// method to get the direct distance to destination
		Location loc = new Location(LOCATION_SERVICE);
		loc.setLatitude(destLat2);
		loc.setLongitude(destLon2);
		double dist = location.distanceTo(loc) / 1000;
		dist = Double.parseDouble(String.format("%.3f", dist)); 
		// rounded to 3 decimal point
		
		return dist; // returns in kilometer
	}

	private void sendMsg(String msg) {
		String SENT = "Message Sent";
		String DELIVERED = "Message Delivered";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();
					isMsgSent = true;
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic Failure",
							Toast.LENGTH_SHORT).show();
					isMsgSent = false;
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					isMsgSent = false;
					break;
				}
			}
		}, new IntentFilter(SENT));

		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					isMsgDelivered = true;
					isMsgSent = true;
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					isMsgDelivered = false;
					isMsgSent = false;
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager smsMgr = SmsManager.getDefault();
		smsMgr.sendTextMessage(number, null,
				msg.length() < 160 ? msg : msg.substring(0, 160), sentPI,
				deliveredPI);
		// divide and sends the rest of the message if the message size is larger for SMS service
		if (msg.length() >= 160) {
			sendMsg(msg.substring(160));
		}
	}

	@Override
	public void onDestroy() {
		Log.e("<<Tracking-onDestroy>>", "I am dead");
		locationReceiver.stop();
		smsCancelTimer.cancel();
		if (!isDestinationReached) {
			// restart the service if the user didn't reach the destination
			restartService();
		}
	}

	private void restartService() {
		// code requesting the user to restart the service if interrupted before reaching the destination
		showNotification("Your tracking service stopped", true, false, true);
		vibrateDevice();
		smsCancelTimer.start();

		// locationReceiver.start();
	}

	public void showNotification(String text, boolean sendSMS,
			boolean lowSpeed, boolean restart) {
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this);

        Notification notification = new Notification(logo,
                "Personal Guardian", System.currentTimeMillis());
		notification.flags = Notification.FLAG_SHOW_LIGHTS
				| Notification.FLAG_AUTO_CANCEL;
		notification.ledARGB = Color.MAGENTA;
		notification.ledOffMS = 3000;
		notification.ledOnMS = 500;
		notification.tickerText = "State change in personal guardian";

        if (sendSMS && restart) {
            //notification asking to restart service and send SMS if not restarted within 2min
			Intent intent2 = new Intent(getApplicationContext(), Splash.class);
			PendingIntent pendingIntent2 = PendingIntent.getActivity(
					getApplicationContext(), 0, intent2, 0);
//			notification.setLatestEventInfo(getApplicationContext(),
//					text,
//					"Tap to restart service", pendingIntent2);
            notification = builder.setContentIntent(pendingIntent2)
                    .setSmallIcon(R.drawable.logo).setTicker(text).setWhen(System.currentTimeMillis())
                    .setAutoCancel(false).setContentTitle("Tap to restart service")
                    .setContentText(text).build();
            manager.notify(3, notification);

			//cancel SMS
			Intent intent3 = new Intent(getApplicationContext(),
					PasswordAuthenticator.class);
			PendingIntent pendingIntent3 = PendingIntent.getActivity(
					getApplicationContext(), 0, intent3, 0);
//			notification.setLatestEventInfo(getApplicationContext(),
//					text,
//					"Tap within " + (MAX_TIME_TO_CANCEL_SMS / 60000) + "min to cancel SMS", pendingIntent3);
            notification = builder.setContentIntent(pendingIntent3)
                    .setSmallIcon(R.drawable.logo).setTicker(text).setWhen(System.currentTimeMillis())
                    .setAutoCancel(false).setContentTitle("Tap within " + (MAX_TIME_TO_CANCEL_SMS / 60000) + "min to cancel SMS")
                    .setContentText(text).build();
            manager.notify(0, notification);
		} else if (sendSMS) {
			//notification to indicate deviation in target and send SMS in 2min if user didn't tap and cancel it
			Intent intent = new Intent(getApplicationContext(),
					PasswordAuthenticator.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(
					getApplicationContext(), 0, intent, 0);
//			notification.setLatestEventInfo(getApplicationContext(),
//					"You are deviating from the destination",
//					text, pendingIntent);
            notification = builder.setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo).setTicker(text).setWhen(System.currentTimeMillis())
                    .setAutoCancel(false).setContentTitle("You are deviating from the destination")
                    .setContentText(text).build();
            manager.notify(0, notification);

		} else if (lowSpeed) {
//			notification.setLatestEventInfo(getApplicationContext(),
//					"Your travelling speed is lower than the average", text,
//					null);
            notification = builder.setContentIntent(null)
                    .setSmallIcon(R.drawable.logo).setTicker(text).setWhen(System.currentTimeMillis())
                    .setAutoCancel(false).setContentTitle("Your travelling speed is lower than the average")
                    .setContentText(text).build();
            manager.notify(1, notification);
		} else {
//			notification.setLatestEventInfo(getApplicationContext(),
//					"Personal Guardian status", text, null);
            notification = builder.setContentIntent(null)
                    .setSmallIcon(R.drawable.logo).setTicker(text).setWhen(System.currentTimeMillis())
                    .setAutoCancel(false).setContentTitle("Personal Guardian status")
                    .setContentText(text).build();
            manager.notify(2, notification);
		}
	}

	public void vibrateDevice() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		long[] pattern = { 500, 500, 500, 500, 500, 500 };
		vibrator.vibrate(pattern, -1);
	}

	@Override
	protected void onHandleIntent(Intent intent1) {
		// TODO Auto-generated method stub
		intent = intent1;
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			destLat = bundle.getDouble("Destination Latitude");
			destLon = bundle.getDouble("Destination Longitude");
			destinationGeocode = destLat + "," + destLon;
		}
	}

	private void updateChangedLocationData() {
		// method to update distance data when new data received after location
		// change
		directDistance = getDistance(location, destLat, destLon);
		showNotification("Distance to travel: " + currDistance + " km", false,
				false, false);
		locationTraces.add(new MyLocation(location.getLatitude(), location
				.getLongitude(), currDistance, System.currentTimeMillis()));

		// Log.i("Time", "" + System.currentTimeMillis());

		if (locationTraces.size() > 1) {
			double differenceToDest = locationTraces.get(count)
					.getDistanceToDest()
					- locationTraces.get(count - 1).getDistanceToDest();
			if (differenceToDest > MIN_DISTANCE_TO_DETECT_DEVIATION) {
				deviationCount++;
                Log.e("Deviation from target", "" + currDistance);
                Log.e("Deviation Count", "" + deviationCount);
				Toast.makeText(
						getApplicationContext(),
						"You are deviating from the target\nDeviation Count: "
								+ deviationCount, Toast.LENGTH_SHORT).show();
			}
			if (deviationCount > MAX_DEVIATION_OCCURANCE) {
				// SMS alert code
				if (!isMsgSent) {
					isMsgSent = true;
					smsCancelTimer.start(); // starts the timer to send SMS
					showNotification("Tap within " + (MAX_TIME_TO_CANCEL_SMS / 60000) + "min to cancel SMS",
							true, false, false);
					vibrateDevice();

					Log.e("ALERT!!!", "Sending message to guardian");
					Toast.makeText(
							getApplicationContext(),
							"ALERT!!!\nSending message to guardian\nCurrent Location:\n\tLat: "
									+ location.getLatitude() + "\n\tLong: "
									+ location.getLongitude()
									+ "\n\nDestination:\n\tLat: " + destLat
									+ "\n\tLong: " + destLon,
							Toast.LENGTH_SHORT).show();
				}
			}

			if (count > MIN_COUNT_TO_ACHIEVE_AVERAGE_SPEED) {
				currTravelledDist = locationTraces.get(0).getDistanceToDest()
						- locationTraces.get(count).getDistanceToDest();
				currTravelledDuration = (locationTraces.get(count).getTime() - locationTraces
						.get(0).getTime()) / 3600000;
				currSpeed = currTravelledDist / currTravelledDuration;

				Log.i("Dist", "" + currTravelledDist);
				Log.i("Time", "" + currTravelledDuration);
				Log.i("Speed", "" + currSpeed);

				if (currSpeed < MIN_TRAVELLING_SPEED) {
					showNotification(
							"Your Speed: " + String.format("%.2f", currSpeed)
									+ "kmph", false, true, false);
					vibrateDevice();
				}
			}
		}

		Log.w("Distance", "" + currDistance);
		Log.w("Count", "" + count);
		Toast.makeText(getApplicationContext(),
				"Current Distance: " + currDistance + "\nCount: " + count,
				Toast.LENGTH_SHORT).show();
		count++;

		// stopping service when destination reached
		if (currDistance < MIN_DISTANCE_VALUE_FOR_DESTINATION_REACHED) {
			locationReceiver.stop();
			smsCancelTimer.cancel();
			showNotification("Destination reached", false, false, false);
			vibrateDevice();
			isDestinationReached = true;
			stopService(intent);
		}
	}

	private class XmlReader extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			String temp = distanceReader.readURL(urls[0]);
			return temp;
		}

		@Override
		protected void onPostExecute(String result) {
			currDistance = Double.parseDouble(result) / 1000;
			updateChangedLocationData();
		}
	}
}
