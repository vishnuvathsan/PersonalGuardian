package com.vishnu.personalguardian.activity;

import com.vishnu.personalguardian.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;


//temp class
public class Pref extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
}
