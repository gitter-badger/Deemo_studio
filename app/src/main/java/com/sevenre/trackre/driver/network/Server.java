package com.sevenre.trackre.driver.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

import com.sevenre.trackre.driver.datatypes.BusPosition;
import com.sevenre.trackre.driver.datatypes.ParkStop;
import com.sevenre.trackre.driver.datatypes.Stop;
import com.sevenre.trackre.driver.datatypes.TaggingStop;
import com.sevenre.trackre.driver.datatypes.Trip;
import com.sevenre.trackre.driver.utils.Constants;
import com.sevenre.trackre.driver.utils.Utils;

@SuppressLint("SimpleDateFormat") public class Server {
	//http://www.trackre.net/school/ws01/driverapi/1.0/vehicle/gettrips/?schoolId=2&vehicleId=3&tripType=pickup

	final static String base_url = "http://www.trackre.net/school/ws02/driverapi/1.0/";
	
	final static String authenticate = base_url + "/authenticate/";
	final static String authenticateCustomer = authenticate + "customer?customerNo=";
	final static String authenticateVehicle = authenticate + "vehicle?";
	final static String authenticateDriver = authenticate + "driverPassCode?";
	
	final static String vehicle = base_url + "vehicle/";
	final static String drop = vehicle + "gettrips?tripType=drop&";
	final static String pickUp = vehicle + "gettrips?tripType=pickup&";
	
	final static String gettimetable =base_url+"trip/getTimetable?tripId=";
	
	final static String daylog = base_url + "daylog/";
	final static String starttrip = daylog + "startTrip?";
	final static String completetrip = daylog + "completeTrip?";
	final static String tagstop = daylog + "tagStop?";
	final static String tagposition = daylog + "tagPosition?";
	
	final static String school = base_url + "school/";
	final static String tagParkstation = school + "tagParkstation?";
	final static String getParkStation = school + "getParkstations?schoolId=";
	final static String driverPassCode = base_url + "authenticate/driverPassCode?passCode=";
	final static String getSchoolInfo = school + "getSchoolInfo?schoolId=";
	
	//variables in json
	final static String SUCCESS = "success", VEHICLE_ID = "vehicleId", DATA = "data", TRIPS = "trips",
			ROUTE_ID = "routeId", ROUTE_DISPLAY_NAME = "routeNameDispval", TRIP_ID = "tripId",
			TRIP_TYPE = "tripType", TRIP_NAME_DISP = "tripNameDispval", TRIP_STATUS = "tripStatus",
			TIME = "time", ERROR = "error", TIMETABLE = "timetable", STOPTIME="stopTime";
	final static String STOP_ID = "stopId", STOP_TYPE = "stopType", STOP_NO = "stopNo", SCHOOL = "school",
			STOP_DISP_NAME = "stopNameDispval", LAT = "stopLat", LNG = "stopLng", SCHOOL_ID = "schoolId", 
			PARKSTATIONS = "parkStations", VEHICLE = "vehicle", STARTTIME="startTime", 
			ROUTEDISPLAYNAME="routeNameDispval", DRIVER="driver", DRIVERID="driverId",
			TRIPDESCRIPTION="tripDescription";
	
	//-----------------------------------------------------
	
	
	public static String authenticateApp (String customerNo) {
		String url = authenticateCustomer + customerNo;
		String result = readFromUrl(url);
		if (result==null) { 
			return null;
		}
		JSONObject object;
		try {
			object = new JSONObject(result);
			if(object.has(SUCCESS) && object.getBoolean(SUCCESS)) {
				if (object.has(DATA)) {
					object = object.getJSONObject(DATA);
					return object.getJSONObject(SCHOOL).getString(SCHOOL_ID);
				}
			} else 
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean startTrip(Trip t, double mLat, double mLng, double mSpeed, int driverId) {	
		// $required_params = array('tripId','tripStatus','date','time','lat','long','speed');
		String url = starttrip + "tripId=" + t.getTripId() +
				"&tripStatus=" + t.getTripStatus() +
				"&time=" + getTime() +
				"&date=" + getDate() +
				"&lat=" + mLat +
				"&lng=" +mLng +
				"&speed=" + mSpeed +
				"&driverId=" + driverId;
		
		String result = readFromUrl(url);
		JSONObject object;
		System.out.println(url);
		System.out.println(result);
		if (result==null )
			return false;
		
		try {
			object = new JSONObject(result);
			if(object.has(SUCCESS) && object.getBoolean(SUCCESS)) {
				return true;
			} else 
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean completeTrip(String tripId, String status, String time, String date, String lat,
			String lng, String speed, String activity,  int driverId, boolean isLive) {
		//$required_params = array('tripId','tripStatus','date','time','lat','long','speed');

		String url = completetrip + "tripId=" + tripId +
				"&tripStatus=" + status +
				"&time=" + time +
				"&date=" + date +
				"&lat=" +lat +
				"&lng=" + lng +
				"&speed=" + speed +
				"&driverId=" + driverId +
				"&isLive=" + isLive;
		
		String result = readFromUrl(url);
		JSONObject object;
		
		if (result!=null) {
			try {
				object = new JSONObject(result);
				if(object.has(SUCCESS) && object.getBoolean(SUCCESS)) {
					return true;
				} else 
					return false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static boolean tagPosition(BusPosition pos, boolean isLive, int stopReached) {
		//$required_params = array('tripId','tripStatus','date','time','lat','long','speed');
		String url = tagposition + "tripId=" + pos.getTripId() +
				"&tripStatus=" + pos.getTripStatus() +
				"&time=" + pos.getTime() +
				"&date=" + pos.getDate() +
				"&lat=" + pos.getLat() +
				"&lng=" + pos.getLng() +
				"&speed=" + pos.getSpeed() + 
				"&isLive=" + isLive + 
				"&e=" + (stopReached+1);
		
		String result = readFromUrl(url);
		if(result!= null) {
			JSONObject object;
			try {
				object = new JSONObject(result);
				if(object.has(SUCCESS) && object.getBoolean(SUCCESS)) {
					return true;
				} else 
					return false;
			} catch (Exception e) {
				e.printStackTrace();
				Constants.exceptionError = e;
				Constants.isExceptionalError = true;
			}
		}
		return false;
	}
	
	public static boolean tagStop(TaggingStop t,  boolean isLive) {
		//$required_params = array('tripId','stopId','date','time','lat','long','speed');
		String url = tagstop + "tripId=" + t.getTripId() +
				"&stopNo=" + t.getId() +
				"&stopId=" + t.getId() +
				"&time=" + t.getTime() +
				"&date=" + t.getDate() +
				"&lat=" + t.getLat() +
				"&lng=" + t.getLng() +
				"&speed=" + t.getSpeed() + 
				"&isLive=" + isLive;
		String result = readFromUrl(url);
		System.out.println(url);
		System.out.println(result);
		if (result!=null) {
			JSONObject object;
			try {
				object = new JSONObject(result);
				if(object.has(SUCCESS) && object.getBoolean(SUCCESS)) {
					return true;
				} else 
					return false;
			} catch (Exception e) {
				e.printStackTrace();
				Constants.exceptionError = e;
				Constants.isExceptionalError = true;
			}
		}
		return false;
	}

	public static String getVehicleId(String vehicleNo, String schollId) {
		String url = authenticateVehicle + "vehicleNo=" + vehicleNo + "&schoolId=" + schollId;
		String result = readFromUrl(url);
		JSONObject object;
		if (result==null)
			return Utils.ERROR;
		try {
			object = new JSONObject(result);
			if(object.has(SUCCESS) && object.getBoolean(SUCCESS)) {
				JSONObject data = object.getJSONObject(DATA);
				Constants.isLoginSuccess = true;
				return data.getJSONObject(VEHICLE).getString(VEHICLE_ID);
			} else if(object.has(SUCCESS)){
				Constants.isLoginSuccess = false;
				return object.getString(ERROR);
			}
			else {
				Constants.isLoginSuccess = false;
				return "Unknown error occured"; 
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Constants.exceptionError = e;
			Constants.isExceptionalError = true;
			return "Unknown error occured";
		}
		
	}
	
	public static ArrayList<Trip> getPickUpTrip(String vechileId, String schoolId) {
		String url = pickUp + VEHICLE_ID + "=" + vechileId + "&schoolId=" + schoolId;
		//System.out.println(url);
		String result = readFromUrl(url);
		//System.out.println(result);

		ArrayList<Trip> tirpList = new ArrayList<Trip>();
		if (result == null) {
			return tirpList;
		}
		JSONObject object;
		
		try {
			object = new JSONObject(result);
			if(object.has(SUCCESS) && object.getBoolean(SUCCESS)) {
				JSONArray trips = (object.getJSONObject(DATA)).getJSONArray(TRIPS);
				for (int i = 0; i < trips.length(); i++) {
					 JSONObject o = trips.getJSONObject(i);
					 String name = o.getString(ROUTE_DISPLAY_NAME);
					 String time = o.getString(STARTTIME);
					 String title = o.getString(TRIPDESCRIPTION);
					 String tag = o.getString(TRIP_STATUS);
					 String id = o.getString(TRIP_ID);
					 boolean isTagged = true;
					 if ("tag".equals(tag)) {
						 isTagged = false;
					 }
					 tirpList.add(new Trip(id,name, time, title, "PICKUP",tag, isTagged));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Constants.exceptionError = e;
			Constants.isExceptionalError = true;
		}
		return tirpList ;
	}

	public static ArrayList<Trip> getDropTrip(String vehicleID, String schoolId) {
		
		String url = drop + VEHICLE_ID +"="+ vehicleID + "&schoolId=" + schoolId;
		String result = readFromUrl(url);
		System.out.println(url);
		System.out.println(result);
		ArrayList<Trip> tirpList = new ArrayList<Trip>();
		if (result==null) {
			return tirpList;
		}
		JSONObject object;
		try {
			object = new JSONObject(result);
			if(object.has(SUCCESS) && object.getBoolean(SUCCESS)) {
				JSONArray trips = (object.getJSONObject(DATA)).getJSONArray(TRIPS);
				for (int i = 0; i < trips.length(); i++) {
					JSONObject o = trips.getJSONObject(i);
					 String name = o.getString(ROUTE_DISPLAY_NAME);
					 String time = o.getString(STARTTIME);
					 String title = o.getString(TRIPDESCRIPTION);
					 String tag = o.getString(TRIP_STATUS);
					 String id = o.getString(TRIP_ID);
					 boolean isTagged = true;
					 if ("tag".equals(tag)) {
						 isTagged = false;
					 }
					tirpList.add(new Trip(id, name, time, title, "DROP", tag, isTagged));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tirpList ;
	}

	public static ArrayList<Stop> getStopList(String tripId, String schoolId) {
		ArrayList<Stop> stopList = new ArrayList<Stop>();
		String url = gettimetable + tripId + "&schoolId=" + schoolId;
		String result = readFromUrl(url);
		JSONObject object; 
		if (result==null) {
			return stopList;
		}
		try {
			object = new JSONObject(result);
			if(object.has(SUCCESS) && object.getBoolean(SUCCESS)) {
				JSONArray stops = (object.getJSONObject(DATA)).getJSONArray(TIMETABLE);
				for (int i = 0; i < stops.length(); i++) {
					JSONObject o = (JSONObject) stops.get(i);
					String name = o.getString(STOP_DISP_NAME);
					String id = o.getString(STOP_ID);
					String no = o.getString(STOP_NO);
					String type = o.getString(STOP_TYPE);
					String time = o.getString(STOPTIME);
					String lat, lng;
					try {
						lat = ""+ o.getDouble(LAT);
					} catch (Exception e) {
						lat = "0.0";
					}
					try {
						lng = ""+ o.getDouble(LNG);
					}catch (Exception e) {
						lng = "0.0";
					}
					stopList.add(new Stop(id, name, time, no, type, lat, lng));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return stopList ;
	}

	public static ArrayList<ParkStop> getParkStation(String schoolId) {
		ArrayList<ParkStop> list = new ArrayList<ParkStop>();
		String url = getParkStation + schoolId;
		System.out.println(url);
		String result = readFromUrl(url);
		System.out.println(result);
		JSONObject object; 
		if (result==null) {
			return list;
		}
		try {
			object = new JSONObject(result);
			if(object.has(SUCCESS) && object.getBoolean(SUCCESS)) {
				JSONArray stops = (object.getJSONObject(DATA)).getJSONArray(PARKSTATIONS);
				for (int i = 0; i < stops.length(); i++) {
					JSONObject o = (JSONObject) stops.get(i);
					String name = o.getString(STOP_DISP_NAME);
					String id = o.getString(STOP_ID);
					String lat, lng;
					try {
						lat = ""+ o.getDouble(LAT);
					} catch (Exception e) {
						lat = "0.0";
					}
					try {
						lng = ""+ o.getDouble(LNG);
					}catch (Exception e) {
						lng = "0.0";
					}
					list.add(new ParkStop(Double.parseDouble(lat),Double.parseDouble(lng),name,Integer.parseInt(id)));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static boolean tagParkStation(ParkStop stop, String schoolId){
		String url = tagParkstation + 
				"schoolId=" + schoolId +
				"&stopId=" + stop.getId() +
				"&lat=" + stop.getLat() +
				"&lng=" + stop.getLng();
		String result = readFromUrl(url);
		if (result==null)
			return false;
		
		try {
			JSONObject object = new JSONObject(result);
			if(object.has(SUCCESS) && object.getBoolean(SUCCESS)) {
				return true;
			} else 
				return false;
		} catch (Exception e) {
			
		}
		return false;
	}

	public static ArrayList<TaggingStop> getTaggingStopList(String tripId, String schoolId) {
		ArrayList<TaggingStop> stopList = new ArrayList<TaggingStop>();
		String url = gettimetable + tripId + "&schoolId=" + schoolId;
		String result = readFromUrl(url);
		JSONObject object; 
		if (result==null) {
			return stopList;
		}
		try {
			object = new JSONObject(result);
			if(object.has(SUCCESS) && object.getBoolean(SUCCESS)) {
				JSONArray stops = (object.getJSONObject(DATA)).getJSONArray(TIMETABLE);
				for (int i = 0; i < stops.length(); i++) {
					JSONObject o = (JSONObject) stops.get(i);
					String name = o.getString(STOP_DISP_NAME);
					String id = o.getString(STOP_ID);
					String no = o.getString(STOP_NO);
					String type = o.getString(STOP_TYPE);
					String time = o.getString(TIME);
					String lat = o.getString(LAT);
					String lng = o.getString(LNG);
					stopList.add(new TaggingStop(id, name, time, no, type, lat, lng));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return stopList ;
	}

	public static String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return sdf.format(date);
	}
	
	public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return sdf.format(date);
    }
	
	public static String readFromUrl(String url) {
		BufferedReader in;
		try {
			URL url_ = new URL(url);
			URLConnection con = url_.openConnection();
			con.setConnectTimeout(15000);
			con.setReadTimeout(15000);
			InputStream is = con.getInputStream();
			
			in = new BufferedReader(new InputStreamReader(is));
			String inputLine;
			StringBuilder builder = new StringBuilder();
			while ((inputLine = in.readLine()) != null)
				builder.append(inputLine);
			in.close();
			return builder.toString();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			Constants.exceptionError = e;
			Constants.isExceptionalError = true;
		}
		return null;
	}
	
	public static int verifyDriverPassCode (String code, String schoolId) {
		String url = driverPassCode + code + "&schoolId=" + schoolId;
		String result = readFromUrl(url);
		if (result==null) { 
			return -1;
		}
		JSONObject object;
		try {
			object = new JSONObject(result);
			if(object.has(SUCCESS) && object.getBoolean(SUCCESS)) {
				return object.getJSONObject(DATA).getJSONObject(DRIVER).getInt(DRIVERID);
			} else 
				return -1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static String getSchoolInfo (String schoolId) {
		String url = getSchoolInfo + schoolId;
		String result = readFromUrl(url);
		System.out.println(result);
		System.out.println(url);
		return result;
	}
}