package com.example.twitter.api.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.twitter.api.model.Tweet;
import com.example.twitter.api.model.User;

public class TwitterParser
{	
	private TwitterParser()
	{
		
	}
	
	public static List<Tweet> parseTweet(String json) throws JSONException
	{	
		JSONArray tweets = new JSONArray(json);
		
		return parseTweet(tweets);
	}
	
	private static List<Tweet> parseTweet(JSONArray json) throws JSONException
	{
		List<Tweet> tweets = new ArrayList<Tweet>();
		
		for(int i = 0; i < json.length(); i++)
		{
			Tweet tweet = parseTweet(json.getJSONObject(i));
			tweets.add(tweet);
		}
		
		return tweets;
	}
	
	private static Tweet parseTweet(JSONObject json) throws JSONException
	{
		Tweet tweet = new Tweet();
		
		tweet.setId(json.getLong(Tweet.KEY_ID));
		tweet.setText(json.getString(Tweet.KEY_TEXT));
		tweet.setDate(json.getString(Tweet.KEY_DATE));
		
		return tweet;
	}

	public static List<User> parseUser(String json) throws JSONException
	{
		JSONArray tweets = new JSONArray(json);
	
		return TwitterParser.parseUser(tweets);
	}

	private static List<User> parseUser(JSONArray json) throws JSONException
	{
		List<User> users = new ArrayList<User>();
	
		for (int i = 0; i < json.length(); i++)
		{
			User user = TwitterParser.parseUser(json.getJSONObject(i));
			users.add(user);
		}
	
		return users;
	}

	private static User parseUser(JSONObject json) throws JSONException
	{
		User user = new User();
	
		user.setId(json.getLong(User.KEY_ID));
		user.setName(json.getString(User.KEY_NAME));
	
		return user;
	}
}
