package com.minerva.utils;


public class Constants {
	public static final String DEBUG_TAG			= "minerva_ineed";
	
	public static final String LOGGED_USER_PREFS 		= "logged_user_prefs";
	public static final String PREFS_USER_NAME 			= "user_name";
	public static final String PREFS_USER_ID 			= "user_id";
	public static final String PREFS_USER_PWD			= "user_pwd";
	public static final String PREFS_USER_CONSUMERKEY	= "user_consumer_key";
	public static final String PREFS_USER_CONSUMERSECRET= "user_consumer_secret";
	
	// SERVER URL
	public static final String SERVER_URL			= "http://ineed.herokuapp.com/";
	public static final String LOGIN_URL			= "user/login/";
	public static final String REGISTER_URL			= "user/register/";
	public static final String ACCESS_TOKEN_URL		= "oauth/access_token/";
	public static final String GET_USER_PROFILE_URL	= "user/get_profile/";
	
	// SERVER RESPONSES ERROR CODE
	public static final int RESPONSE_SUCCEED_CODE	= 200;
	public static final int RESPONSE_INVALID_USER	= 404;
	public static final int RESPONSE_ERROR_PWD		= 401;
	
	public static final int RESPONSE_REG_INCOMPLETE	= 401;
	public static final int RESPONSE_REG_USER_EXISTS= 406;
	
	// JSON FORM
	public static final String JSON_USERNAME			= "user_name";
	public static final String JSON_USERPWD				= "user_password";
	public static final String JSON_USERINFO			= "user_info";
	public static final String JSON_CONSUMERKEY			= "consumer_key";
	public static final String JSON_CONSUMERSECRET		= "consumer_secret";
	public static final String JSON_USER_ID				= "user_id";
	public static final String JSON_USER_GLOBAL_ID		= "user_id";
	public static final String JSON_SHORT_DESCRIPTION	= "short_description";
	public static final String JSON_LIVE_ADDR			= "live_address";
	public static final String JSON_WORK_ADDR			= "work_address";
	public static final String JSON_SINA_ACCOUNT		= "sina_account";
	public static final String JSON_TENCENT_ACCOUNT		= "tencent_account";
	public static final String JSON_AVATAR_URL			= "avatar_url";
	public static final String JSON_THUMBNAIL_URL		= "thumbnail";
	public static final String JSON_USER_EMAIL			= "user_email";
	
	public static final String[] JSON_USER_LOGIN_RESPONSE = 
		{ JSON_CONSUMERKEY, JSON_CONSUMERSECRET, JSON_USERNAME, JSON_USER_GLOBAL_ID };
	public static final String[] JSON_USER_PROFILE_RESPONSE =
		{ 
			JSON_USER_GLOBAL_ID, JSON_SHORT_DESCRIPTION, JSON_LIVE_ADDR, JSON_WORK_ADDR, JSON_SINA_ACCOUNT, JSON_TENCENT_ACCOUNT,
			JSON_AVATAR_URL, JSON_THUMBNAIL_URL 
		};
}
