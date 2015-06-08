package com.sevenre.trackre.driver.datatypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Stop {

	String name, time, date, no, type, id;
	double lat, lng;
	boolean isReached;

	public Stop(String id, String name, String time, String no, String type,
			String lat, String lng) {
		super();
		this.id = id;
		this.name = name;
		this.time = time;
		this.no = no;
		this.type = type;
		this.lat = Double.parseDouble(lat);
		this.lng = Double.parseDouble(lng);
	}

	public Stop() {

	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			java.util.Date date = (java.util.Date)formatter.parse(time);
			SimpleDateFormat formatter_ = new SimpleDateFormat("hh:mm a");
			return formatter_.format(date).toUpperCase();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public Date get_time() {
		Date date = new Date();
		date.setTime(System.currentTimeMillis());
		date.setMinutes(Integer.parseInt(time.substring(3, 4)));
		date.setHours(Integer.parseInt(time.substring(0, 1)));
		if (time.toLowerCase().contains("pm")) {
			date.setHours(date.getHours() + 12);
		}
		return date;
	}

	public String getTime12() {
		String time = "";
		return time;
	}

	public String getServerTime() {
		return time;
	}

	@Override
	public String toString() {
		String result = "" + getId() + " " + getName() + " " + getNo();
		return result;
	}

	public boolean isReached() {
		return isReached;
	}

	public void setReached(boolean isReached) {
		this.isReached = isReached;
	}

}
