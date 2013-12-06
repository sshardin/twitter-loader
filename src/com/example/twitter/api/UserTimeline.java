package com.example.twitter.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.twitter.api.model.Tweet;
import com.example.twitter.api.parser.TwitterParser;

public class UserTimeline implements Parcelable
{
	public static final String REQUEST_URL    = "statuses/user_timeline.json";
	public static final String REQUEST_METHOD = "GET";
	
	public static final String PARAM_USER  = "screen_name";
	public static final String PARAM_SINCE = "since_id";
	public static final String PARAM_MAX   = "max_id";
	public static final String PARAM_COUNT = "count";
	
	public static final int DEFAULT_COUNT =  20;
	public static final int MAX_COUNT     = 200;
	
	private static int constrainCount(int count)
	{
		if(count > MAX_COUNT) return MAX_COUNT;
		if(count < 0)         return  0;
		
		return count;
	}
	
	private String user;
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	
	public UserTimeline(String user)
	{
		this.user = user;
	}
	
	public String getUser()
	{
		return user;
	}
	
	public List<Tweet> getTweets()
	{
		return tweets;
	}
	
	private TwitterRequest createBaseRequest()
	{
		TwitterRequest request = new TwitterRequest(REQUEST_URL, REQUEST_METHOD);
		
		request.appendQuery(PARAM_USER, getUser());
		request.appendQuery(PARAM_COUNT, constrainCount(DEFAULT_COUNT));
		
		return request;
	}
	
	private List<Tweet> getInitialTweets() throws IOException, JSONException
	{
		if(tweets.size() != 0) return null;
			
		TwitterRequest request = createBaseRequest();
		
		String json = request.execute();
		
		if(json == null) return null;
		
		List<Tweet> initialTweets = TwitterParser.parseTweet(json);
		tweets.addAll(initialTweets);
		
		return initialTweets;
	}
	
	public List<Tweet> getNewTweets() throws IOException, JSONException
	{
		if(tweets.size() == 0) return getInitialTweets();
		
		List<Tweet> batchOfNewTweets;
		List<Tweet> newTweets = new ArrayList<Tweet>();
		
		do
		{
			TwitterRequest request = createBaseRequest();
			request.appendQuery(PARAM_SINCE, tweets.get(0).getId());

			if(newTweets.size() != 0)
			{
				request.appendQuery(PARAM_MAX, newTweets.get(newTweets.size() - 1).getId() - 1);
			}

			String json = request.execute();

			if(json == null) return null;

			batchOfNewTweets = TwitterParser.parseTweet(json);
			newTweets.addAll(batchOfNewTweets);
		}
		while(batchOfNewTweets.size() == DEFAULT_COUNT);
		 
		tweets.addAll(0, newTweets);
		
		return newTweets;
	}
	
	public List<Tweet> getOldTweets() throws IOException, JSONException
	{
		if(tweets.size() == 0) return getInitialTweets();
		
		TwitterRequest request = createBaseRequest();
		request.appendQuery(PARAM_MAX, tweets.get(tweets.size() - 1).getId() - 1);
		
		String json = request.execute();
		
		if(json == null) return null;
		
		List<Tweet> oldTweets = TwitterParser.parseTweet(json);
		tweets.addAll(oldTweets);
		
		return oldTweets;
	}
	
	/**
	 *  Poller
	 */
	
	public interface Poller
	{
		public static final int DEFAULT_POLL_PERIOD = 60000;
		
		public void start();
		public void stop();
		
		public interface Listener
		{
			public void newTweetsPolled(List<Tweet> tweets);
		}
		
		public Listener getListener();
		public void setListener(Listener listener);
	}
	
	private Poller poller;
	
	public Poller getPoller()
	{
		if(poller == null)
		{
			poller = new UserTimelinePoller(this);
		}
		
		return poller;
	}
	
	public void startPoller()
	{
		getPoller().start();
	}
	
	public void stopPoller()
	{
		getPoller().stop();
	}
	
	public Poller.Listener getPollerListener()
	{
		return getPoller().getListener();
	}
	
	public void setPollerListener(Poller.Listener listener)
	{
		getPoller().setListener(listener);
	}
	
	/**
	 *  Parceling
	 */
	
	private UserTimeline(Parcel parcel)
	{
		user = parcel.readString();
		parcel.readTypedList(tweets, Tweet.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags)
	{
		parcel.writeString(user);
		parcel.writeTypedList(tweets);
	}
	
	public static final Parcelable.Creator<UserTimeline> CREATOR =
			new Parcelable.Creator<UserTimeline>()
			{
				public UserTimeline createFromParcel(Parcel parcel)
				{
					return new UserTimeline(parcel);
				}

				public UserTimeline[] newArray(int size)
				{
					return new UserTimeline[size];
				}
			};
			
	@Override
	public int describeContents()
	{
		return 0;
	}
}

class UserTimelinePoller implements UserTimeline.Poller, Runnable
{
	private UserTimeline userTimeline;
	
	private Thread thread;
	private boolean running = false;
	
	UserTimelinePoller(UserTimeline userTimeline)
	{
		this.userTimeline = userTimeline;
	}
	
	public void start()
	{
		if(running) return;
		
		running = true;
		
		thread = new Thread(this);
		thread.start();
	}
	
	public void run()
	{
		while(running)
		{
			try
			{
				List<Tweet> newTweets = userTimeline.getNewTweets();
				
				if(newTweets != null && newTweets.size() > 0)
				{
					notifyListener(newTweets);
				}
				
				Thread.sleep(UserTimeline.Poller.DEFAULT_POLL_PERIOD);
			}
			catch(InterruptedException e) { }
			catch(IOException e) { }
			catch(JSONException e) { }
		}
	}
	
	public void stop()
	{
		if(!running) return;
		
		running = false;
		thread.interrupt();
	}
	
	private UserTimeline.Poller.Listener listener;
	
	public UserTimeline.Poller.Listener getListener()
	{
		return listener;
	}
	
	public void setListener(UserTimeline.Poller.Listener listener)
	{
		this.listener = listener;
	}
	
	private void notifyListener(List<Tweet> newTweets)
	{
		if(listener != null)
		{
			listener.newTweetsPolled(newTweets);
		}
	}
}
