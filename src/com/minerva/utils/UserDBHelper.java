package com.minerva.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class UserDBHelper extends SQLiteOpenHelper {
	
	public static final String DB_NAME = "user_info.db";					// Database File Name
	public static final int DB_VERSION = 1;									// Database Version code
	public static final String TABLE_NAME = "user_information";				// Table Name
	public static final String C_ID = BaseColumns._ID;						// User ID
	public static final String C_GROBAL_ID = "grobal_id";					// The ID stored in the Server, only identify the user
	public static final String C_NAME = "user_name";						// User Name
	public static final String C_PHOTO_PATH = "photo_path";					// Photo Path in storage
	public static final String C_MEMBER_TIME = "member_time";				// Time to register
	public static final String C_LIVEPLACE = "live_place";					// Place of living
	public static final String C_WORKPLACE = "work_place";					// Place of working
	public static final String C_STUDIEDPLACE = "studied_place";			// Place of studying
	public static final String C_RECOMMENDATION = "recommendation";			// A number, recommendation level
	
	public static final String[] COLUMNS =
		{
			C_ID, C_GROBAL_ID, C_NAME, C_PHOTO_PATH, C_NAME, C_PHOTO_PATH, C_MEMBER_TIME
			, C_LIVEPLACE, C_WORKPLACE, C_STUDIEDPLACE, C_RECOMMENDATION
		};
	
	Context context;
	
	public UserDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + TABLE_NAME + " ("
				+ C_ID 				+ " INT PRIMARY KEY, "
				+ C_GROBAL_ID		+ " TEXT NOT NULL, "
				+ C_NAME 			+ " TEXT NOT NULL, "
				+ C_PHOTO_PATH 		+ " TEXT, "
				+ C_MEMBER_TIME 	+ " TEXT, "
				+ C_LIVEPLACE 		+ " TEXT, "
				+ C_WORKPLACE 		+ " TEXT, "
				+ C_STUDIEDPLACE 	+ " TEXT, "
				+ C_RECOMMENDATION 	+ " INT"
				+ ")";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
