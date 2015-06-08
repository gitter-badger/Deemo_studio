package com.sevenre.trackre.driver.datatypes;

public class TaggingStop extends Stop {

	boolean isTagged = false;
	String dbId, tripId;
	double speed;

	public TaggingStop(String id, String name, String time, String no,
			String type, String lat, String lng) {
		super(id, name, time, no, type, lat, lng);
	}

	public TaggingStop() {
		// TODO Auto-generated constructor stub
	}

	public boolean isTagged() {
		return isTagged;
	}

	public void setTagged(boolean isTagged) {
		this.isTagged = isTagged;
	}

	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
	
	@Override
	public String toString() {
		String result = "" + getDbId() + " " + getTripId() +  " " + getName() + " " +
				lat + " " + lng + " " + speed;
			//getLat() + " " + getLng() + " " + getSpeed();
		return  result ;
	}
}
