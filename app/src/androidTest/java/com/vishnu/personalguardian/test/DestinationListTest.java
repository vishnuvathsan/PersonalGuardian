package com.vishnu.personalguardian.test;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.vishnu.personalguardian.activity.DestinationList;
import com.vishnu.personalguardian.activity.Main;
import com.vishnu.personalguardian.activity.Map;
import com.vishnu.personalguardian.activity.Splash;
import com.vishnu.personalguardian.activity.Tracking;
import com.vishnu.personalguardian.activity.WikiData;

public class DestinationListTest extends ActivityInstrumentationTestCase2<Splash> {

	private Solo solo;

	public DestinationListTest() {
		super(Splash.class);

	}

	@Override
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		// tearDown() is run after a test case has finished.
		// finishOpenedActivities() will finish all the activities that have
		// been opened during the test execution.
		solo.finishOpenedActivities();
	}

	public void testListItem() throws Exception {
		//test to open tracking activity
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
	
	public void testListItem2() throws Exception {
		//test to open map activity
		solo.waitForActivity(Main.class);
		solo.enterText(0, "Crow island");
		solo.clickOnButton(1); // Search
		solo.waitForActivity(DestinationList.class);
		solo.clickInList(0);
		solo.waitForDialogToOpen();
		solo.clickOnButton(1); // Cancel
		solo.waitForDialogToClose();
		boolean isMapStarted = solo.waitForActivity(Map.class);
		assertTrue("Map activity appeared", isMapStarted);
	}
}