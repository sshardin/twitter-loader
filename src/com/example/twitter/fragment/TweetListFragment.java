package com.example.twitter.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.json.R;
import com.example.twitter.adapter.TweetAdapter;
import com.example.twitter.api.UserTimeline;
import com.example.twitter.api.model.Tweet;
import com.example.twitter.loader.UserTimelineLoader;

public class TweetListFragment extends ListFragment
implements LoaderManager.LoaderCallbacks<List<Tweet>>
{
	private String user;
	private UserTimelineLoader userTimelineLoader;
	
	private View loadMoreTweets;
	private TextView loadMoreTweetsText;
	private ProgressBar loadMoreTweetsProgress;

	public TweetListFragment()
	{
		// Required empty public constructor
	}

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		userTimelineLoader = (UserTimelineLoader)
				getLoaderManager().initLoader(R.id.user_timeline_loader, null, this);
	}
	
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		createLoadMoreTweetsView();
		createListAdapter();
	}
	
	private void createListAdapter()
	{
		UserTimeline userTimeline = userTimelineLoader.getUserTimeline();
		
		if(userTimeline != null)
		{
			setListAdapter(new TweetAdapter(
					getActivity(),
					R.layout.twitter_row,
					userTimeline.getTweets()));
		}
		else
		{
			setListAdapter(new TweetAdapter(
					getActivity(),
					R.layout.twitter_row,
					new ArrayList<Tweet>()));
		}
	}
	
	private void createLoadMoreTweetsView()
	{
		loadMoreTweets = getLayoutInflater(null).inflate(R.layout.load_tweets, null);
		loadMoreTweets.setVisibility(View.GONE);
		
		loadMoreTweetsText = (TextView) loadMoreTweets.findViewById(R.id.load_more_tweets);
		loadMoreTweetsProgress = (ProgressBar) loadMoreTweets.findViewById(R.id.load_tweets_progress);
		
		loadMoreTweets.setOnClickListener(new LoadMoreTweetsClickListener());
		
		getListView().addFooterView(loadMoreTweets);
	}
	
	private void configureLoadMoreTweets(boolean loading)
	{
		loadMoreTweets.setVisibility(View.VISIBLE);
		
		if(loading)
		{
			loadMoreTweetsText.setText(R.string.loading_more_tweets);
			loadMoreTweetsProgress.setVisibility(View.VISIBLE);
		}
		else
		{
			loadMoreTweetsText.setText(R.string.load_more_tweets);
			loadMoreTweetsProgress.setVisibility(View.INVISIBLE);
		}
	}
	
	private class LoadMoreTweetsClickListener implements OnClickListener
	{
		public void onClick(View view)
		{
			configureLoadMoreTweets(true);
			
			userTimelineLoader.loadMoreTweets();
		}
	}

	public void changeUser(String user)
	{
		if(this.user != null && this.user.equals(user)) return;

		this.user = user;

		getLoaderManager().restartLoader(R.id.user_timeline_loader, null, this);
		
		configureLoadMoreTweets(true);
	}

	@Override
	public Loader<List<Tweet>> onCreateLoader(int id, Bundle arg1)
	{
		if(id == R.id.user_timeline_loader)
		{
			userTimelineLoader = new UserTimelineLoader(getActivity(), user);
			
			createListAdapter();
			
			return userTimelineLoader;
		}
		
		return null;
	}

	@Override
	public void onLoadFinished(Loader<List<Tweet>> loader, List<Tweet> tweets)
	{
		TweetAdapter adapter = (TweetAdapter) getListAdapter();

		if(adapter == null || tweets == null || tweets.size() == 0) return;
		
		adapter.notifyDataSetChanged();
		
		configureLoadMoreTweets(false);
	}

	@Override
	public void onLoaderReset(Loader<List<Tweet>> loader)
	{
		
	}
}
