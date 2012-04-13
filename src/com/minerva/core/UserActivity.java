package com.minerva.core;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.minerva.R;
import com.minerva.utils.ProfileScrollView;
import com.minerva.utils.Constants;

public class UserActivity extends ActivityGroup {

	LinearLayout bg;
	ProfileScrollView sv;
	
	TextView userName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile);
		
		bg = (LinearLayout) findViewById(R.id.linearlayout_activityprofile_logo);
		sv = (ProfileScrollView) findViewById(R.id.scrollview_activityprofile);
		sv.setFollowLinearLayout(bg);
		
		userName = (TextView) findViewById(R.id.textview_profileactivity_username);
		userName.setText(getIntent().getExtras().getString(Constants.PREFS_USER_NAME));
	}

}
