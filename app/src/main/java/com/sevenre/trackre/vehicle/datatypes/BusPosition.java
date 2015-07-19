package com.sevenre.trackre.vehicle.datatypes;

public class BusPosition {

	public static String  mDeviceId = "1111111";
	String mLat = "", mLng = "", mSpeed = "", mTime = "", mDate = "", tripId = "tripId", tripMode = "track",id = "-1";

	public BusPosition(String mLat, String mLng, String mSpeed, String mTime,
			String mDate, String tripId, String tripMode) {
		super();
		this.mLat = mLat;
		this.mLng = mLng;
		this.mSpeed = mSpeed;
		this.mTime = mTime;
		this.mDate = mDate;
		this.tripId = tripId;
		this.tripMode = tripMode;
	}

	public BusPosition() {

	}

	public static String getDeviceId() {
		return mDeviceId;
	}

	public static void setDeviceId(String mDeviceId) {
		BusPosition.mDeviceId = mDeviceId;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getLat() {
		return mLat;
	}

	public void setLat(String mLat) {
		this.mLat = mLat;
	}

	public String getLng() {
		return mLng;
	}

	public void setLng(String mLng) {
		this.mLng = mLng;
	}

	public String getSpeed() {
		return mSpeed;
	}

	public void setSpeed(String mSpeed) {
		this.mSpeed = mSpeed;
	}

	public String getTime() {
		return mTime;
	}

	public void setTime(String mTime) {
		this.mTime = mTime;
	}

	public String getDate() {
		return mDate;
	}

	public void setDate(String mDate) {
		this.mDate = mDate;
	}

	public String getTripMode() {
		return tripMode;
	}

	public void setTripMode(String tripMode) {
		this.tripMode = tripMode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		String result = getId() + " " + getTripId() + " " + getTripMode() + " " + getDate() + " " + getTime()
				+ " " + getLat() + " " + getLng() + " " + getSpeed() ;
		return result;
	}
}