package com.minerva.activities;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.minerva.R;

public class MessageActivity extends TabActivity {

	TabHost tabs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
		tabs = getTabHost();
		tabs.addTab(tabs.newTabSpec("Buy").setContent(R.id.linearlayout_mainactivity_message_buy).setIndicator("Buy"));
		tabs.addTab(tabs.newTabSpec("Sale").setContent(R.id.linearlayout_mainactivity_message_sale).setIndicator("Sale"));
		tabs.setCurrentTab(0);
	}
	
}
