package com.vishnu.personalguardian.activity;

import com.vishnu.personalguardian.R;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author Vishnuvathsasarma
 *
 */
public class Setting extends ActionBarActivity implements
		OnClickListener {

	private EditText etxtName, etxtPhone;
	private Button btnSave;
	boolean isSaved = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		etxtName = (EditText) findViewById(R.id.etxtName);
		etxtPhone = (EditText) findViewById(R.id.etxtPhone);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnSave:
			isSaved = false;
			String name = etxtName.getText().toString();
			String phone = etxtPhone.getText().toString();
			// save
			isSaved = true;
			Toast.makeText(this, "Successfully saved the data...",
					Toast.LENGTH_SHORT).show();
			break;
		}
		if (isSaved) {
			onBackPressed();
		}
	}

	@Override
	public void onBackPressed() {
		if (isSaved) {
			this.finish();
		} else {
			Toast.makeText(this, "Please save the data", Toast.LENGTH_SHORT)
					.show();
		}
	}
}
