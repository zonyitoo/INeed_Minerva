package com.minerva.core;

import java.util.Date;

import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.minerva.utils.RequestDBHelper;
import com.minerva.utils.UserDBHelper;
import com.minerva.utils.UtilConstant;

public class INeedApplication extends Application {
	
	UserDBHelper userDBHelper;
	SQLiteDatabase userDB;
	RequestDBHelper requestDBHelper;
	SQLiteDatabase requestDB;
	
	SharedPreferences userPrefs;
	
	@Override
	public void onCreate() {
		super.onCreate();
		userDBHelper = new UserDBHelper(this);
		userDB = userDBHelper.getWritableDatabase();
		requestDBHelper = new RequestDBHelper(this);
		requestDB = requestDBHelper.getWritableDatabase();
		
		userPrefs = getSharedPreferences(UtilConstant.LOGGED_USER_PREFS, MODE_PRIVATE);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		
		userDB.close();
		requestDB.close();
	}
	
	public synchronized long addNewUser(String user_name, long user_global_id, 
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

}
