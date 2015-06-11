package com.sevenre.trackre.driver.activity;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sevenre.trackre.driver.R;
import com.sevenre.trackre.driver.utils.Constants;
import com.sevenre.trackre.driver.utils.NetworkConnectivity;


public class ActivitySimplyTrack extends AppCompatActivity implements LocationListener {

	GoogleMap googleMap;
	Context mContext;
	LocationManager locationManager;
	Location mLocation;
	double mLat, mLng;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simply_track);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar ab = getSupportActionBar();
		if (ab != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		mContext = getApplicationContext();
		googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.simply_track_map)).getMap();
		initializeLocationParameters();
		
		
	}

	public void initializeLocationParameters() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (!isGPSEnabled && !isNetworkEnabled) {

		} else {
			if (locationManager != null) {
				if (isGPSEnabled) {
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 20, this);
					mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				} else if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 20, this);
					mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
			}
			if (mLocation == null) {
				if (NetworkConnectivity.isConnectedToInternet(mContext)) {
					Criteria c = new Criteria();
					String mBestProvider = locationManager.getBestProvider(c,true);
					locationManager.requestLocationUpdates(mBestProvider, 2000, 20,this);
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

	@Override
	public void onLocationChanged(Location location) {
		LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
		googleMap.addMarker(new MarkerOptions().position(latlng));
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,googleMap.getCameraPosition().zoom));
	}
	
	@Override
	protected void onResume() {
		initializeLocationParameters();
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		if (locationManager!=null)
			locationManager.removeUpdates(this);
		super.onStop();
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
}
