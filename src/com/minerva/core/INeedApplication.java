package com.minerva.core;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.minerva.utils.Constants;
import com.minerva.utils.RequestDBHelper;
import com.minerva.utils.UserDBHelper;

public class INeedApplication extends Application {
	
	UserDBHelper userDBHelper;
	SQLiteDatabase userDB;
	RequestDBHelper requestDBHelper;
	SQLiteDatabase requestDB;
	
	SharedPreferences userPrefs;
	
	private boolean isLogin;
	
	@Override
	public void onCreate() {
		super.onCreate();
		userDBHelper = new UserDBHelper(this);
		userDB = userDBHelper.getWritableDatabase();
		requestDBHelper = new RequestDBHelper(this);
		requestDB = requestDBHelper.getWritableDatabase();
		
		userPrefs = getSharedPreferences(Constants.LOGGED_USER_PREFS, MODE_PRIVATE);
		isLogin = false;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		
		userDB.close();
		requestDB.close();
	}
	
	public synchronized long addNewUser(JSONObject userprofile) throws JSONException {
		ContentValues values = new ContentValues();
		values.put(UserDBHelper.C_NAME, userprofile.getString(Constants.JSON_USERNAME));
		values.put(UserDBHelper.C_GROBAL_ID, userprofile.getString(Constants.JSON_USER_GLOBAL_ID));
		values.put(UserDBHelper.C_SHORT_DESCRIPTION, userprofile.getString(Constants.JSON_SHORT_DESCRIPTION));
		values.put(UserDBHelper.C_AVATAR_PATH, userprofile.getString(Constants.JSON_AVATAR_URL));
		values.put(UserDBHelper.C_THUMBNAIL_PATH, userprofile.getString(Constants.JSON_THUMBNAIL_URL));
		values.put(UserDBHelper.C_MEMBER_TIME, new Date().toGMTString());
		values.put(UserDBHelper.C_LIVEPLACE, userprofile.getString(Constants.JSON_LIVE_ADDR));
		values.put(UserDBHelper.C_WORKPLACE, userprofile.getString(Constants.JSON_WORK_ADDR));
		values.put(UserDBHelper.C_STUDIEDPLACE, "");
		values.put(UserDBHelper.C_RECOMMENDATION, 0);
		
		long user_id = userDB.insertOrThrow(UserDBHelper.TABLE_NAME, null, values);
		
		return user_id;
	}
	
	public synchronized Cursor getUserFromDatabaseByGrobalId(long user_global_id) {
		Cursor cursor = userDB.query(UserDBHelper.TABLE_NAME, UserDBHelper.COLUMNS, UserDBHelper.C_GROBAL_ID + "=" + user_global_id, null, null, null, null);
		
		return cursor;
	}
	
	public synchronized Cursor getUserFromDatabaseByName(String user_name) {
		Cursor cursor;
		try {
			cursor = userDB.query(UserDBHelper.TABLE_NAME, UserDBHelper.COLUMNS, UserDBHelper.C_NAME + "=" + user_name, null, null, null, null);
		} catch (SQLiteException e) {
			return null;
		}
		return cursor;
	}
	
	public synchronized long modifiedUserDatabase(JSONObject userprofile, long user_id) throws JSONException {
		ContentValues values = new ContentValues();
		values.put(UserDBHelper.C_NAME, userprofile.getString(Constants.JSON_USERNAME));
		values.put(UserDBHelper.C_GROBAL_ID, userprofile.getString(Constants.JSON_USER_GLOBAL_ID));
		values.put(UserDBHelper.C_SHORT_DESCRIPTION, userprofile.getString(Constants.JSON_SHORT_DESCRIPTION));
		values.put(UserDBHelper.C_AVATAR_PATH, userprofile.getString(Constants.JSON_AVATAR_URL));
		values.put(UserDBHelper.C_THUMBNAIL_PATH, userprofile.getString(Constants.JSON_THUMBNAIL_URL));
		values.put(UserDBHelper.C_MEMBER_TIME, new Date().toGMTString());
		values.put(UserDBHelper.C_LIVEPLACE, userprofile.getString(Constants.JSON_LIVE_ADDR));
		values.put(UserDBHelper.C_WORKPLACE, userprofile.getString(Constants.JSON_WORK_ADDR));
		values.put(UserDBHelper.C_STUDIEDPLACE, "");
		values.put(UserDBHelper.C_RECOMMENDATION, 0);
		
		int numberOfRowAffected = userDB.update(UserDBHelper.TABLE_NAME, values, UserDBHelper.C_ID + "=" + user_id, null);
		return numberOfRowAffected;
	}
	
	public boolean getLoginState() {
		return isLogin;
	}
	
	public void setLoginState(boolean state) {
		isLogin = state;
	}
}
