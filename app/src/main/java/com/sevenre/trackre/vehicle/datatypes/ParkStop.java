package com.sevenre.trackre.vehicle.datatypes;

public class ParkStop {

	double lat, lng;
	String name;
	int id;

	public ParkStop(double lat, double lng, String name, int id) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.name = name;
		this.id = id;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
