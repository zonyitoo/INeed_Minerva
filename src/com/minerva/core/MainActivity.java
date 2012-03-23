package com.minerva.core;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.minerva.R;
import com.minerva.utils.UtilConstant;

public class MainActivity extends Activity {
	TabHost maintabs;
	TabSpec message;
	TabSpec request;
	TabSpec provide;
	TabSpec user;
	LocalActivityManager localActivityManager;
	
	String user_name;
	long user_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		localActivityManager = new LocalActivityManager(this, true);
		localActivityManager.dispatchCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		user_name = getIntent().getExtras().getString(UtilConstant.PREFS_USER_NAME);
		user_id = getIntent().getExtras().getLong(UtilConstant.PREFS_USER_ID);
		
		maintabs = (TabHost) findViewById(R.id.tabhost_mainactivity);
		maintabs.setup(localActivityManager);
		message = maintabs.newTabSpec("Message");
		message.setContent(new Intent(this, MessageActivity.class)
			.putExtra(UtilConstant.PREFS_USER_NAME, user_name)
			.putExtra(UtilConstant.PREFS_USER_ID, user_id));
		message.setIndicator("Message");
		request = maintabs.newTabSpec("Request");
		request.setIndicator("Request");
		request.setContent(new Intent(this, RequestActivity.class)
			.putExtra(UtilConstant.PREFS_USER_NAME, user_name)
			.putExtra(UtilConstant.PREFS_USER_ID, user_id));
		provide = maintabs.newTabSpec("Provide");
		provide.setContent(new Intent(this, ProvideActivity.class)
			.putExtra(UtilConstant.PREFS_USER_NAME, user_name)
			.putExtra(UtilConstant.PREFS_USER_ID, user_id));
		provide.setIndicator("Provide");
		user = maintabs.newTabSpec("User");
		user.setContent(new Intent(this, UserActivity.class)
			.putExtra(UtilConstant.PREFS_USER_NAME, user_name)
			.putExtra(UtilConstant.PREFS_USER_ID, user_id));
		user.setIndicator("User");
		maintabs.addTab(message);
		maintabs.addTab(request);
		maintabs.addTab(provide);
		maintabs.addTab(user);
		maintabs.setCurrentTab(0);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
