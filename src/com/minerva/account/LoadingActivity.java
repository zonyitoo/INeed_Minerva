package com.minerva.account;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.minerva.utils.Constants;
import com.minerva.utils.MinervaConnErr;
import com.minerva.utils.Remote;
import com.minerva.utils.UserDBHelper;

public class LoadingActivity extends Activity{
	
	SharedPreferences userPrefs;
	String user_name;
	long user_id;
	String user_pwd;
	String user_consumerkey;
	String user_consumersecret;
	long user_global_id;
	
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
        
        ((INeedApplication) getApplication()).mloadingActivity = this;
        
        userPrefs = getSharedPreferences(Constants.LOGGED_USER_PREFS, MODE_PRIVATE);
        
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
        user_id = userPrefs.getLong(Constants.PREFS_USER_ID, -1);
        
        if (user_id != -1) {
        	user_name = userPrefs.getString(Constants.PREFS_USER_NAME, "");
        	user_pwd = userPrefs.getString(Constants.PREFS_USER_PWD, "");
        	user_consumerkey = userPrefs.getString(Constants.PREFS_USER_CONSUMERKEY, "");
        	user_consumersecret = userPrefs.getString(Constants.PREFS_USER_CONSUMERSECRET, "");
        	user_global_id = userPrefs.getLong(Constants.PREFS_USER_GLOBAL_ID, -1);
        	
        	Intent intent = new Intent(this, MainActivity.class);
        	intent.putExtra(Constants.PREFS_USER_NAME, user_name);
        	intent.putExtra(Constants.PREFS_USER_ID, user_id);
        	intent.putExtra(Constants.PREFS_USER_GLOBAL_ID, user_global_id);
        	intent.putExtra(Constants.PREFS_USER_CONSUMERKEY, user_consumerkey);
        	intent.putExtra(Constants.PREFS_USER_CONSUMERSECRET, user_consumersecret);
        	
        	startActivity(intent);
        	this.finish();
        }
        else {
        	showLoginBox();
        }
    }
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		((INeedApplication) getApplication()).mloadingActivity = null;
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
			
			public void onClick(View v) {
				user_name = edit_name.getText().toString();
				user_name.trim();
				user_pwd = edit_pwd.getText().toString();
				if (user_name.isEmpty() || user_pwd.isEmpty()) {
					Toast.makeText(LoadingActivity.this, "NO INPUT", Toast.LENGTH_LONG).show();
					edit_pwd.setText("");
					return;
				}
				loginProgress.show();
				new LoginServer().execute(user_name, user_pwd);
			}
		});
	}
	
	private void setRegListener() {
		button_reg.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent(LoadingActivity.this, RegActivity.class));
				//LoadingActivity.this.finish();
			}
			
		});
	}
	
	protected synchronized long getUserId(long user_global_id) throws JSONException {
		INeedApplication app = (INeedApplication) getApplication();
		Cursor cursor = app.getUserFromDatabaseByGlobalId(user_global_id);
		JSONObject userprofile = Remote.User.getUserProfile(user_name, user_pwd, user_global_id, user_consumerkey, user_consumersecret);
		userprofile.put(Constants.JSON_USERNAME, user_name);
		Log.d(Constants.DEBUG_TAG, "LoadingActivity getUserId userProfile=" + userprofile);
		long userid = 0;
		if (cursor.getCount() == 0) {
			userid = app.addNewUser(userprofile);
		}
		else {
			cursor.moveToFirst();
			userid = cursor.getLong(cursor.getColumnIndex(UserDBHelper.C_ID));
					
			long numberOfAffectRow = app.modifiedUserDatabase(userprofile, userid);
			Log.d(Constants.DEBUG_TAG, "LoadingActivity getUserId numberOfAffectRow=" + numberOfAffectRow);
		}
				
		return userid;
	}
	
	class LoginServer extends AsyncTask<String, Void, Integer> {
		JSONObject response;

		@Override
		protected Integer doInBackground(String... params) {
			try {
				response = Remote.User.login(params[0], params[1]);
			} catch (MinervaConnErr e) {
				e.printStackTrace();
				return e.errorCode();
			}
			return Constants.RESPONSE_SUCCEED_CODE;
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case Constants.RESPONSE_ERROR_PWD:
				
			case Constants.RESPONSE_INVALID_USER:
				
				return;
				
			}
			loginProgress.setMessage("PULLING USER DATA");
			try {
				JSONObject userinfo = response.getJSONObject(Constants.JSON_USERINFO);
				Log.d(Constants.DEBUG_TAG, "LoadingActivity LoginServer userinfo=" + userinfo);
				long user_global_id = userinfo.getLong(Constants.JSON_USER_GLOBAL_ID);
				Log.d(Constants.DEBUG_TAG, "LoadingActivity LoginServer loginUserGrobalId=" + user_global_id);
				user_consumerkey = userinfo.getString(Constants.JSON_CONSUMERKEY);
				user_consumersecret = userinfo.getString(Constants.JSON_CONSUMERSECRET);
				userPrefs.edit().putString(Constants.PREFS_USER_CONSUMERKEY, user_consumerkey)
					.putString(Constants.PREFS_USER_PWD, user_pwd)
					.putString(Constants.PREFS_USER_NAME, user_name)
					.putString(Constants.PREFS_USER_CONSUMERSECRET, user_consumersecret)
					.putLong(Constants.PREFS_USER_GLOBAL_ID, user_global_id)
					.commit();
				long userid = getUserId(user_global_id);
				userPrefs.edit().putLong(Constants.PREFS_USER_ID, userid).commit();
				Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
				intent.putExtra(Constants.PREFS_USER_NAME, user_name)
					.putExtra(Constants.PREFS_USER_ID, userid)
					.putExtra(Constants.PREFS_USER_GLOBAL_ID, user_global_id)
					.putExtra(Constants.PREFS_USER_CONSUMERKEY, user_consumerkey)
					.putExtra(Constants.PREFS_USER_CONSUMERSECRET, user_consumersecret);
				startActivity(intent);
				LoadingActivity.this.finish();
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(LoadingActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
			} catch (NullPointerException e) {
				e.printStackTrace();
				Toast.makeText(LoadingActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
			loginProgress.dismiss();
			super.onPostExecute(result);
		}
	}
}