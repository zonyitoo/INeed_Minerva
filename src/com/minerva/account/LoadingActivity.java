package com.minerva.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.minerva.R;
import com.minerva.core.INeedApplication;
import com.minerva.core.MainActivity;
import com.minerva.utils.UserDBHelper;
import com.minerva.utils.UtilConstant;

public class LoadingActivity extends Activity{
	
	SharedPreferences userPrefs;
	String user_name;
	long user_id;
	String user_pwd;
	
	ConnectivityManager conMan;
	
	Animation loginAllMovup;
	Animation loginBoxShowup;
	Animation regButtonMovup;
	
	LinearLayout regPanel;
	LinearLayout loginAll;
	LinearLayout loginBox;
	EditText edit_name;
	EditText edit_pwd;
	Button button_login;
	Button button_reg;
	
	boolean showLoginPanel;
	ProgressDialog loginProgress;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        
        userPrefs = getSharedPreferences(UtilConstant.LOGGED_USER_PREFS, MODE_PRIVATE);
        
        loginAllMovup = AnimationUtils.loadAnimation(this, R.anim.loading_loginall_movup);
        loginBoxShowup = AnimationUtils.loadAnimation(this, R.anim.loading_loginbox_showup);
        regButtonMovup = AnimationUtils.loadAnimation(this, R.anim.loading_reg_movup);
        
        loginAll = (LinearLayout) findViewById(R.id.linearlayout_loadingactivity_centerall);
        loginBox = (LinearLayout) findViewById(R.id.linearlayout_loadingactivity_loginbox);
        regPanel = (LinearLayout) findViewById(R.id.linearlayout_loadingactivity_reg);
        edit_name = (EditText) findViewById(R.id.edittext_loadingactivity_name);
        edit_pwd = (EditText) findViewById(R.id.edittext_loadingactivity_passwd);
        button_login = (Button) findViewById(R.id.button_loadingactivity_login);
        button_reg = (Button) findViewById(R.id.button_loadingactivity_reg);
        
        setLoginListener();
        setRegListener();
        
        showLoginPanel = false;
        loginProgress = new ProgressDialog(this);
        loginProgress.setMessage("Connecting to Server");
        loginProgress.setCancelable(false);
        
        conMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        user_id = userPrefs.getLong(UtilConstant.PREFS_USER_ID, -1);
        
        if (user_id != -1) {
        	user_name = userPrefs.getString(UtilConstant.PREFS_USER_NAME, "");
        	user_pwd = userPrefs.getString(UtilConstant.PREFS_USER_PWD, "");
        	
        	//new LoginThread().start();
        	//loginProgress.show();
			new LoginServer().execute(null);
        }
        else {
        	showLoginBox();
        }
        /*&
        if (conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().getState() == State.CONNECTED)
        	new LoadingThread().start();
        else {
        	Toast.makeText(this, "NO ACTIVE NETWORK", Toast.LENGTH_LONG).show();
        	((INeedApplication) getApplication()).setLoginState(false);
        	if (user_id == -1) {
        		startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        	}
        	else {
        		user_name = userPrefs.getString(UtilConstant.PREFS_USER_NAME, "");
        		startActivity(new Intent(LoadingActivity.this, MainActivity.class)
    				.putExtra(UtilConstant.PREFS_USER_NAME, user_name)
    				.putExtra(UtilConstant.PREFS_USER_ID, user_id));
        		finish();
        	}
        }
        */
    }

	@Override
	protected void onStop() {
		super.onStop();
		
		//startActivity(new Intent(this, MainActivity.class));
	}
	
	private void showLoginBox() {
		showLoginPanel = true;
		
		loginAll.startAnimation(loginAllMovup);
    	loginBox.startAnimation(loginBoxShowup);
    	regPanel.startAnimation(regButtonMovup);
    	loginAll.setPadding(0, 0, 0, 0);
    	loginBox.setVisibility(LinearLayout.VISIBLE);
    	regPanel.setVisibility(LinearLayout.VISIBLE);
	}
	
	private void setLoginListener() {
		button_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String getname = edit_name.getText().toString();
				getname.trim();
				if (getname.isEmpty()) {
					Toast.makeText(LoadingActivity.this, "Please Input name", Toast.LENGTH_SHORT).show();
					edit_name.setText("");
					edit_pwd.setText("");
					return ;
				}
				String getpwd = edit_pwd.getText().toString();
				if (getpwd.isEmpty()) {
					Toast.makeText(LoadingActivity.this, "Please Input password", Toast.LENGTH_SHORT).show();
					edit_pwd.setText("");
					
					edit_name.setFocusable(true);
					edit_pwd.setFocusable(true);
					button_login.setFocusable(true);
					return ;
				}
				
				if (conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().getState() == State.CONNECTED) {
					user_name = getname;
					user_pwd = getpwd;
					
					//new LoginThread().start();
					loginProgress.show();
					new LoginServer().execute(null);
				}
				else {
					Toast.makeText(LoadingActivity.this, "NO ACTIVE NETWORK", Toast.LENGTH_LONG).show();
					startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
				}
			}
			
		});
	}
	
	private void setRegListener() {
		button_reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoadingActivity.this, RegActivity.class));
				//LoadingActivity.this.finish();
			}
			
		});
	}
	
	class LoginServer extends AsyncTask<String, Integer, String> {
		
		URL loginURL;
		
		@Override
		protected String doInBackground(String... params) {
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
		     	//e.printStackTrace();
		    } catch (IOException e) {
		     	Toast.makeText(LoadingActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		     			//e.printStackTrace();
		    }
		        	
		    if (respCode == UtilConstant.RESPONSE_SUCCEED_CODE) {
		        	// Some code getting user_data from server
		        	// As the photo, name, and also..
		        	// just for updating the database.
		        	
		    	((INeedApplication) getApplication()).setLoginState(true);
		        	
		        	// New User Login, first open the software
		        	// Or exit and then relogin
		        if (user_id == -1) {
		        	Cursor cursor = ((INeedApplication) getApplication()).getUserFromDatabaseByName(user_name);
		        	if (cursor == null) {
		        		((INeedApplication) getApplication()).addNewUser(user_name, "", "", "", "", "", 0);
		        	}
		        	// Relogin, the user is in the database
		        	else {
		        		user_id = cursor.getLong(cursor.getColumnIndexOrThrow(UserDBHelper.C_ID));
		        	}
		        }
		        	
		        startActivity(new Intent(LoadingActivity.this, MainActivity.class)
		        	.putExtra(UtilConstant.PREFS_USER_NAME, user_name)
		        	.putExtra(UtilConstant.PREFS_USER_ID, user_id));
		        LoadingActivity.this.finish();
		    }
		    else {
		        Toast.makeText(LoadingActivity.this, R.string.loading_login_err, Toast.LENGTH_SHORT).show();
		        showLoginBox();
		        //startActivity(new Intent(LoadingActivity.this, RegActivity.class));
		    }
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d(UtilConstant.DEBUG_TAG, "LoadingProgress AsyncTask onPostExecute");
			if (loginProgress.isShowing())
				loginProgress.dismiss();
		}
		
	}
    /*
    class LoginThread extends Thread {

    	URL loginURL;
    	
		@Override
		public void run() {
	        	
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
	     		//e.printStackTrace();
	     	} catch (IOException e) {
	     		Toast.makeText(LoadingActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
	     			//e.printStackTrace();
	     	}
	        	
	        if (respCode == UtilConstant.RESPONSE_SUCCEED_CODE) {
	        	// Some code getting user_data from server
	        	// As the photo, name, and also..
	        	// just for updating the database.
	        	
	        	((INeedApplication) getApplication()).setLoginState(true);
	        	
	        	// New User Login, first open the software
	        	// Or exit and then relogin
	        	if (user_id == -1) {
	        		Cursor cursor = ((INeedApplication) getApplication()).getUserFromDatabaseByName(user_name);
	        		if (cursor == null) {
	        			((INeedApplication) getApplication()).addNewUser(user_name, "", "", "", "", "", 0);
	        		}
	        		// Relogin, the user is in the database
	        		else {
	        			user_id = cursor.getLong(cursor.getColumnIndexOrThrow(UserDBHelper.C_ID));
	        		}
	        	}
	        	
	        	startActivity(new Intent(LoadingActivity.this, MainActivity.class)
	        		.putExtra(UtilConstant.PREFS_USER_NAME, user_name)
	        		.putExtra(UtilConstant.PREFS_USER_ID, user_id));
	        	LoadingActivity.this.finish();
	        }
	        else {
	        	Toast.makeText(LoadingActivity.this, R.string.loading_login_err, Toast.LENGTH_SHORT).show();
	        	//startActivity(new Intent(LoadingActivity.this, RegActivity.class));
	        }
		}
    	
    }
    */
}