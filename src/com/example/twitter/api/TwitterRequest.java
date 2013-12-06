package com.example.twitter.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.twitter.api.util.IOUtil;

public class TwitterRequest
{
	public static final String URL_TWITTER_REQUEST = "https://api.twitter.com/1.1/";
	
	private StringBuilder requestBuilder;
	private String        requestMethod;
	
	private boolean queryAppended = false;
	
	public TwitterRequest(String baseRequest, String method)
	{
		requestBuilder = new StringBuilder(URL_TWITTER_REQUEST);
		requestBuilder.append(baseRequest);
		
		requestMethod  = method;
	}
	
	private void appendQueryToken()
	{
		if(queryAppended)
		{
			requestBuilder.append('&');
		}
		else
		{
			requestBuilder.append('?');
			queryAppended = true;
		}
	}
	
	public void appendQuery(String param, int value)
	{
		appendQueryToken();

		requestBuilder.append(param).append('=').append(value);
	}
	
	public void appendQuery(String param, long value)
	{
		appendQueryToken();

		requestBuilder.append(param).append('=').append(value);
	}
	
	public void appendQuery(String param, boolean value)
	{
		appendQueryToken();

		requestBuilder.append(param).append('=').append(value);
	}
	
	public void appendQuery(String param, String value)
	{
		appendQueryToken();

		requestBuilder.append(param).append('=').append(value);
	}
	
	public String getUrl()
	{
		
		return requestBuilder.toString();
	}
	
	public String getMethod()
	{
		return requestMethod;
	}
	
	public String execute() throws IOException
	{
		URL url = new URL(getUrl());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestMethod(getMethod());
		connection.setRequestProperty("Authorization", "Bearer " + BEARER_ID);
		connection.connect();
		
		if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) return null;
		
		InputStream in = connection.getInputStream();
		String results = new String(IOUtil.toByteArray(in));
		in.close();
		
		connection.disconnect();
		
		return results;
	}
	
	private static final String BEARER_ID =
			"AAAAAAAAAAAAAAAAAAAAAPI6TwAAAAAA%2F46RRPoEM%2FQnSzghZ" +
			"kcVypRj8cw%3DMF9tQnWr4q3ZgC7xVxGQsNFLbZG2OeOGYl12BEaOY";
}