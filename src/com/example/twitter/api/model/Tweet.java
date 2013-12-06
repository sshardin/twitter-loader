package com.example.twitter.api.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *  This class is used to hold a Tweet
 */
public class Tweet implements Parcelable
{
	public static final String KEY_ID   = "id";
	public static final String KEY_TEXT = "text";
	public static final String KEY_DATE = "created_at";
	
	public Tweet()
	{
		
	}
	
	/**
	 *   A unique long ID to identify the Tweet
	 */
	private long id;
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	/**
	 *  A string to represent the date
	 */
	private String date;
	
	public String getDate()
	{
		return date;
	}
	
	public void setDate(String date)
	{
		this.date = date;
	}
	
	/**
	 *  A string to represent the text
	 */
	private String text;
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	/**
	 *  Parceling
	 */

	private Tweet(Parcel parcel)
	{
		id   = parcel.readLong();
		text = parcel.readString();
		date = parcel.readString();
	}
	
	@Override
	public void writeToParcel(Parcel parcel, int flags)
	{
		parcel.writeLong(id);
		parcel.writeString(text);
		parcel.writeString(date);
	}
	
	public static final Parcelable.Creator<Tweet> CREATOR =
			new Parcelable.Creator<Tweet>()
			{
				public Tweet createFromParcel(Parcel parcel)
				{
					return new Tweet(parcel);
				}

				public Tweet[] newArray(int size)
				{
					return new Tweet[size];
				}
			};
			
	@Override
	public int describeContents()
	{
		return 0;
	}
}
