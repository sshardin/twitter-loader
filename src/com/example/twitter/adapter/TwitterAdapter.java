package com.example.twitter.adapter;

import java.util.List;

import com.example.json.R;
import com.example.twitter.api.model.Tweet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * This class is used to represent the information in each row of the applications list view
 */
public class TwitterAdapter extends ArrayAdapter<Tweet>
{
	private int layoutResourceId;

	/**
	 * Constructor for a new TwitterAdapter
	 * @param context the current context 
	 * @param layoutResourceId the id to represent the layout
	 * @param data a list of information for each row in the list view
	 */
	public TwitterAdapter(Context context, int layoutResourceId, List<Tweet> data)
	{
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@SuppressLint("DefaultLocale")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		TwitterHolder holder = null;

		if (row == null)
		{
			LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			//create a new TwitterHolder and set it to the fields the row in the list view
			holder = new TwitterHolder();
			
			holder.twitterDate  = (TextView) row.findViewById(R.id.date_text);
			holder.twitterTweet = (TextView) row.findViewById(R.id.tweet_text);

			row.setTag(holder);
		}
		else
		{
			holder = (TwitterHolder) row.getTag();
		}
		
		//get the current position from the list
		Tweet tweet = getItem(position);
		
		holder.twitterDate.setText(tweet.getDate());
		holder.twitterTweet.setText(tweet.getText());

		return row;
	}

	/**
	 * A class to represent the fields in the row layout
	 */
	static class TwitterHolder
	{
		TextView twitterDate;
		TextView twitterTweet;
	}
}