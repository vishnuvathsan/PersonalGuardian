package com.vishnu.personalguardian.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.vishnu.personalguardian.R;

/**
 * The ACTIVITY of the application to show Wikipedia result in dialog
 * 
 * @author Vishnuvathsasarma
 *
 */
public class WikiResult extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			String description = bundle.getString("Description");
			TextView tv_wiki = (TextView) findViewById(R.id.tv_result);
			tv_wiki.setText(description);
			setTitle("Wikipedia");
		}
	}
}
