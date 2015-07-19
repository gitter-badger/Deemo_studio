package com.sevenre.trackre.vehicle.utils;

public class Log {
	public static void e (String s) {
		if (s==null)
			return;
		if (Constants.isDev)
			android.util.Log.e("Raj", s);
	}
}
