package com.sevenre.trackre.driver.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.sevenre.trackre.driver.R;

public class NetworkConnectivity {
	
	public static boolean isConnectedToInternet(Context mContext) 
	{
		ConnectivityManager connectivity = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) 
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) 
						return true;
		}
		Toast.makeText(mContext, R.string.no_internet, Toast.LENGTH_SHORT).show();
		PlaySound.play(mContext, Utils.no_internet);
		return true; //raj
	}
	
	public static boolean isGPSConnected (Context context) {
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE );
		boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!statusOfGPS) {
			Toast.makeText(context, R.string.no_gps, Toast.LENGTH_SHORT).show();
			PlaySound.play(context, Utils.no_gps);
		}
		return statusOfGPS;
	}
}
