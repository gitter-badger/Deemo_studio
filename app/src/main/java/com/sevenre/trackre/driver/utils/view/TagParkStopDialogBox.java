package com.sevenre.trackre.driver.utils.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sevenre.trackre.driver.R;
import com.sevenre.trackre.driver.datatypes.ParkStop;
import com.sevenre.trackre.driver.network.Server;
import com.sevenre.trackre.driver.utils.Constants;
import com.sevenre.trackre.driver.utils.NetworkConnectivity;
import com.sevenre.trackre.driver.utils.SharedPreference;
import com.sevenre.trackre.driver.utils.Utils;

public class TagParkStopDialogBox extends Dialog implements OnClickListener, LocationListener {

	Button yes, no;
	Activity activity;
	ParkStop stop;
	
	LocationManager locationManager;
	Location mLocation;
	double mLat, mLng;

	GoogleMap googleMap;
	
	public TagParkStopDialogBox(Activity activity, ParkStop stop) {
		super(activity);
		this.stop = stop;
		this.activity = activity;
	}
	
	@Override
	public void onDetachedFromWindow() {
		locationManager.removeUpdates(this);
		MapFragment f = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.dialog_tag_park_map);
		if (f != null)
			activity.getFragmentManager().beginTransaction().remove(f).commit();
		super.onDetachedFromWindow();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_box_tag_park_stop);

		yes = (Button) findViewById(R.id.dialog_box_yes);
		no = (Button) findViewById(R.id.dialog_box_no);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);

		if (Constants.MODE == Constants.USER_MODE) {
			yes.setVisibility(View.GONE);
			no.setVisibility(View.GONE);
			findViewById(R.id.dialog_box_select_trip_text).setVisibility(View.GONE);
		}

		googleMap = ((MapFragment) activity.getFragmentManager().findFragmentById(R.id.dialog_tag_park_map)).getMap();
		TextView tv = (TextView) findViewById(R.id.dialog_box_select_trip_text);
		tv.setText("Update park station position to current?");
		tv = (TextView) findViewById(R.id.dialog_box_title);
		tv.setText(stop.getName());

        initializeLocationParameters();
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.dialog_box_yes:
			stop.setLat(mLat);
			stop.setLng(mLng);
			new TagParkStation().execute(stop);
		case R.id.dialog_box_no:
			dismiss();
		}
	}
	
	@Override
	public void onLocationChanged(Location location) {
		googleMap.clear();
		mLat = location.getLatitude();
		mLng = location.getLongitude();
		googleMap.addMarker(new MarkerOptions().position(new LatLng(mLat, mLng)));
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLat, mLng), 
				googleMap.getCameraPosition().zoom));
	}
	
	public void initializeLocationParameters() {
		locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
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
				if (NetworkConnectivity.isConnectedToInternet(activity)) {
					Criteria c = new Criteria();
					String mBestProvider = locationManager.getBestProvider(c,true);
					locationManager.requestLocationUpdates(mBestProvider, 0, 0,this);
					mLocation = locationManager.getLastKnownLocation(mBestProvider);
				}
			}
			if (mLocation != null) {
				mLat = mLocation.getLatitude();
				mLng = mLocation.getLongitude();
				LatLng latlng = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
				googleMap.addMarker(new MarkerOptions().position(latlng));
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
			}
		}
	}

	@Override public void onProviderDisabled(String provider) {Toast.makeText(activity, R.string.no_gps, Toast.LENGTH_LONG).show();}

	@Override public void onProviderEnabled(String provider) {}

	@Override public void onStatusChanged(String provider, int status, Bundle extras) { }
	
	class TagParkStation extends AsyncTask<ParkStop, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(ParkStop... params) {
			
			return Server.tagParkStation(params[0], SharedPreference.getSchoolId(activity));
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			
			super.onPostExecute(result);
		}
	}
}
