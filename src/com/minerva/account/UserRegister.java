package com.minerva.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.minerva.utils.Constants;

public class UserRegister {
	
	URL registerURL;
	
	public UserRegister() {
		try {
			registerURL = new URL(Constants.SERVER_URL + "/register/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, Object> register(String user_name, String user_passwd,
			String photo_path, String live_place, String work_place, String studied_place) {
		
		HttpURLConnection connection;
		String responseMessage = null;
		String[] responseData;
		try {
			connection = (HttpURLConnection) registerURL.openConnection();
			connection.setRequestMethod("POST");
			connection.setInstanceFollowRedirects(true);
			connection.setDoInput(true);
			connection.setDoOutput(true);
					
			connection.connect();
					
			PrintWriter writer = new PrintWriter(connection.getOutputStream());
			writer.print(user_name + "&" + user_passwd);
			writer.flush();
			writer.close();
			
			if (connection.getResponseCode() != 200) {
				return null;
			}
			else {
				responseMessage = connection.getResponseMessage();
				responseData = responseMessage.split("&");
			}
		} catch(IOException e) {
			
			return null;
		}
		
		
		HashMap<String, Object> userDetails = new HashMap<String, Object>();
		
		
		return userDetails;
	}
}
