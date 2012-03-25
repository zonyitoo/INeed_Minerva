package com.minerva.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.minerva.R;
import com.minerva.core.MainActivity;
import com.minerva.utils.UtilConstant;

public class LoadingActivity extends Activity{
	
	SharedPreferences userPrefs;
	String user_name;
	long user_id;
	String user_pwd;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        
        userPrefs = getSharedPreferences(UtilConstant.LOGGED_USER_PREFS, MODE_PRIVATE);
        
        new LoadingThread().start();
    }

	@Override
	protected void onStop() {
		super.onStop();
		
		//startActivity(new Intent(this, MainActivity.class));
	}
    
    class LoadingThread extends Thread {

    	URL loginURL;
    	
		@Override
		public void run() {
	        user_id = userPrefs.getLong(UtilConstant.PREFS_USER_ID, -1);
	        if (user_id == -1) {
	        	startActivity(new Intent(LoadingActivity.this, RegActivity.class));
	        }
	        else {
	        	user_name = userPrefs.getString(UtilConstant.PREFS_USER_NAME, "");
	        	user_pwd = userPrefs.getString(UtilConstant.PREFS_USER_PWD, "");
	        	
	        	// Login server
	        	
	        	PrintWriter writer = null;
	        	int respCode = 0;
	        	try {
	     			loginURL = new URL(UtilConstant.SERVER_URL + "/login/");
	     			HttpURLConnection connection = (HttpURLConnection) loginURL.openConnection();
	     			
	     			connection.setRequestMethod("POST");
	     			connection.setDoInput(true);
	     			connection.setDoOutput(true);
	     			
	     			writer = new PrintWriter(connection.getOutputStream());
	     			
	     			writer.print(user_name + "&" + user_pwd);
	     			writer.flush();
	     			writer.close();
	     			
	     			respCode = connection.getResponseCode();
	     			Log.d(UtilConstant.DEBUG_TAG, "respCode=" + respCode);
	        	} catch (MalformedURLException e) {
	     			// TODO Auto-generated catch block
	     			e.printStackTrace();
	     		} catch (IOException e) {
	     			// TODO Auto-generated catch block
	     			e.printStackTrace();
	     		}
	        	
	        	if (respCode == UtilConstant.RESPONSE_SUCCEED_CODE) {
	        		startActivity(new Intent(LoadingActivity.this, MainActivity.class)
	        			.putExtra(UtilConstant.PREFS_USER_NAME, user_name)
	        			.putExtra(UtilConstant.PREFS_USER_ID, user_id));
	        	}
	        	else {
	        		Toast.makeText(LoadingActivity.this, R.string.loading_login_err, Toast.LENGTH_SHORT).show();
	        		startActivity(new Intent(LoadingActivity.this, RegActivity.class));
	        	}
	        	
	        }
	        LoadingActivity.this.finish();
		}
    	
    }
}