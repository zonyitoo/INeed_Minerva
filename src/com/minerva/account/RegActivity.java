package com.minerva.account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
	
	ProgressDialog loadingDialog;
	
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
		
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setMessage("Loading");
		loadingDialog.setCancelable(false);
	}

	@Override
	public void onClick(View v) {
		
		if (!password.getText().toString().equals(password2.getText().toString())) {
			Toast.makeText(this, "password must be the same!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		loadingDialog.show();
		new CommunicateWithServer().execute(null);
		/*
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setMessage("Loading");
		progress.setCancelable(false);
		progress.show();
		
		new Thread() {

			@Override
			public void run() {
				// register
				try {
					URL registerURL = new URL(UtilConstant.SERVER_URL + "/register/");
					HttpURLConnection connection = (HttpURLConnection) registerURL.openConnection();
							
					connection.setRequestMethod("POST");
					connection.setInstanceFollowRedirects(true);
					connection.setDoInput(true);
					connection.setDoOutput(true);
							
					connection.connect();
							
					PrintWriter writer = new PrintWriter(connection.getOutputStream());
					writer.print(userName.getText().toString().trim() + "&" + password.getText().toString() + "&" + userName.getText().toString().trim());
					writer.flush();
					writer.close();
							
					//System.out.println(connection.getResponseMessage());
							
					if (connection.getResponseCode() == 200) {
						InputStreamReader isr = new InputStreamReader(connection.getInputStream());
						BufferedReader reader = new BufferedReader(isr);
								
						String line = null;
								
						while ((line = reader.readLine()) != null) {
							//System.out.println(line);
							Log.d(UtilConstant.DEBUG_TAG, line);
						}
						
						long user_id = app.addNewUser(userName.getText().toString(), 0, "", "", "", "", 1);
						
						startActivity(new Intent(RegActivity.this, MainActivity.class)
							.putExtra(UtilConstant.PREFS_USER_NAME, userName.getText().toString().trim())
							.putExtra(UtilConstant.PREFS_USER_ID, user_id));
						finish();
					}
					
					else {
						Toast.makeText(RegActivity.this, "ERROR", Toast.LENGTH_LONG).show();
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				progress.cancel();
			}
			
		};
		*/
		
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) 
			finish();
		
		return super.onKeyDown(keyCode, event);
	}
	
	class CommunicateWithServer extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				URL registerURL = new URL(UtilConstant.SERVER_URL + "/register/");
				HttpURLConnection connection = (HttpURLConnection) registerURL.openConnection();
						
				connection.setRequestMethod("POST");
				connection.setInstanceFollowRedirects(true);
				connection.setDoInput(true);
				connection.setDoOutput(true);
						
				connection.connect();
						
				PrintWriter writer = new PrintWriter(connection.getOutputStream());
				writer.print(userName.getText().toString().trim() + "&" + password.getText().toString() + "&" + userName.getText().toString().trim());
				writer.flush();
				writer.close();
						
				//System.out.println(connection.getResponseMessage());
						
				if (connection.getResponseCode() == 200) {
					InputStreamReader isr = new InputStreamReader(connection.getInputStream());
					BufferedReader reader = new BufferedReader(isr);
							
					String line = null;
							
					while ((line = reader.readLine()) != null) {
						//System.out.println(line);
						Log.d(UtilConstant.DEBUG_TAG, line);
					}
					
					long user_id = app.addNewUser(userName.getText().toString(), 0, "", "", "", "", 1);
					
					startActivity(new Intent(RegActivity.this, MainActivity.class)
						.putExtra(UtilConstant.PREFS_USER_NAME, userName.getText().toString().trim())
						.putExtra(UtilConstant.PREFS_USER_ID, user_id));
					finish();
				}
				
				else {
					Toast.makeText(RegActivity.this, "ERROR", Toast.LENGTH_LONG).show();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			loadingDialog.dismiss();
		}
		
		
	}

}
