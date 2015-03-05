package com.vishnu.personalguardian.test;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.vishnu.personalguardian.activity.DestinationList;
import com.vishnu.personalguardian.activity.Main;
import com.vishnu.personalguardian.activity.Map;
import com.vishnu.personalguardian.activity.Setting;
import com.vishnu.personalguardian.activity.Splash;
import com.vishnu.personalguardian.activity.Tracking;

public class MainTest extends ActivityInstrumentationTestCase2<Splash> {

	private Solo solo;

	public MainTest() {
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

	public void testMainActivity() throws Exception {
		solo.unlockScreen();
		boolean isMainAppeared = solo.waitForActivity(Main.class);
		assertTrue("Main activity launched", isMainAppeared);
	}

	public void testEmptySearch() throws Exception {
		solo.waitForActivity(Main.class);
		solo.clearEditText(0);
		solo.clickOnButton(1);
		boolean isToastAppeared = solo.searchText("Please enter a destination");
		assertTrue("Empty search toast appeared", isToastAppeared);
	}

	public void testSearch() throws Exception {
		solo.waitForActivity(Main.class);
		solo.enterText(0, "Crow island");
		solo.clickOnButton(1); // Search
		boolean isDestinationListStarted = solo
				.waitForActivity(DestinationList.class);
		assertTrue("DestinationList activity appeared",
				isDestinationListStarted);
	}

	public void testMarker() throws Exception {
		solo.waitForActivity(Main.class);
		solo.clickOnButton(0);	//opens google map
		boolean isMapStarted = solo
				.waitForActivity(Map.class);
		assertTrue("Map activity appeared",
				isMapStarted);
	}
	
	public void testLongClick() throws Exception {
		solo.waitForActivity(Main.class);
		solo.clickLongOnView(solo.getView(0x7f090057));	//opens google map
		boolean isMapStarted = solo
				.waitForActivity(Map.class);
		assertTrue("Map activity appeared",
				isMapStarted);
	}
	
	public void testSetting() throws Exception {
		solo.waitForActivity(Main.class);
		solo.clickOnMenuItem("Settings");
		solo.waitForDialogToOpen();
		solo.clearEditText(0);
		solo.enterText(0, Main.conf.getPassword());	//this is a password
		solo.clickOnButton(1);
		boolean isSettingStarted = solo
				.waitForActivity(Setting.class);
		assertTrue("Setting activity appeared",
				isSettingStarted);
	}
	
	public void testAuthor() throws Exception {
		solo.waitForActivity(Main.class);
		solo.clickOnMenuItem("Author");
		solo.waitForDialogToOpen();
		solo.goBack();
		solo.waitForDialogToClose();
	}

	public void tsetBack() throws Exception {
		solo.waitForActivity(Main.class);
		solo.goBack();
		solo.waitForDialogToOpen();
		solo.clickOnButton(0);
	}
	
	public void testExit() throws Exception {
		solo.waitForActivity(Main.class);
		solo.goBack();
		solo.waitForDialogToOpen();
		solo.clickOnButton(1);
		solo.waitForDialogToClose();
		solo.waitForDialogToOpen();
		solo.clearEditText(0);
		solo.enterText(0, "a");
		solo.clickOnButton(1);
		boolean isToastAppeared = solo.searchText("Password Incorrect");
		assertTrue("Empty search toast appeared", isToastAppeared);
		solo.waitForDialogToClose();
		solo.waitForDialogToOpen();
		solo.clearEditText(0);
		solo.enterText(0, Main.conf.getPassword());
		solo.clickOnButton(1);
		solo.waitForDialogToClose();
	}
}