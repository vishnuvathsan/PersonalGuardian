package com.vishnu.personalguardian.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vishnu.personalguardian.R;
import com.vishnu.personalguardian.logic.UserConfDAO;
import com.vishnu.personalguardian.logic.UserConfiguration;

/**
 * The SETTING ACTIVITY of the application to set/change guardian detail and set/change password
 * 
 * @author Vishnuvathsasarma
 *
 */
public class Setting extends ActionBarActivity implements OnClickListener {

	private UserConfiguration conf;
	private EditText etxtName, etxtPhone, etxtPassword;
	private Button btnSave;
	boolean isSaved = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		conf = new UserConfiguration();
		
		etxtName = (EditText) findViewById(R.id.etxtName);
		etxtPhone = (EditText) findViewById(R.id.etxtPhone);
		etxtPassword = (EditText) findViewById(R.id.etxtPassword);
		
		etxtName.setSelectAllOnFocus(true);
		etxtPhone.setSelectAllOnFocus(true);
		etxtPassword.setSelectAllOnFocus(true);
		
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);

		UserConfiguration data = UserConfDAO.getInstance().loadConfiguration(
				this);
		if (data != null) {
			conf = data;

			if (conf.getPassword() != null) {
				showPasswordDialog();
			}
		}
	}

	private void showPasswordDialog() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		final View loginView = inflater.inflate(
				R.layout.alert_activity_password, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(loginView)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						EditText etxtPwd = (EditText) loginView
								.findViewById(R.id.etxtAlertPassword);
						String password = etxtPwd.getText().toString();
						Log.i("Pwd", password);
						if (password.equalsIgnoreCase(conf.getPassword())) {
							updateFields();
							Log.i("Pwd22", conf.getPassword());
						} else {
							showPasswordDialog();
							Toast.makeText(Setting.this, "Password Incorrect",
									Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// closes the activity
								Setting.this.finish();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnSave:
			getUserInputs();
			// isSaved = deleteFile("conf");
			if (isSaved) {
				Toast.makeText(Setting.this, "Saved successfully", Toast.LENGTH_SHORT).show();
				Main.conf = conf;
				Setting.this.finish();
			} else {
				Toast.makeText(this, "Unable to save.\nTry again later", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private void getUserInputs() {
		String name = etxtName.getText().toString();
		String phone = etxtPhone.getText().toString();
		String passwrd = etxtPassword.getText().toString();
		// get data from inputs and store it in UserConfiguration object

		Log.i("name", name);
		Log.i("phone", phone);
		Log.i("passwrd", passwrd);

		if (name.length() > 2) {
			conf.setGuardianName(name);
		} else {
			Toast.makeText(this, "Name should be atleast 3 charactors", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (phone.length() >= 9 && phone.length() <= 14) {
			conf.setGuardianPhone(phone);
		} else {
			Toast.makeText(this, "Phone number invalid", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (passwrd.length() >= 3) {
			conf.setPassword(passwrd);
		} else {
			Toast.makeText(this, "Password should be atleast 3 charactors", Toast.LENGTH_SHORT).show();
			return;
		}
		// save
		isSaved = UserConfDAO.getInstance().saveConfiguration(this, conf);
	}

	private void updateFields() {
		// used to update the data fields after reading configuration setting
		// from file
		if (conf != null) {
			etxtName.setText(conf.getGuardianName());
			etxtPhone.setText(conf.getGuardianPhone());
			etxtPassword.setText(conf.getPassword());
		}
	}

	@Override
	public void onBackPressed() {
		if (isSaved) {
			this.finish();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"You have not saved your configurations settings.\nDo you want to save the settings?")
					.setTitle("Configurations not saved")
					.setCancelable(true)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// this saves the data
									getUserInputs();
									if (isSaved) {
										Toast.makeText(Setting.this, "Saved successfully", Toast.LENGTH_SHORT).show();
										Main.conf = conf;
										Setting.this.finish();
									} else {
										Toast.makeText(Setting.this, "Unable to save.\nTry again later", Toast.LENGTH_SHORT).show();
									}
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// closes the activity
									Setting.this.finish();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.finish();
	}
	
}
