package com.minerva.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;

public class LoadingDialog extends Dialog {

	private LayoutParams param;
	public LoadingDialog(Context context) {
		super(context);
		
		param = getWindow().getAttributes();
		param.gravity = Gravity.CENTER;
		param.dimAmount = 0;
		param.alpha = 1.0f;
		getWindow().setAttributes(param);
	}
	
}
