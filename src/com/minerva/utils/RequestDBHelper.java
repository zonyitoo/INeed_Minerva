package com.minerva.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class RequestDBHelper extends SQLiteOpenHelper {
	
	public static final String DB_NAME = "request.db";
	public static final int DB_VERSION = 1;
	
	public static final String REQUEST_USER_PREV = "request_user_";					// the table name is PREV+UserID
	public static final String C_ID = BaseColumns._ID;								// primary key
	public static final String C_TITLE = "request_title";							// The title of request
	public static final String C_DESCRIPTION = "request_description";				// The description of request
	public static final String C_REWARD = "request_reward";							// The reward of request(double)
	public static final String C_REWARD_UNIT = "request_reward_unit";				// The unit of reward money enum{RMB, DOLLAR, ...}
	public static final String C_POST_DATE = "request_post_date";					// The DATE of the post
	public static final String C_LOCATION = "request_location";						// The Location information
	
	Context context;
	
	public static final String REQUEST_TIMELINE = "request_timeline";				// Timeline
	public static final String REQUEST_USER_NAME = "request_user_name";
	public static final String REQUEST_USER_PHOTO_PATH = "request_user_photo_path";
	
	public static final String REQUEST_RESPONSE = "request_response";				// Response
	public static final String RESPONSE_USER_GROBAL_ID = "response_user_grobal_id";
	public static final String RESPONSE_COMMENT = "response_comment";
	public static final String RESPONSE_PRIZE = "response_prize";
	public static final String RESPONSE_PRIZE_UNIT = "response_prize_unit";
	public static final String RESPONSE_DATE = "response_date";
	
	private static final String CREATE_REQUEST_TIMELINE = "CREATE TABLE " + REQUEST_TIMELINE + " ("
			+ C_ID 						+ " INT PRIMARY KEY, "
			+ REQUEST_USER_NAME 		+ " TEXT NOT NULL, "
			+ REQUEST_USER_PHOTO_PATH 	+ " TEXT, "
			+ C_TITLE 					+ " TEXT NOT NULL, "
			+ C_DESCRIPTION 			+ " TEXT NOT NULL, "
			+ C_REWARD 					+ " DOUBLE NOT NULL, "
			+ C_REWARD_UNIT 			+ " INT NOT NULL, "
			+ C_POST_DATE 				+ " TEXT NOT NULL, "
			+ C_LOCATION 				+ " TEXT"
			+ ")";
	private static final String CREATE_REQUEST_RESPONSE = "CREATE TABLE " + REQUEST_RESPONSE + " ("
			+ C_ID						+ " INT PRIMARY KEY, "
			+ RESPONSE_USER_GROBAL_ID	+ " INT NOT NULL, "
			+ RESPONSE_COMMENT			+ " TEXT, "
			+ RESPONSE_PRIZE			+ " INT NOT NULL, "
			+ RESPONSE_PRIZE_UNIT		+ " INT NOT NULL, "
			+ RESPONSE_DATE				+ " TEXT"
			+ ")";
	private static final String CREATE_USER_REQUEST_RESPONSE_PREV = "CREATE TABLE " + REQUEST_USER_PREV;
	private static final String CREATE_USER_REQUEST_RESPONSE_TAIL = " ("
			+ C_ID						+ " INT PRIMARY KEY, "
			+ C_TITLE					+ " TEXT NOT NULL, "
			+ C_DESCRIPTION				+ " TEXT NOT NULL, "
			+ C_REWARD					+ " DOUBLE NOT NULL, "
			+ C_REWARD_UNIT				+ " INT NOT NULL, "
			+ C_POST_DATE				+ " TEXT NOT NULL, "
			+ C_LOCATION				+ " TEXT"
			+ ")";
	
	public RequestDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_REQUEST_TIMELINE);
		db.execSQL(CREATE_REQUEST_RESPONSE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void createUserRequestTable(SQLiteDatabase db, int userID) {
		db.execSQL(CREATE_USER_REQUEST_RESPONSE_PREV + userID + CREATE_USER_REQUEST_RESPONSE_TAIL);
	}

}
