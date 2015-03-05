package com.vishnu.personalguardian.test;

import com.robotium.solo.Solo;
import com.vishnu.personalguardian.activity.DestinationList;
import com.vishnu.personalguardian.activity.Main;
import com.vishnu.personalguardian.activity.Map;
import com.vishnu.personalguardian.activity.Splash;
import com.vishnu.personalguardian.activity.Tracking;

import android.test.ActivityInstrumentationTestCase2;

public class MapTest extends ActivityInstrumentationTestCase2<Splash> {

private Solo solo;
	
	public MapTest() {
		super(Splash.class);
	}
	
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
	
	public void testMapAppeared1() throws Exception {
		solo.waitForActivity(Main.class);
		solo.clickOnButton(0);	//opens google map
		boolean isMapStarted = solo
				.waitForActivity(Map.class);
		assertTrue("Map activity appeared",
				isMapStarted);
	}
	
	public void testMapAppeared2() throws Exception {
		solo.waitForActivity(Main.class);
		solo.clickLongOnView(solo.getView(0x7f090057));	//opens google map
		boolean isMapStarted = solo
				.waitForActivity(Map.class);
		assertTrue("Map activity appeared",
				isMapStarted);
	}
	
	public void testMapAppeared3() throws Exception {
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
