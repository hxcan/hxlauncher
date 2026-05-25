package com.stupidbeauty;

import com.android.volley.toolbox.HttpStack;

public class GsonHttpManager
{
	public static GsonHttpManager mGsonHttpManager;

	private HttpStack httpStack; //!<The http stack object to be used.
	
	public static GsonHttpManager shareInstance()
	{
		if(mGsonHttpManager == null)
		{
			mGsonHttpManager = new GsonHttpManager();
		}
		return mGsonHttpManager;
	}
	
	public HttpStack getHttpStack()
	{
		return httpStack;
	}

}
