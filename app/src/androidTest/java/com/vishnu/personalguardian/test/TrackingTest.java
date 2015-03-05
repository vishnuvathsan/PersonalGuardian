package com.vishnu.personalguardian.test;

import com.robotium.solo.Solo;
import com.vishnu.personalguardian.activity.DestinationList;
import com.vishnu.personalguardian.activity.Main;
import com.vishnu.personalguardian.activity.Splash;
import com.vishnu.personalguardian.activity.Tracking;

import android.test.ActivityInstrumentationTestCase2;

public class TrackingTest extends ActivityInstrumentationTestCase2<Splash> {

	private Solo solo;
	
	public TrackingTest() {
		super(Splash.class);
	}
	
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	public void testTracking() throws Exception {
		solo.waitForActivity(Main.class);
		solo.enterText(0, "Crow island");
		solo.clickOnButton(1); // Search
		solo.waitForActivity(DestinationList.class);
		solo.clickInList(0);
		solo.waitForDialogToOpen();
		solo.clickOnButton(0); // Cancel
		solo.waitForDialogToClose();
		boolean isTrackingStarted = solo.waitForActivity(Tracking.class);
		assertTrue("Tracking activity appeared", isTrackingStarted);
	}
	
//	public void testWeather() throws Exception {
//		solo.waitForActivity(Main.class);
//		solo.enterText(0, "Crow island");
//		solo.clickOnButton(1); // Search
//		solo.waitForActivity(DestinationList.class);
//		solo.clickInList(0);
//		solo.waitForDialogToOpen();
//		solo.clickOnButton(0); // Cancel
//		solo.waitForDialogToClose();
//		solo.waitForActivity(Tracking.class);
//		solo.clickOnMenuItem("Author");
//		solo.waitForDialogToOpen();
//		solo.goBack();
//		solo.waitForDialogToClose();
//	}
}
