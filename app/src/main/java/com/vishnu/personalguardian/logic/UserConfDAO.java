package com.vishnu.personalguardian.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.widget.Toast;

public class UserConfDAO {	

	private final String FILENAME;
	private UserConfiguration conf;
	private boolean isSaved;
	
	private static UserConfDAO instance;

    public static UserConfDAO getInstance() {
        if (instance == null) {
            instance = new UserConfDAO();
        }
        return instance;
    }

    private UserConfDAO() {
    	conf = new UserConfiguration();
    	isSaved = false;
    	FILENAME = "conf";
    }

	public UserConfiguration loadConfiguration(Context context) {
		// read configuration data object from file
		FileInputStream fis;
		ObjectInputStream ois;
		try {
			fis = context.openFileInput(FILENAME);
			ois = new ObjectInputStream(fis);
			conf = (UserConfiguration) ois.readObject();
			ois.close();
			fis.close();
			
		} catch (FileNotFoundException e) {
			// user needs to provide initial settings on first run of app
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conf;
	}
	
	public boolean saveConfiguration(Context context, UserConfiguration data) {
		// save the configuration setting as object in a file
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.close();
			fos.close();
			isSaved = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSaved = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSaved = false;
		}
		return isSaved;
	}
}
