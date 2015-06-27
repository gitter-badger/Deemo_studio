package com.sevenre.trackre.vehicle.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;

import com.sevenre.trackre.vehicle.network.TaggingService;
import com.sevenre.trackre.vehicle.network.TrackService;

public class Utils {

	public static final String STATUS_CHANGED_INTENT = "com.sevenre.trackre.vehicle.STATUS_CHANGE";
	public static final String STATUS = "status";
	
	// Shared Preference
	public static final String PREFERENCE = "com.sevenre.trackre.vehicle.pref";
	public static final String VEHICLE_ID = "com.sevenre.trackre.vehicle.pref.vehicleId";
	public static final String VEHICLE_NO = "com.sevenre.trackre.vehicle.pref.vehicleNo";
	public static final String TRIP_ID = "com.sevenre.trackre.vehicle.pref.tripId";
	public static final String SCHOOL_ID = "com.sevenre.trackre.vehicle.pref.schoolId";
	public static final String TRIP_STATUS = "com.sevenre.trackre.vehicle.pref.tripStatus";
	public static final String ERROR = "error";
	public static final String CUSTOMER_NO = "com.sevenre.trackre.vehicle.pref.customerno";
	public static final String SERVER_MOBILE_NUMBER = "com.sevenre.trackre.vehicle.pref.servermobilenumber";
	public static final String PASS_CODE = "com.sevenre.trackre.vehicle.pref.passcode";
	public static final String SCHOOL_INFO = "com.sevenre.trackre.vehicle.pref.schoolinfo";
	public static final String DRIVER_ID = "com.sevenre.trackre.vehicle.pref.driverid";
	public static final String NEXT_STOP_INTENT = "com.sevenre.trackre.vehicle.pref.next_stop_intent";
	public static final String NEXT_STOP = "com.sevenre.trackre.vehicle.pref.update_stop";
	
	// font name
	public static final int roboto = 1;
	public static final int robotoCondensed = 2;
	public static final int helvetica = 3;
	public static final int gothic = 4;
	public static final int gothicLight = 5;
	public static final int erasBold = 6;

	//audio notification
	public static final int no_internet = 1;
	public static final int no_gps = 2;
	public static final int unknown_error = 3;
	public static final int try_again = 4;
	public static final int student_found = 5;
	public static final int student_not_found = 6;
	
	//theme color
	public static final int Theme_light = 1;
	public static final int Theme_medium = 2;
	public static final int Theme_dark = 3;
	public static final int Theme_red = 4;
	
	
	public static Intent intentForNetworkAccess() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
		return intent;
	}

	public static Intent intentForGPS() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		return intent;
	}
	
	public static Typeface getTypeFace(AssetManager assets, int id) {
		Typeface t = Typeface.createFromAsset(assets, "fonts/roboto.ttf");
		switch (id) {
		case roboto:
			t = Typeface.createFromAsset(assets, "fonts/roboto.ttf");
			break;
		case robotoCondensed:
			t = Typeface.createFromAsset(assets,"fonts/robotoCondensedRegular.ttf");
			break;
		case helvetica:
			t = Typeface.createFromAsset(assets, "fonts/roboto.ttf");
			break;
		case gothic:
			t = Typeface.createFromAsset(assets, "fonts/gothi720bt.ttf");
			break;
		case gothicLight:
			t = Typeface.createFromAsset(assets, "fonts/gothic720ltbtlight.ttf");
			break;
		case erasBold:
			t = Typeface.createFromAsset(assets, "fonts/erasBold.TTF");
			break;
		}
		return t;
	}

	public static boolean isServiceRunning(Context context)
	{
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (TrackService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;	
	}
	
	public static boolean isTaggingServiceRunning (Context context)
	{
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (TaggingService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;	
	}
	
	public static int getColor(int color) {
		switch (color) {
		case Theme_light:
			return Color.rgb(90,93,110);
		case Theme_medium:
			return Color.rgb(58,74,90);
		case Theme_dark:
			return Color.rgb(51,58,77);
		case Theme_red:
			return Color.rgb(253, 132, 131);
		}
		return 0;

	}
}
