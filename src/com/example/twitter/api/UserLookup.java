package com.example.twitter.api;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import com.example.twitter.api.model.User;
import com.example.twitter.api.parser.TwitterParser;

public class UserLookup
{

	public static final String REQUEST_URL    = "users/show.json";
	public static final String REQUEST_METHOD = "GET";
	
	public static final String PARAM_USER  = "screen_name";
	
	private UserLookup()
	{
		
	}
	
	public static User getUser(String user) throws IOException, JSONException
	{
		TwitterRequest request = new TwitterRequest(REQUEST_URL, REQUEST_METHOD);
		
		request.appendQuery(PARAM_USER, user);
		
		String json = request.execute();
		
		if(json == null) return null;
		
		List<User> users = TwitterParser.parseUser(json);
		
		if(users == null|| users.size() == 0) return null;
 		
		return users.get(0);
	}
}
