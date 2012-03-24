package com.minerva.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class ProfileScrollView extends ScrollView {
	
	LinearLayout follower = null;
	
	public ProfileScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ProfileScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ProfileScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setFollowLinearLayout(LinearLayout ll) {
		follower = ll;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		
		follower.scrollTo(l, t / 2);
	}
	
}
