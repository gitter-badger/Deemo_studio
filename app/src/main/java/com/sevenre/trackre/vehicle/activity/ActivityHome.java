package com.sevenre.trackre.vehicle.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.sevenre.trackre.vehicle.R;
import com.sevenre.trackre.vehicle.database.PushDatabase;
import com.sevenre.trackre.vehicle.utils.Constants;
import com.sevenre.trackre.vehicle.utils.NetworkConnectivity;
import com.sevenre.trackre.vehicle.utils.SharedPreference;
import com.sevenre.trackre.vehicle.utils.Utils;
import com.sevenre.trackre.vehicle.utils.view.VehicleIdDialogBox;

public class ActivityHome extends Activity implements OnClickListener {

	View pick, drop, setting, simply_track, other_info;
	AlertDialog.Builder builder;
	Dialog dialog;
	Context mContext;
	String schoolId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		mContext = getApplicationContext();
		setUpUI();
	}

	private void setUpUI() {
		pick = findViewById(R.id.home_pick_up);
		drop = findViewById(R.id.home_drop);
		
		simply_track = findViewById(R.id.home_just_track);
		other_info = findViewById(R.id.home_other_info);

		pick.setOnClickListener(ActivityHome.this);
		drop.setOnClickListener(ActivityHome.this);
		
		other_info.setOnClickListener(ActivityHome.this);
		simply_track.setOnClickListener(ActivityHome.this);

		TextView tv = (TextView)findViewById(R.id.school_info_home_screen);
		tv.setText(SharedPreference.getSchoolName(mContext) + ", " + SharedPreference.getSchoolCity(mContext));
		tv.setMovementMethod(new ScrollingMovementMethod());
		tv.setTypeface(Utils.getTypeFace(getAssets(), Utils.roboto));
	}

	
	
	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.home_drop:
			if(Utils.isServiceRunning(getApplicationContext())) {
				i.setClass(ActivityHome.this, MainActivity.class);
				startActivity(i);
			} else if (Utils.isTaggingServiceRunning(getApplicationContext())) {
				i.setClass(ActivityHome.this, ActivityTagging.class);
				startActivity(i);
			}else {
				if (NetworkConnectivity.isConnectedToInternet(getApplicationContext())) {
					i.setClass(ActivityHome.this, ActivitySelectTrip.class);
					i.putExtra(Constants.TRIP, Constants.TRIP_DROP);
					if (dialog!=null)
						if (dialog.isShowing())
							dialog.dismiss();
					dialog = new VehicleIdDialogBox(ActivityHome.this,i);
					dialog.show();
				}
				else {
					Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
					break;
				}
			}
			break;
		case R.id.home_pick_up:
			if(Utils.isServiceRunning(getApplicationContext())) {
				i.setClass(ActivityHome.this, MainActivity.class);
				startActivity(i);
			} else if (Utils.isTaggingServiceRunning(getApplicationContext())) {
				i.setClass(ActivityHome.this, ActivityTagging.class);
				startActivity(i);
			} else {
				if (NetworkConnectivity.isConnectedToInternet(getApplicationContext())) {
					i.setClass(ActivityHome.this, ActivitySelectTrip.class);
					i.putExtra(Constants.TRIP, Constants.TRIP_PICK_UP);
					if (dialog!=null)
						if (dialog.isShowing())
							dialog.dismiss();
					dialog = new VehicleIdDialogBox(ActivityHome.this,i);
					dialog.show();
					
				}
				else {
					Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
					break;
				}
			}
			break;
		case R.id.home_just_track:
			i.setClass(ActivityHome.this, ActivitySimplyTrack.class);
			startActivity(i);
			break;
		case R.id.home_other_info:
			i.setClass(ActivityHome.this, ActivityOtherInfo.class);
			startActivity(i);
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		schoolId = SharedPreference.getSchoolId(mContext);
		if (schoolId.equals(Utils.ERROR)) {
			Intent i = new Intent(ActivityHome.this,ActivityAuthenticate.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			Constants.isAuthentic = false;
			startActivity(i);
		}
		if (!NetworkConnectivity.isConnectedToInternet(getApplicationContext())){
			new PushDatabase().execute(getApplicationContext());
			
			builder = new AlertDialog.Builder(ActivityHome.this);
			builder.setTitle("Network connectivity not found");
			builder.setMessage("Please start your Data Plan");
			builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(Utils.intentForNetworkAccess());
				}
			});
			builder.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			if (dialog!= null)
				if (dialog.isShowing())
					dialog.dismiss();
			dialog = builder.create();
		}
	}
}
