package com.turbomeme.servlet;

public final class Environment
{
	public static final String HOST;

	public static String PORT;

	static
	{
		HOST = System.getProperty("turbomemeHost");

		if (HOST == null)
		{
			throw new IllegalArgumentException("Host cannot be null!");
		}
		else if (HOST.isEmpty())
		{
			throw new IllegalArgumentException("Host cannot be empty!");
		}

		PORT = System.getProperty("turbomemePort");

		if (PORT == null)
		{
			PORT = "";
		}
	}

	public static String createRootURL()
	{
		String url = "http://" + Environment.HOST;

		if (Environment.PORT != null && !Environment.PORT.isEmpty())
		{
			url = url + ":" + Environment.PORT;
		}

		url = url + "/";
		return url;
	}
}