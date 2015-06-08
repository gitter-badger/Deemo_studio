package com.sevenre.trackre.driver.utils;

public class Log {
	public static void e (String s) {
		if (Constants.isDev)
			android.util.Log.e("Raj", s);
	}
}
