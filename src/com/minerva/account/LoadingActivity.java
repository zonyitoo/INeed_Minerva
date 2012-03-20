package com.minerva.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.minerva.R;

public class LoadingActivity extends Activity implements OnClickListener {
    Button reg;
    LinearLayout logon_layout;
    LinearLayout top;
    Animation logon_anim;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        
        logon_anim = AnimationUtils.loadAnimation(this, R.anim.anim_splash_screen);
        
        reg = (Button) findViewById(R.id.button_reg);
        reg.setOnClickListener(this);
        logon_layout = (LinearLayout) findViewById(R.id.linearlayout_splashscreen_logon);
        top = (LinearLayout) findViewById(R.id.linearlayout_splashscreen_top);
        top.setVisibility(LinearLayout.VISIBLE);
        
        logon_layout.setVisibility(LinearLayout.VISIBLE);
		logon_layout.startAnimation(logon_anim);
		reg.setVisibility(Button.VISIBLE);
    }

	@Override
	public void onClick(View v) {
		startActivityForResult(new Intent(this, RegActivity.class), 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
}