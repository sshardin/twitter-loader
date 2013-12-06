package com.example.twitter.api.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class IOUtil
{
	public static byte[] toByteArray(InputStream in) throws IOException
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		
		int count;
		byte[] buff = new byte[1024];
		
		while((count = in.read(buff)) != -1)
		{
			bytes.write(buff, 0, count);
		}
		
		return bytes.toByteArray();
	}
}
