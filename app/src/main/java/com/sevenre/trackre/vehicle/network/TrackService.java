package com.sevenre.trackre.vehicle.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.support.v4.content.LocalBroadcastManager;
import android.view.WindowManager;
import android.widget.Toast;

import com.sevenre.trackre.vehicle.database.LiveDatabaseHandler;
import com.sevenre.trackre.vehicle.datatypes.BusPosition;
import com.sevenre.trackre.vehicle.datatypes.Stop;
import com.sevenre.trackre.vehicle.utils.Log;
import com.sevenre.trackre.vehicle.utils.NetworkConnectivity;
import com.sevenre.trackre.vehicle.utils.SharedPreference;
import com.sevenre.trackre.vehicle.utils.Utils;

public class TrackService extends Service implements LocationListener {
	
	public static double mLat, mLng, mSpeed, mTime, mDistance = 0;
	public static boolean serviceStatus = true/*, isTagging = false*/;
	
	LocationManager locationManager;
	Location mLocation;
	Context mContext;
	String mBestProvider, tripId, tripMode = "track";
	final List<BusPosition> safeList = Collections.synchronizedList(new ArrayList<BusPosition>());
	Timer addTimer, uploadTimer;
	LiveDatabaseHandler db ;
	boolean killService = false; //if service is to be stopped and push data into database
	int stopReached = 0, TAG_TIME = 5000;
	ArrayList<Stop> stopList;
	
	public TrackService() {}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		initializeLocationParameters();
		addTimer = new Timer();
		uploadTimer = new Timer();
		BusPosition.setDeviceId(Secure.getString(getContentResolver(),Secure.ANDROID_ID));
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initializeLocationParameters();
		tripId = SharedPreference.getTripId(getApplicationContext());
		//tripStatus = SharedPreference.getTripStatus(getApplicationContext());
		db = new LiveDatabaseHandler(this);
		addTimer.scheduleAtFixedRate(addTask, 1000, TAG_TIME);
		uploadTimer.scheduleAtFixedRate(uploadTask, 1000, 60202);
		stopReached = 0;
		IntentFilter filter = new IntentFilter();
		filter.addAction(Utils.STATUS_CHANGED_INTENT);
		registerReceiver(mStatusReceiver, filter);
		
		stopList = (new LiveDatabaseHandler(getApplicationContext())).getTimeTable();

		return super.onStartCommand(intent, flags, startId);
	}

	BroadcastReceiver mStatusReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			final String status = intent.getStringExtra(Utils.STATUS);
			if (status.contains("STOP")||status.contains("CANCEL")) {
				unregisterReceiver(mStatusReceiver);
				//isTagging = false;
				killService =true;
				addTimer.cancel();
				uploadTimer.cancel();
				db.addTagPosition(safeList);
				for (int i = 0; i < safeList.size(); i++)
					safeList.remove(0);
				stopSelf();
				locationManager.removeUpdates(TrackService.this);

				onDestroy();
			} else if (status.contains("PAUSE")) {
				serviceStatus = false;
			} else if (status.contains("START")) {
				serviceStatus = true;
			}

			TimerTask addStatusTask = new TimerTask() {

				@Override
				public void run() {
					//Server.addCurrentStatus(Server.getDate(), status);
				}
			};
			addStatusTask.run();
		}
	};

	@Override
	public void onLocationChanged(Location location) {
		mLat = location.getLatitude();
		mLng = location.getLongitude();
		mSpeed = location.getSpeed();

////		try {
////			((Activity) getApplicationContext()).runOnUiThread(new Runnable() {
////				@Override
////				public void run() {
////					TextView tv = (TextView) ((AppCompatActivity) getApplicationContext()).findViewById(R.id.active_trip_next_stop_name);
////					tv.setText("" + mLat + ", " + mLng);
////				}
////			});
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//
//        Class<?> activityClass = null;
//        final Activity contextActivity;
//        try {
//            activityClass = Class.forName("com.sevenre.trackre.vehicle.activity.ActivityTracking");
//            contextActivity = (Activity) activityClass.newInstance();
//
//            //Update UI on FirstActivity not working
//            contextActivity.runOnUiThread(new Runnable() {
//                public void run()
//                {
//                    TextView tv = (TextView ) contextActivity.findViewById(R.id.tracking_next_stop_name);
//                    tv.setText("" + mLat + ", " + mLng);
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



		if (stopReached < stopList.size()-1) {
			Stop s = stopList.get(stopReached + 1);
			Location stop = new Location("GPS");
			stop.setLatitude(s.getLat());
			stop.setLongitude(s.getLng());
			float distance = location.distanceTo(stop);
			if (distance < 250) {
				stopReached++;
                LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
                Intent intent = new Intent(Utils.NEXT_STOP_INTENT);
                intent.putExtra(Utils.NEXT_STOP, "" + mLat + ", " + mLng);
                broadcaster.sendBroadcast(intent);
				Toast.makeText(getApplicationContext(), "About to reach : " + s.getName(), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	@Override
	public void onProviderEnabled(String provider) {Log.e("gps enabled");}

	@Override
	public void onProviderDisabled(String provider) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
		builder.setTitle("No GPS not found");
		builder.setMessage("Please start your GPS");
		builder.setCancelable(false);
		builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getApplication().startActivity(Utils.intentForGPS());
			}
		});
		
		 AlertDialog dialog = builder.create();
		 dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		 dialog.show();
		 Log.e("gps disabled");
	}

	public void initializeLocationParameters() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (!isGPSEnabled && !isNetworkEnabled) {

		} else {
			if (locationManager != null) {
				if (isGPSEnabled) {
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
					mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				} else if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
					mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
			}
			if (mLocation == null) {
				if (NetworkConnectivity.isConnectedToInternet(mContext)) {
					Criteria c = new Criteria();
					String mBestProvider = locationManager.getBestProvider(c,true);
					locationManager.requestLocationUpdates(mBestProvider, 0, 0,this);
					mLocation = locationManager.getLastKnownLocation(mBestProvider);
				}
			}
			if (mLocation != null) {
				mLat = mLocation.getLatitude();
				mLng = mLocation.getLongitude();
				mSpeed = mLocation.getSpeed();
			}
		}
	}

	public void addPosition() throws Exception {
		BusPosition pos = new BusPosition(
				Double.toString(mLat),
				Double.toString(mLng), 
				Double.toString(mSpeed),
				Server.getTime(),
				Server.getDate(),
				tripId, tripMode);
		if (!Server.tagPosition(pos,true, stopReached)) {
			synchronized(safeList) {
				safeList.add(pos);
			}
		}
	}

	public void uploadData() throws Exception{
		while (!safeList.isEmpty()) {
			BusPosition pos = safeList.get(0);
			if (Server.tagPosition(pos, false, stopReached)) {
				synchronized(safeList) {
					safeList.remove(0);	
				}
			} 
		}
	}

	TimerTask uploadTask = new TimerTask() {
		@Override
		public void run() {			
			try {
				if (serviceStatus)
					uploadData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	TimerTask addTask = new TimerTask() {
		@Override
		public void run() {
			try {
				if (serviceStatus) 
					addPosition();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}