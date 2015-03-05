package com.vishnu.personalguardian.activity;

import com.vishnu.personalguardian.R;
import android.app.Activity;
import android.os.Bundle;
/**
 * The ACTIVITY of the application to show about the author and contact email in dialog
 * 
 * @author Vishnuvathsasarma
 *
 */
public class AboutAuthor extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_author);
		setTitle("Author");
	}
}
