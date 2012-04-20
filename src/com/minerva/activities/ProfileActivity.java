package com.minerva.activities;

import android.app.ActivityGroup;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minerva.R;
import com.minerva.utils.Constants;
import com.minerva.utils.ProfileScrollView;

public class ProfileActivity extends ActivityGroup {

	LinearLayout bg;
	ProfileScrollView sv;
	
	TextView userName;
	
	Button setProfile;
	Button logout;
	
	String user_name;
	long user_id;
	long user_global_id;
	String consumerkey;
	String consumersecret;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile);
		
		bg = (LinearLayout) findViewById(R.id.linearlayout_activityprofile_logo);
		sv = (ProfileScrollView) findViewById(R.id.scrollview_activityprofile);
		sv.setFollowLinearLayout(bg);
		
		user_name = getIntent().getExtras().getString(Constants.PREFS_USER_NAME);
		user_id = getIntent().getExtras().getLong(Constants.PREFS_USER_ID);
		user_global_id = getIntent().getExtras().getLong(Constants.PREFS_USER_GLOBAL_ID);
		consumerkey = getIntent().getExtras().getString(Constants.PREFS_USER_CONSUMERKEY);
		consumersecret = getIntent().getExtras().getString(Constants.PREFS_USER_CONSUMERSECRET);
		
		userName = (TextView) findViewById(R.id.textview_profileactivity_username);
		userName.setText(getIntent().getExtras().getString(Constants.PREFS_USER_NAME));
		setProfile = (Button) findViewById(R.id.button_profileactivity_setprofile);
		logout = (Button) findViewById(R.id.button_profileactivity_logout);
		setButtonListener();
	}
	
	
	void setButtonListener() {
		setProfile.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				Intent intent = new Intent(ProfileActivity.this, ChangeProfileActivity.class);
				intent.putExtra(Constants.PREFS_USER_ID, user_id)
					.putExtra(Constants.PREFS_USER_GLOBAL_ID, user_global_id)
					.putExtra(Constants.PREFS_USER_CONSUMERKEY, consumerkey)
					.putExtra(Constants.PREFS_USER_CONSUMERSECRET, consumersecret)
					.putExtra(Constants.PREFS_USER_NAME, user_name);
				startActivity(intent);
				
			}
			
		});
		
		logout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				SharedPreferences userPrefs = getSharedPreferences(Constants.LOGGED_USER_PREFS, MODE_PRIVATE);
				userPrefs.edit().remove(Constants.PREFS_USER_ID).commit();
				
				startActivity(new Intent(ProfileActivity.this, LoadingActivity.class));
				ProfileActivity.this.finish();
			}
			
		});
	}
}
