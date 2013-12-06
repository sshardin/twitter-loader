package com.example.twitter.loader;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.twitter.api.UserTimeline;
import com.example.twitter.api.model.Tweet;

public class UserTimelineLoader extends AsyncTaskLoader<List<Tweet>>
implements UserTimeline.Poller.Listener
{
	private UserTimeline userTimeline;
	
	public UserTimelineLoader(Context context, String user)
	{
		super(context);
		
		if(user == null) return;
		
		userTimeline = new UserTimeline(user);
	}
	
	public UserTimeline getUserTimeline()
	{
		return userTimeline;
	}
	
	public void loadMoreTweets()
	{
		forceLoad();
	}

	@Override
	public List<Tweet> loadInBackground()
	{
		Log.d("UserTimelineLoader", "loadInBackground()");
		
		try
		{
			return userTimeline.getOldTweets();
		}
		catch(IOException e)   { }
		catch(JSONException e) { }
		
		return null;
	}
	
	public void onAbandon()
	{
		super.onAbandon();
		
		Log.d("UserTimelineLoader", "onAbandon()");
	}
	
	public boolean onCancelLoad()
	{
		Log.d("UserTimelineLoader", "onCancelLoad()");
		
		return true;
	}
	
	public void onForceLoad()
	{
		super.onForceLoad();
		
		Log.d("UserTimelineLoader", "onForceLoad()");	
	}
	
	public void onReset()
	{
		super.onReset();
		
		Log.d("UserTimelineLoader", "onReset()");
	}
	
	public void onStartLoading()
	{
		super.onStartLoading();
		
		Log.d("UserTimelineLoader", "onStartLoading()");
		
		if(userTimeline == null) return;
		
		userTimeline.setPollerListener(this);
		userTimeline.startPoller();
	}
	
	public void onStopLoading()
	{
		super.onStopLoading();
		
		Log.d("UserTimelineLoader", "onStopLoading()");
		
		if(userTimeline == null) return;
		
		userTimeline.stopPoller();
	}

	@Override
	public void newTweetsPolled(final List<Tweet> tweets)
	{
		Log.d("UserTimelineLoader", "newTweetsPolled()");
		
		Handler handler = new Handler(getContext().getMainLooper());
		
		handler.post(new Runnable()
		{
			public void run()
			{
				deliverResult(tweets);
			}
		});
	}
}
