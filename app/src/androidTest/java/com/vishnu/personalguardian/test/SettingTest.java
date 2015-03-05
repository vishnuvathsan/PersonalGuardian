package com.vishnu.personalguardian.test;

import com.robotium.solo.Solo;
import com.vishnu.personalguardian.activity.Main;
import com.vishnu.personalguardian.activity.Setting;
import com.vishnu.personalguardian.activity.Splash;

import android.test.ActivityInstrumentationTestCase2;

public class SettingTest extends ActivityInstrumentationTestCase2<Splash> {

	private Solo solo;
	
	public SettingTest() {
		super(Splash.class);
	}
	
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	public void testSetting1() throws Exception {
		//invalid password
		solo.waitForActivity(Main.class);
		solo.clickOnMenuItem("Settings");
		solo.waitForDialogToOpen();
		solo.clearEditText(0);
		solo.enterText(0, "");	//this is a password
		solo.clickOnButton(1);
		boolean isSettingStarted = solo
				.waitForActivity(Setting.class);
		assertTrue("Setting activity appeared",
				isSettingStarted);
	}
	
	public void testSetting2() throws Exception {
		//with correct password
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
	
	public void testEmptyName() throws Exception {
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
		solo.clearEditText(0);
		solo.clickOnButton(0);
		boolean isToastAppeared = solo.searchText("Name should be atleast 3 charactors");
		assertTrue("Invalid name toast appeared", isToastAppeared);
		boolean isToastAppeared2 = solo.searchText("Unable to save.\nTry again later");
		assertTrue("Not saved toast appeared", isToastAppeared2);
	}
	
	public void testName() throws Exception {
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
		solo.clearEditText(0);
		solo.enterText(0, "Vishnu");
		solo.clickOnButton(0);
		boolean isToastAppeared = solo.searchText("Saved successfully");
		assertTrue("Saved toast appeared", isToastAppeared);
	}
	
	public void testEmptyPhone() throws Exception {
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
		solo.clearEditText(1);
		solo.clickOnButton(0);
		boolean isToastAppeared = solo.searchText("Phone number invalid");
		assertTrue("Invalid phone toast appeared", isToastAppeared);
		boolean isToastAppeared2 = solo.searchText("Unable to save.\nTry again later");
		assertTrue("Not saved toast appeared", isToastAppeared2);
	}
	
	public void testPhone() throws Exception {
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
		solo.clearEditText(1);
		solo.enterText(1, "0755049930");
		solo.clickOnButton(0);
		boolean isToastAppeared = solo.searchText("Saved successfully");
		assertTrue("Saved toast appeared", isToastAppeared);
	}
	
	public void testEmptyPassword() throws Exception {
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
		solo.clearEditText(2);
		solo.clickOnButton(0);
		boolean isToastAppeared = solo.searchText("Password should be atleast 3 charactors");
		assertTrue("Invalid password toast appeared", isToastAppeared);
		boolean isToastAppeared2 = solo.searchText("Unable to save.\nTry again later");
		assertTrue("Not saved toast appeared", isToastAppeared2);
	}
	
	public void testPassword() throws Exception {
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
		solo.clearEditText(2);
		solo.enterText(2, "abc");
		solo.clickOnButton(0);
		boolean isToastAppeared = solo.searchText("Saved successfully");
		assertTrue("Saved toast appeared", isToastAppeared);
	}
	
	public void testBack() throws Exception {
		//not save data
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
		solo.goBack();
		solo.waitForDialogToOpen();
		solo.clickOnButton(0);
	}
	
	public void testBack2() throws Exception {
		//save data
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
		solo.goBack();
		solo.waitForDialogToOpen();
		solo.clickOnButton(1);
		solo.waitForDialogToClose();
		boolean isToastAppeared = solo.searchText("Saved successfully");
		assertTrue("Saved toast appeared", isToastAppeared);
	}
	
	public void testSave() throws Exception {
		//save data
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
		solo.clickOnButton(0);
		boolean isToastAppeared = solo.searchText("Saved successfully");
		assertTrue("Saved toast appeared", isToastAppeared);
	}
	
	public void testSave2() throws Exception {
		//save data
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
		solo.clearEditText(0);
		solo.enterText(0, "Vishnu");
		solo.clearEditText(1);
		solo.enterText(1, "0755049930");
		solo.clearEditText(2);
		solo.enterText(2, "abc");
		solo.clickOnButton(0);
		boolean isToastAppeared = solo.searchText("Saved successfully");
		assertTrue("Saved toast appeared", isToastAppeared);
	}
}
