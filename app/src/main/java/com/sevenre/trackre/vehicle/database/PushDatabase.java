package com.sevenre.trackre.vehicle.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;

import com.sevenre.trackre.vehicle.datatypes.BusPosition;
import com.sevenre.trackre.vehicle.datatypes.TaggingStop;
import com.sevenre.trackre.vehicle.network.Server;
import com.sevenre.trackre.vehicle.utils.Log;
import com.sevenre.trackre.vehicle.utils.NetworkConnectivity;

public class PushDatabase extends AsyncTask<Context, Integer, Void>{

	LiveDatabaseHandler db;
	Context context;
	
	@Override
	protected Void doInBackground(Context... params) {
		Log.e("PushDatabase + start pushing data");
		context = params[0];
		db = new LiveDatabaseHandler(context);
		if (Looper.myLooper()==null) {
			Looper.prepare();
		}
		tripActivity();
		tagStop();
		tagPosition();
		return null;
	}

	private void tagStop() {
		List<TaggingStop> stop = db.getAllTagStop();
		if (stop.size()==0) return;
		List<String> keys = new ArrayList<String>();
		
		if (NetworkConnectivity.isConnectedToInternet(context)) {
			for (TaggingStop o : stop) {
				if (Server.tagStop(o, false)){
					keys.add(o.getDbId());
					Log.e("PushDatabase tag stop : \n" +o.toString());
				} else if (NetworkConnectivity.isConnectedToInternet(context)){
					break;
				}
			}
		}
		
		if (keys.size() == stop.size())
			db.removeAllTagStop();
		else
			db.removeTagStop(keys);
		Log.e("TAG STOP PUSHED TO SERVER, Key size : " + keys.size() 
				+ " Actual size : " + stop.size());
	}

	private void tripActivity() {
		List<String[]> act = db.getAllTripActivity();
		if (act.size()==0) return;
		List<String> keys = new ArrayList<String>();
		
		if (NetworkConnectivity.isConnectedToInternet(context)) {
			for (String[] o :act) {
				//ToDo Change Database for compelte trip
//				if (Server.completeTrip(o[1], o[2], o[4], o[3], o[5], o[6], o[7], o[8], 0,false)) { //set driverid in db
//					keys.add(o[0]);
//				} else if (NetworkConnectivity.isConnectedToInternet(context)){
//					break;
//				}
			}
		}
		
		if (keys.size() == act.size())
			db.removeAllTripActivity();
		else
			db.removeTripActivity(keys);
		Log.e("TRIP ACTIVITY PUSHED TO SERVER, Key size : " + keys.size() 
				+ " Actual size : " + act.size());
	}

	private void tagPosition() {
		List<BusPosition> positions = db.getAllTagPosition();
		if(positions.size()==0) return;
		
		List<String> keys = new ArrayList<String>();
		
		// push data to server and record keys that have been pushed
		if (NetworkConnectivity.isConnectedToInternet(context)) {
			for (BusPosition o : positions) {
				if (Server.tagPosition(o,false,0)) {
					keys.add(o.getId());
				} else if (NetworkConnectivity.isConnectedToInternet(context)){
					break;
				}
			}
		}
		
		// remove rows from database with specific keys
		if (keys.size() == positions.size())
			db.removeAllTagPosition();
		else
			db.removeTagPosition(keys);
		
		Log.e("TAG POSITION PUSHED TO SERVER, Key size : " + keys.size() 
				+ " Actual size : " + positions.size());
	}

}
