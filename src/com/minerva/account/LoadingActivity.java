package com.minerva.account;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import com.minerva.R;
import com.minerva.core.MainActivity;
import com.minerva.utils.UtilConstant;

public class LoadingActivity extends Activity{
	
	SharedPreferences userPrefs;
	String user_name;
	long user_id;
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

		@Override
		public void run() {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	        user_id = userPrefs.getLong(UtilConstant.PREFS_USER_ID, -1);
	        if (user_id == -1) {
	        	startActivity(new Intent(LoadingActivity.this, RegActivity.class));
	        }
	        else {
	        	user_name = userPrefs.getString(UtilConstant.PREFS_USER_NAME, "");
	        	startActivity(new Intent(LoadingActivity.this, MainActivity.class)
	        		.putExtra(UtilConstant.PREFS_USER_NAME, user_name)
	        		.putExtra(UtilConstant.PREFS_USER_ID, user_id));
	        }
	        
	        LoadingActivity.this.finish();
		}
    	
    }
}