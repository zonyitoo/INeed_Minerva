package com.minerva.core;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.minerva.R;

public class ProvideActivity extends ActivityGroup {
	
	ViewFlipper vf;
	LinearLayout around;
	Animation centerToLeft;
	Animation rightToCenter;
	Animation centerToRight;
	Animation leftToCenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_provide);
		
		vf = (ViewFlipper) findViewById(R.id.viewflipper_provideactivity);
		around = (LinearLayout) findViewById(R.id.linearlayout_provideactivity_around);
		
		centerToLeft = AnimationUtils.loadAnimation(this, R.anim.push_center_to_left);
		rightToCenter = AnimationUtils.loadAnimation(this, R.anim.push_right_to_center);
		centerToRight = AnimationUtils.loadAnimation(this, R.anim.push_center_to_right);
		leftToCenter = AnimationUtils.loadAnimation(this, R.anim.push_left_to_center);
		
		around.setOnClickListener(new OnClickListener() {

			
			public void onClick(View v) {
				vf.setInAnimation(rightToCenter);
				vf.setOutAnimation(centerToLeft);
				
				vf.showNext();
				
			}
			
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && vf.getDisplayedChild() != 0) {
			vf.setInAnimation(leftToCenter);
			vf.setOutAnimation(centerToRight);
			
			vf.showPrevious();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	

}
