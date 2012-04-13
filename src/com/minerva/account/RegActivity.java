package com.minerva.account;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.minerva.R;
import com.minerva.core.INeedApplication;
import com.minerva.core.MainActivity;
import com.minerva.utils.Constants;
import com.minerva.utils.MinervaConnErr;
import com.minerva.utils.Remote;
import com.minerva.utils.UserDBHelper;

public class RegActivity extends Activity implements OnClickListener {
	
	SharedPreferences userPrefs;
	
	EditText userName;
	EditText password;
	EditText password2;
	EditText userEmail;
	Button reg;
	
	String user_name;
	String user_pwd;
	String user_pwd_again;
	String user_email;
	String user_consumerkey;
	String user_consumersecret;
	
	INeedApplication app;
	
	ProgressDialog loadingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		app = (INeedApplication) getApplication();
		
		userPrefs = getSharedPreferences(Constants.LOGGED_USER_PREFS, MODE_PRIVATE);
		
		userName = (EditText) findViewById(R.id.edittext_regactivity_username);
		password = (EditText) findViewById(R.id.edittext_regactivity_passwd);
		password2 = (EditText) findViewById(R.id.edittext_regactivity_passwd2);
		userEmail = (EditText) findViewById(R.id.edittext_regactivity_email);
		reg = (Button) findViewById(R.id.button_regactivity_reg);
		
		reg.setOnClickListener(this);
		//userName.addTextChangedListener(this);
		
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setMessage("Loading");
		loadingDialog.setCancelable(false);
	}

	public void onClick(View v) {
		user_name = userName.getText().toString().trim();
		user_pwd = password.getText().toString();
		user_pwd_again = password2.getText().toString();
		user_email = userEmail.getText().toString().trim();
		
		if (user_name.isEmpty() || user_pwd.length() < 6 || user_pwd_again.isEmpty() || user_email.isEmpty()) {
			Toast.makeText(this, "INVALID INPUT", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (!user_pwd.equals(user_pwd_again)) {
			Toast.makeText(this, "password must be the same!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		loadingDialog.show();
		new RegisterServer().execute(user_name, user_pwd, user_email);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) 
			finish();
		
		return super.onKeyDown(keyCode, event);
	}
	
	protected long getUserId(long user_global_id) throws JSONException {
		//INeedApplication app = (INeedApplication) getApplication();
		Cursor cursor = app.getUserFromDatabaseByGrobalId(user_global_id);
		JSONObject userprofile = Remote.User.getUserProfile(user_name, user_pwd, user_global_id, user_consumerkey, user_consumersecret);
		long userid = 0;
		if (cursor.getCount() == 0) {
			userid = app.addNewUser(userprofile);
		}
		else {
			userid = cursor.getLong(cursor.getColumnIndex(UserDBHelper.C_ID));
			
			long numberOfAffectRow = app.modifiedUserDatabase(userprofile, userid);
			Log.d(Constants.DEBUG_TAG, "RegActivity getUserId numberOfAffectRow=" + numberOfAffectRow);
		}
		
		return userid;
	}
	
	class RegisterServer extends AsyncTask<String, Void, Integer> {
		
		JSONObject response;
		
		@Override
		protected Integer doInBackground(String... params) {
			try {
				response = Remote.User.register(params[0], params[1], params[2]);
			} catch (MinervaConnErr e) {
				e.printStackTrace();
				
				return e.errorCode();
			}
			return Constants.RESPONSE_SUCCEED_CODE;
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			
			}

			try {
				JSONObject userinfo = response.getJSONObject(Constants.JSON_USERINFO);
				Log.d(Constants.DEBUG_TAG, "RegActivity RegisterServer userinfo=" + userinfo);
				long user_global_id = userinfo.getLong(Constants.JSON_USER_GLOBAL_ID);
				user_consumerkey = userinfo.getString(Constants.JSON_CONSUMERKEY);
				user_consumersecret = userinfo.getString(Constants.JSON_CONSUMERSECRET);
				userPrefs.edit().putString(Constants.PREFS_USER_CONSUMERKEY, user_consumerkey)
					.putString(Constants.PREFS_USER_PWD, user_pwd)
					.putString(Constants.PREFS_USER_NAME, user_name)
					.putString(Constants.PREFS_USER_CONSUMERSECRET, user_consumersecret)
					.commit();
				long userid = getUserId(user_global_id);
				Intent intent = new Intent(RegActivity.this, MainActivity.class);
				intent.putExtra(Constants.PREFS_USER_NAME, user_name)
					.putExtra(Constants.PREFS_USER_ID, userid)
					.putExtra(Constants.PREFS_USER_CONSUMERKEY, user_consumerkey)
					.putExtra(Constants.PREFS_USER_CONSUMERSECRET, user_consumersecret);
				startActivity(intent);
				finish();
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(RegActivity.this, "postExecute JSONException", Toast.LENGTH_LONG).show();
			}
			loadingDialog.dismiss();
			super.onPostExecute(result);
		}
		
	}
}
