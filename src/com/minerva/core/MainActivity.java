package com.minerva.core;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.minerva.R;
import com.minerva.utils.Constants;

public class MainActivity extends Activity {
	TabHost maintabs;
	TabSpec message;
	TabSpec request;
	TabSpec provide;
	TabSpec user;
	LocalActivityManager localActivityManager;
	
	String user_name;
	long user_id;
	long user_global_id;
	String consumerkey;
	String consumersecret;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		localActivityManager = new LocalActivityManager(this, true);
		localActivityManager.dispatchCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		user_name = getIntent().getExtras().getString(Constants.PREFS_USER_NAME);
		user_id = getIntent().getExtras().getLong(Constants.PREFS_USER_ID);
		user_global_id = getIntent().getExtras().getLong(Constants.PREFS_USER_GLOBAL_ID);
		consumerkey = getIntent().getExtras().getString(Constants.PREFS_USER_CONSUMERKEY);
		consumersecret = getIntent().getExtras().getString(Constants.PREFS_USER_CONSUMERSECRET);
		
		maintabs = (TabHost) findViewById(R.id.tabhost_mainactivity);
		maintabs.setup(localActivityManager);
		message = maintabs.newTabSpec("Message");
		message.setContent(new Intent(this, MessageActivity.class)
			.putExtra(Constants.PREFS_USER_NAME, user_name)
			.putExtra(Constants.PREFS_USER_ID, user_id)
			.putExtra(Constants.PREFS_USER_GLOBAL_ID, user_global_id)
			.putExtra(Constants.PREFS_USER_CONSUMERKEY, consumerkey)
			.putExtra(Constants.PREFS_USER_CONSUMERSECRET, consumersecret));
		message.setIndicator("Message");
		request = maintabs.newTabSpec("Request");
		request.setIndicator("Request");
		request.setContent(new Intent(this, RequestActivity.class)
			.putExtra(Constants.PREFS_USER_NAME, user_name)
			.putExtra(Constants.PREFS_USER_ID, user_id)
			.putExtra(Constants.PREFS_USER_GLOBAL_ID, user_global_id)
			.putExtra(Constants.PREFS_USER_CONSUMERKEY, consumerkey)
			.putExtra(Constants.PREFS_USER_CONSUMERSECRET, consumersecret));
		provide = maintabs.newTabSpec("Provide");
		provide.setContent(new Intent(this, ProvideActivity.class)
			.putExtra(Constants.PREFS_USER_NAME, user_name)
			.putExtra(Constants.PREFS_USER_ID, user_id)
			.putExtra(Constants.PREFS_USER_GLOBAL_ID, user_global_id)
			.putExtra(Constants.PREFS_USER_CONSUMERKEY, consumerkey)
			.putExtra(Constants.PREFS_USER_CONSUMERSECRET, consumersecret));
		provide.setIndicator("Provide");
		user = maintabs.newTabSpec("User");
		user.setContent(new Intent(this, UserActivity.class)
			.putExtra(Constants.PREFS_USER_NAME, user_name)
			.putExtra(Constants.PREFS_USER_ID, user_id)
			.putExtra(Constants.PREFS_USER_GLOBAL_ID, user_global_id)
			.putExtra(Constants.PREFS_USER_CONSUMERKEY, consumerkey)
			.putExtra(Constants.PREFS_USER_CONSUMERSECRET, consumersecret));
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
