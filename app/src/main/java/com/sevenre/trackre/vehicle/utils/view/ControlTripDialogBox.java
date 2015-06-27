package com.sevenre.trackre.vehicle.utils.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.sevenre.trackre.vehicle.R;
import com.sevenre.trackre.vehicle.datatypes.Trip;
import com.sevenre.trackre.vehicle.utils.NetworkConnectivity;

public class ControlTripDialogBox extends Dialog implements android.view.View.OnClickListener, LocationListener{

	Location mLocation;
	double mLat, mLng, mSpeed;
	Context mContext;
	Intent startIntent, service;
	Trip trip;
	boolean control; // start = 0, end = 1
	
	public ControlTripDialogBox(Context context, Intent startIntent, Intent service, Trip t, boolean control) {
		super(context);
		this.mContext = context;
		this.startIntent = startIntent;
		this.service = service;
		this.trip = t;
		this.control = control;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeLocationParameters();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.dialog_box_control_trip);
	    
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	public void initializeLocationParameters() {
		LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (!isGPSEnabled && !isNetworkEnabled) {

		}  else {
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
				locationManager.removeUpdates(this);
			}
			
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		mLat = location.getLatitude();
		mLng = location.getLongitude();
		mSpeed = location.getSpeed();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onProviderDisabled(String provider) {}
	
}
