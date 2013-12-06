
package com.example.twitter.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.EditText;

import com.example.json.R;
import com.example.twitter.fragment.TweetListFragment;

public class MainActivity extends FragmentActivity
{
	private TweetListFragment tweetListFragment;
	
	private EditText usernameText;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		usernameText = (EditText) findViewById(R.id.username);
		
		usernameText.setText("RealHumanPraise");
	}
	
	public void search(View v)
	{
		String username = usernameText.getText().toString();
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		tweetListFragment = (TweetListFragment) fragmentManager.findFragmentById(R.id.tweet_list_fragment);
		
		tweetListFragment.changeUser(username);
	}
}
