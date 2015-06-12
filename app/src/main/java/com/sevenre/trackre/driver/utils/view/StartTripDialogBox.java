package com.sevenre.trackre.driver.utils.view;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sevenre.trackre.driver.R;
import com.sevenre.trackre.driver.database.LiveDatabaseHandler;
import com.sevenre.trackre.driver.datatypes.Stop;
import com.sevenre.trackre.driver.datatypes.Trip;
import com.sevenre.trackre.driver.network.Server;
import com.sevenre.trackre.driver.utils.Constants;
import com.sevenre.trackre.driver.utils.NetworkConnectivity;
import com.sevenre.trackre.driver.utils.SharedPreference;
import com.sevenre.trackre.driver.utils.Utils;
import com.sevenre.trackre.driver.utils.view.pin.PinNumberDeleteKeyListener;
import com.sevenre.trackre.driver.utils.view.pin.PinNumberTextWatcher;

public class StartTripDialogBox extends Dialog implements android.view.View.OnClickListener, LocationListener{

	Context mContext;
	Dialog d;
	Button yes, no;
	Intent i,s;
	String tripId;
	Trip t;
	ProgressDialog dialog;
	Location mLocation;
	double mLat, mLng, mSpeed;
	EditText driverPin;
	
	public StartTripDialogBox(Context a,  Intent i,  Intent s, Trip t) {
		super(a);
		this.mContext = a;
		this.i = i;
		this.tripId = t.getTripId();
		this.t = t;
		this.s = s;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeLocationParameters();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.dialog_box_start_trip);
	    yes = (Button) findViewById(R.id.dialog_box_yes);
	    no = (Button) findViewById(R.id.dialog_box_no);
	    yes.setOnClickListener(this);
	    no.setOnClickListener(this);
	    driverPin = (EditText)findViewById(R.id.dialog_box_driver_pin);
		TextView tv = (TextView) findViewById(R.id.dialog_box_select_trip_text);
		tv.setText(t.getName());
	    tv = (TextView) findViewById(R.id.dialog_box_title);
		tv.setText(""+t.getTime());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.dialog_box_yes:
			dismiss();
			if(dialog!=null)
				if(dialog.isShowing())
					dialog.cancel();
			if (!NetworkConnectivity.isGPSConnected(mContext)){
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("No GPS not found");
				builder.setMessage("Please start your GPS");
				builder.setCancelable(false);
				builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mContext.startActivity(Utils.intentForGPS());
					}
				});
				builder.create().show();
			} else {
				dialog = GenerateViews.getProgressDialog(mContext, "Starting Trip", "Please wait while trip is being started");
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				new StartTrip().execute();
			}
			break;
		case R.id.dialog_box_no:
			dismiss();
			break;
		}
	}
	
	public class StartTrip extends AsyncTask<String, Integer, Boolean> {

		ArrayList<Stop> stops;
		int driverId;
		String passcode;
		@Override
		protected Boolean doInBackground(String... params) {
			passcode = driverPin.getText().toString();
			driverId = Server.verifyDriverPassCode(
					passcode, SharedPreference.getSchoolId(mContext));
			if (driverId<0) {
				return false;
			}
			boolean start = Server.startTrip(t, mLat, mLng, mSpeed, driverId); 
			if (start) 
				stops = Server.getStopList(t.getTripId(),SharedPreference.getSchoolId(mContext));
			return start;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				if (stops.size()>0) {
					LiveDatabaseHandler db = new LiveDatabaseHandler(mContext.getApplicationContext());
					db.updateTimeTable(stops);
				} 
				SharedPreference.setTripId(mContext, tripId);
				SharedPreference.setTripStatus(mContext, t.getTripStatus());
				SharedPreference.setPassCode(mContext, Integer.parseInt(passcode));
				SharedPreference.setDriverId(mContext, driverId);
				mContext.startActivity(i);
				mContext.startService(s);
				
				if(dialog!=null)
					if(dialog.isShowing())
						dialog.cancel();
			} else {
				if (driverId<0) {
					Toast.makeText(mContext, "Unable to start trip", Toast.LENGTH_LONG).show();
					show();
				} else {
					Toast.makeText(mContext, "Incorrect Pin", Toast.LENGTH_LONG).show();
				}
				if(dialog!=null)
					if(dialog.isShowing())
						dialog.cancel();
			}
			
			super.onPostExecute(result);
		}
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