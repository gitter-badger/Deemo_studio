package com.sevenre.trackre.vehicle.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class Constants {

	//App control
	public static final int USER_MODE = 1;
	public static final int SETUP_MODE = 2;
	public static final int DEVELOPER_MODE = 3;
	public static final int MODE = DEVELOPER_MODE;
	
	// constants that are used in app for sharing variable between classes
	public static final int VEHICLE_NO_LENGTH = 4;
	public static String VEHICLE_NO = null;
	public static boolean isLoginSuccess = false;
	public static boolean isExceptionalError = false;
	public static Exception exceptionError = null;
	public static final boolean isDev = true;
	public static boolean isAuthentic = true;
	
	//colors	
	public static final ColorDrawable ACTION_BAR_COLOR_DRAWABLE = new ColorDrawable(Color.parseColor("#00BCD4"));

	// Select Trip
    public static final String TRIP = "com.sevenre.trackre.triptype";
	public static final int TRIP_DROP = 1;
	public static final int TRIP_PICK_UP = 2;
}
