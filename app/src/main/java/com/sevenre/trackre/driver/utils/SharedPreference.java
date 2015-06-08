package com.sevenre.trackre.driver.utils;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreference {

	public static String getVehicleId(Context c) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		return pref.getString(Utils.VEHICLE_ID, null);
	}

	public static void setVehicleId(Context c, String vehicleId) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		Editor editor = pref.edit();
		editor.putString(Utils.VEHICLE_ID, vehicleId);
		editor.commit();
	}
	
	public static String getVehicleNo(Context c) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		return pref.getString(Utils.VEHICLE_NO, null);
	}

	public static void setVehicleNo(Context c, String vehicleNo) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		Editor editor = pref.edit();
		editor.putString(Utils.VEHICLE_NO, vehicleNo.toUpperCase());
		editor.commit();
	}
	
	public static String getTripId (Context c) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		return pref.getString(Utils.TRIP_ID,  Utils.ERROR);
	}
	
	public static void setTripId (Context c, String tripId) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		Editor editor = pref.edit();
		editor.putString(Utils.TRIP_ID, tripId);
		editor.commit();
	}
	
	public static String getTripStatus (Context c) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		return pref.getString(Utils.TRIP_STATUS,  Utils.ERROR);
	}
	
	public static void setTripStatus (Context c, String tripStatus) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		Editor editor = pref.edit();
		editor.putString(Utils.TRIP_STATUS, tripStatus);
		editor.commit();
	}
	
	public static void setSchoolId (Context c, String schoolId) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		Editor editor = pref.edit();
		editor.putString(Utils.SCHOOL_ID, schoolId);
		editor.commit();
	}
	
	public static String getSchoolId (Context c) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		return pref.getString(Utils.SCHOOL_ID, Utils.ERROR);
	}
	
	public static String getServerMobileNumber (Context c) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		return pref.getString(Utils.SERVER_MOBILE_NUMBER, Utils.ERROR);
	}
	
	public static void setServerMobileNumber (Context c, String number) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		Editor editor = pref.edit();
		editor.putString(Utils.SERVER_MOBILE_NUMBER, number);
		editor.commit();
	}
	
	public static String getCustomerNo (Context c) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		return pref.getString(Utils.CUSTOMER_NO, Utils.ERROR);
	}
	
	public static void setCustomerNo (Context c, String number) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		Editor editor = pref.edit();
		editor.putString(Utils.CUSTOMER_NO, number);
		editor.commit();
	}
	
	public static int getPassCode (Context c) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		return pref.getInt(Utils.PASS_CODE, -1);
	}
	
	public static void setPassCode (Context c, int passcode) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		Editor editor = pref.edit();
		editor.putInt(Utils.PASS_CODE, passcode);
		editor.commit();
	}
	
	public static void setSchoolInfo (Context c,String info) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		Editor editor = pref.edit();
		editor.putString(Utils.SCHOOL_INFO, info);
		editor.commit();
	}
	
	public static String getSchoolInfo (Context c) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		return pref.getString(Utils.SCHOOL_INFO, Utils.ERROR);
	}
	
	public static String getSchoolName (Context c) {
		String name = "";
		try {
			JSONObject object = new JSONObject(getSchoolInfo(c));
			if (object.has("success")&& object.getBoolean("success")) {
				return object.getJSONObject("data").getJSONObject("school").getString("name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public static String getSchoolCity (Context c) {
		String name = "";
		try {
			JSONObject object = new JSONObject(getSchoolInfo(c));
			if (object.has("success")&& object.getBoolean("success")) {
				return object.getJSONObject("data").getJSONObject("school").getString("city");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	public static void setDriverId(Context c, int driverId) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		Editor editor = pref.edit();
		editor.putInt(Utils.DRIVER_ID, driverId);
		editor.commit();
	}
	
	public static int getDriverId (Context c) {
		SharedPreferences pref = c.getSharedPreferences(Utils.PREFERENCE, 0);
		return pref.getInt(Utils.DRIVER_ID, -1);
	}
}
