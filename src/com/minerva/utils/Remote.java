package com.minerva.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Base64;
import android.util.Log;

public class Remote {
	
	public static class User {
		
		public static synchronized JSONObject login(String username, String password) throws MinervaConnErr {
			
			try {
				URL url = new URL(Constants.SERVER_URL + Constants.LOGIN_URL);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				
				connection.connect();
				
				
				String postString = "{\"" + Constants.JSON_USERNAME + "\": " + "\"" + username + "\"" 
						+ ", \"" + Constants.JSON_USERPWD + "\": " + "\"" + password + "\"" + "}";
				
				String encodeString = Base64.encodeToString(postString.getBytes(), Base64.DEFAULT);
				
				PrintWriter writer = new PrintWriter(connection.getOutputStream());
				writer.print(encodeString);
				writer.flush();
				writer.close();
				
				int responseCode = connection.getResponseCode();
				Log.d(Constants.DEBUG_TAG, "Remote Login responseCode=" + responseCode);
				if (responseCode == Constants.RESPONSE_ERROR_PWD || responseCode == Constants.RESPONSE_INVALID_USER) {
					throw new MinervaConnErr(responseCode);
				}
				
				/**
				 * 成功登陆则返回code 200
				 */
				if (responseCode == Constants.RESPONSE_SUCCEED_CODE) {
					String responseString = null;
					InputStreamReader isr = new InputStreamReader(connection.getInputStream());
					BufferedReader reader = new BufferedReader(isr);
					
					responseString = reader.readLine();
					
					/*
					consumer_json_dict = {'user_info': 
                    {'consumer_key': consumer.key, 
                     'consumer_secret': consumer.secret, 
                     'user_name': user.username,
                     'user_id': user.id} 
                    }
                   */
					
					JSONObject response = new JSONObject((String) new JSONTokener(responseString).nextValue());
					Log.d(Constants.DEBUG_TAG, "Remote Login json=" + response);
					return response;
				}
				
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
			/**
			 * after login suceess, you should get the access-token
			 */
			////
		}
		
		public static synchronized JSONObject register(String username, String password, String email) throws MinervaConnErr {
			URL url;
			try {
				url = new URL(Constants.SERVER_URL + Constants.REGISTER_URL);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				
				connection.setRequestMethod("POST");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				
				connection.connect();
				
				PrintWriter writer = new PrintWriter(connection.getOutputStream());
				String postString = "{\"user_name\": " + "\"" + username + "\"" + ", \"user_password\": " + "\"" + 
						password + "\"" + ", \"user_email\": " + "\"" + email + "\"" +  "}";
				
				String encodeString = Base64.encodeToString(postString.getBytes(), Base64.DEFAULT);
				
				writer.print(encodeString);
				writer.flush();
				writer.close();

				//System.out.println(connection.getResponseMessage());
				int responseCode = connection.getResponseCode();
				Log.d(Constants.DEBUG_TAG, "Remote register responseCode=" + responseCode);
				if (responseCode == Constants.RESPONSE_REG_INCOMPLETE) {
					System.out.println("the user information is not complete");
					throw new MinervaConnErr(responseCode);
				}
				else if (responseCode == Constants.RESPONSE_REG_USER_EXISTS || connection.getResponseCode() == 500) {
					System.out.println("the user is exist");
					throw new MinervaConnErr(responseCode);
				}
				else if (connection.getResponseCode() == Constants.RESPONSE_SUCCEED_CODE) {
					StringBuffer buffer = new StringBuffer("");
					String line = null;
					
					InputStreamReader isr = new InputStreamReader(connection.getInputStream());
					BufferedReader reader = new BufferedReader(isr);
					
					while ((line = reader.readLine()) != null) {
						buffer.append(line);
					}
					
					JSONTokener tokener = new JSONTokener(buffer.toString());
					JSONObject response = new JSONObject((String) tokener.nextValue());
					
					return response;
				}
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
	
		public static synchronized JSONObject getUserProfile(String username, String password, long global_id, String consumerkey, String consumersecret) throws JSONException {
			Log.d(Constants.DEBUG_TAG, "Reomte getUserProfile post to=" + Constants.SERVER_URL + Constants.ACCESS_TOKEN_URL);
			AccessTokenGetter accessTokenGetter = new AccessTokenGetter(Constants.SERVER_URL + Constants.ACCESS_TOKEN_URL,
					username, password, consumerkey, consumersecret);
			String[] accessTokenData = accessTokenGetter.getAccessToken();
			Log.d(Constants.DEBUG_TAG, "Remote getUserProfile accessTokenData=" + accessTokenData);
			String accessToken = accessTokenData[0];
			String accessTokenSecret = accessTokenData[1];
			ResourceDataGetter resourceGetter = new ResourceDataGetter(Constants.SERVER_URL + Constants.GET_USER_PROFILE_URL + global_id + "/",
					accessToken, accessTokenSecret, consumerkey, consumersecret, "GET");
			String server_response = resourceGetter.getResource();
			return new JSONObject((String) (new JSONTokener(server_response)).nextValue());
		}
	}
}
