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

public class UserActivity extends ActivityGroup {

	LinearLayout bg;
	ProfileScrollView sv;
	
	TextView userName;
	
	Button setProfile;
	Button logout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile);
		
		bg = (LinearLayout) findViewById(R.id.linearlayout_activityprofile_logo);
		sv = (ProfileScrollView) findViewById(R.id.scrollview_activityprofile);
		sv.setFollowLinearLayout(bg);
		
		userName = (TextView) findViewById(R.id.textview_profileactivity_username);
		userName.setText(getIntent().getExtras().getString(Constants.PREFS_USER_NAME));
		setProfile = (Button) findViewById(R.id.button_profileactivity_setprofile);
		logout = (Button) findViewById(R.id.button_profileactivity_logout);
		setButtonListener();
	}
	
	
	void setButtonListener() {
		setProfile.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				
			}
			
		});
		
		logout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				SharedPreferences userPrefs = getSharedPreferences(Constants.LOGGED_USER_PREFS, MODE_PRIVATE);
				userPrefs.edit().remove(Constants.PREFS_USER_ID).commit();
				
				startActivity(new Intent(UserActivity.this, LoadingActivity.class));
				UserActivity.this.finish();
			}
			
		});
	}
}
