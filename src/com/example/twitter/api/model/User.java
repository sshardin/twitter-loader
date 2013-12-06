package com.example.twitter.api.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable
{
	public static final String KEY_ID   = "id";
	public static final String KEY_NAME = "screen_name";
	
	public User()
	{
		
	}
	
	private long id;
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	private String name;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 *  Parceling
	 */

	private User(Parcel parcel)
	{
		id   = parcel.readLong();
		name = parcel.readString();
	}
	
	@Override
	public void writeToParcel(Parcel parcel, int flags)
	{
		parcel.writeLong(id);
		parcel.writeString(name);
	}
	
	public static final Parcelable.Creator<User> CREATOR =
			new Parcelable.Creator<User>()
			{
				public User createFromParcel(Parcel parcel)
				{
					return new User(parcel);
				}

				public User[] newArray(int size)
				{
					return new User[size];
				}
			};
			
	@Override
	public int describeContents()
	{
		return 0;
	}
}
