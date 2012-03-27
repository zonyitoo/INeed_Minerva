package com.minerva.core;

import java.util.Date;

import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.minerva.utils.RequestDBHelper;
import com.minerva.utils.UserDBHelper;
import com.minerva.utils.UtilConstant;

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
		
		userPrefs = getSharedPreferences(UtilConstant.LOGGED_USER_PREFS, MODE_PRIVATE);
		isLogin = false;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		
		userDB.close();
		requestDB.close();
	}
	
	public synchronized long addNewUser(String user_name, String user_global_id, 
			String userLogoPath, String livePlace, String workPlace, String studiedPlace, int recommendation) {
		ContentValues values = new ContentValues();
		values.put(UserDBHelper.C_NAME, user_name);
		values.put(UserDBHelper.C_GROBAL_ID, user_global_id);
		values.put(UserDBHelper.C_PHOTO_PATH, userLogoPath);
		values.put(UserDBHelper.C_MEMBER_TIME, new Date().toGMTString());
		values.put(UserDBHelper.C_LIVEPLACE, livePlace);
		values.put(UserDBHelper.C_WORKPLACE, workPlace);
		values.put(UserDBHelper.C_STUDIEDPLACE, studiedPlace);
		values.put(UserDBHelper.C_RECOMMENDATION, recommendation);
		
		long user_id = userDB.insertOrThrow(UserDBHelper.TABLE_NAME, null, values);
		userPrefs.edit().putString(UtilConstant.PREFS_USER_NAME, user_name).putLong(UtilConstant.PREFS_USER_ID, user_id).commit();
		
		return user_id;
	}
	
	public synchronized Cursor getUserFromDatabaseByGID(String user_global_id) {
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
	
	public synchronized long modifiedUserDatabase(int user_id, String user_name, String user_global_id, 
			String userLogoPath, String livePlace, String workPlace, String studiedPlace, int recommendation) {
		ContentValues values = new ContentValues();
		values.put(UserDBHelper.C_NAME, user_name);
		values.put(UserDBHelper.C_GROBAL_ID, user_global_id);
		values.put(UserDBHelper.C_PHOTO_PATH, userLogoPath);
		values.put(UserDBHelper.C_MEMBER_TIME, new Date().toGMTString());
		values.put(UserDBHelper.C_LIVEPLACE, livePlace);
		values.put(UserDBHelper.C_WORKPLACE, workPlace);
		values.put(UserDBHelper.C_STUDIEDPLACE, studiedPlace);
		values.put(UserDBHelper.C_RECOMMENDATION, recommendation);
		
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
