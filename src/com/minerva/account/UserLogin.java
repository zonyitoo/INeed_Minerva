package com.minerva.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;
import com.minerva.utils.Constants;

/**
 * @author zonyitoo
 *
 */
public class UserLogin {
	
	URL loginURL;
	
	public UserLogin() {
		try {
			loginURL = new URL(Constants.SERVER_URL + "/login/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Login to server use the given username and password
	 * 
	 * @return response code, if IOException occurs, return -1
	 */
    public int login(String user_name, String user_pwd) {
    	// Login server
    	
        PrintWriter writer = null;
        int respCode = 0;
        try {
        	
         	HttpURLConnection connection = (HttpURLConnection) loginURL.openConnection();
         			
         	connection.setRequestMethod("POST");
         	connection.setDoInput(true);
         	connection.setDoOutput(true);
         			
         	writer = new PrintWriter(connection.getOutputStream());
         			
         	writer.print(user_name + "&" + user_pwd);
         	writer.flush();
         	writer.close();
         			
         	respCode = connection.getResponseCode();
         	Log.d(Constants.DEBUG_TAG, "respCode=" + respCode);
        } catch (IOException e) {
        	return -1;
         	//e.printStackTrace();
        }
        
        return respCode;
    }
}
