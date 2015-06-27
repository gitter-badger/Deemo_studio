package com.sevenre.trackre.vehicle.datatypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Trip {

	String name, time, type, displayName, tripId, tripStatus;
	boolean isTagged;

	public Trip(String tripId, String name, String time, String displayName,
			String type, String tripStatus, boolean isTagged) {
		super();
		this.tripId = tripId;
		this.name = name;
		this.time = time;
		this.type = type;
		this.displayName = displayName;
		this.isTagged = isTagged;
		this.tripStatus = tripStatus;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
	
	public String getTripStatus() {
		return tripStatus;
	}

	public void setTripStatus(String tripStatus) {
		this.tripStatus = tripStatus;
	}

	public boolean isTagged() {
		return isTagged;
	}

	public void setTagged(boolean isTagged) {
		this.isTagged = isTagged;
	}

	public String getTime() {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			java.util.Date date = (java.util.Date)formatter.parse(time);
			SimpleDateFormat formatter_ = new SimpleDateFormat("hh:mm a");
			return formatter_.format(date).toUpperCase();
		} catch (ParseException e) {
			//e.printStackTrace();
		}
		return time;
	}

	public String getTime_server() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
