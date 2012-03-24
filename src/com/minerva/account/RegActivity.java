package com.minerva.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.minerva.R;
import com.minerva.core.INeedApplication;
import com.minerva.core.MainActivity;
import com.minerva.utils.UtilConstant;

public class RegActivity extends Activity implements OnClickListener {
	
	EditText userName;
	EditText password;
	EditText password2;
	ImageView userLogo;
	Button reg;
	
	INeedApplication app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		app = (INeedApplication) getApplication();
		
		userName = (EditText) findViewById(R.id.edittext_regactivity_username);
		password = (EditText) findViewById(R.id.edittext_regactivity_passwd);
		password2 = (EditText) findViewById(R.id.edittext_regactivity_passwd2);
		userLogo = (ImageView) findViewById(R.id.imageview_regactivity_userlogo);
		reg = (Button) findViewById(R.id.button_regactivity_reg);
		
		reg.setOnClickListener(this);
		//userName.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		long user_id = app.addNewUser(userName.getText().toString(), 0, "", "", "", "", 1);
		
		startActivity(new Intent(this, MainActivity.class)
			.putExtra(UtilConstant.PREFS_USER_NAME, userName.getText().toString())
			.putExtra(UtilConstant.PREFS_USER_ID, user_id));
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) 
			finish();
		
		return super.onKeyDown(keyCode, event);
	}

}
