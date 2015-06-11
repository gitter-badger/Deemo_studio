package com.sevenre.trackre.driver.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.sevenre.trackre.driver.R;
import com.sevenre.trackre.driver.datatypes.Trip;
import com.sevenre.trackre.driver.listadapter.TripAdapter;
import com.sevenre.trackre.driver.network.Server;
import com.sevenre.trackre.driver.network.TrackService;
import com.sevenre.trackre.driver.utils.Constants;
import com.sevenre.trackre.driver.utils.NetworkConnectivity;
import com.sevenre.trackre.driver.utils.SharedPreference;
import com.sevenre.trackre.driver.utils.Utils;
import com.sevenre.trackre.driver.utils.view.GenerateViews;
import com.sevenre.trackre.driver.utils.view.StartTripDialogBox;

public class ActivityDropList extends ListActivity {

	ListView lv;
	ArrayList<Trip> result;
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar bar = getActionBar();

		if (bar != null) {
			bar.setBackgroundDrawable(Constants.ACTION_BAR_COLOR_DRAWABLE);
			bar.setTitle("Drop");
			bar.setDisplayHomeAsUpEnabled(true);
		}
		getListView().setBackgroundColor(Color.parseColor("#f5f5f5"));


		dialog = new ProgressDialog(ActivityDropList.this);
		dialog.setTitle("Getting trips");
		dialog.setMessage("Trip list is being fetched, please wait");
		dialog.setButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		new GetDropTrip().execute();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (!NetworkConnectivity.isConnectedToInternet(getApplicationContext())) {
			Toast.makeText(getApplicationContext(), R.string.no_internet, 4500).show();
			super.onListItemClick(l, v, position, id);
			return;
		}
		
		if (dialog!=null) 
			if (dialog.isShowing())
				dialog.cancel();
		
		final Intent s = new Intent(ActivityDropList.this, TrackService.class);
		Trip t =  (Trip)result.get(position);
		StartTripDialogBox d;
		
		
		if (t.getTripStatus().contains("track")) {
			final Intent i = new Intent(ActivityDropList.this, MainActivity.class);
			d = new StartTripDialogBox(ActivityDropList.this,  i, s ,t);
		} else {
			final Intent i = new Intent(ActivityDropList.this, ActivityTagging.class);
			d = new StartTripDialogBox(ActivityDropList.this,  i, s ,t);
		}
		
		d.show();
		
		super.onListItemClick(l, v, position, id);
	}

	public void updateListView(ArrayList<Trip> results) {
		setListAdapter(new TripAdapter(this, results, R.layout.list_item_trip_info));
		ListView lv = (ListView) findViewById(android.R.id.list);
		lv.setDivider(this.getResources().getDrawable(R.drawable.transperent_color));
		lv.setDividerHeight(20);
		lv.setPadding(20, 20, 20, 20);
		lv.setBackgroundColor(Color.WHITE);
		View emptyView = GenerateViews.getEmptyView(getApplicationContext(), "No Trips there for this bus!");
		((ViewGroup) getListView().getParent()).addView(emptyView);
		getListView().setEmptyView(emptyView);
		dialog.dismiss();
	}

	class GetDropTrip extends AsyncTask<String, Integer, ArrayList<Trip>> {

		@Override
		protected ArrayList<Trip> doInBackground(String... arg0) {
			return Server.getDropTrip(SharedPreference.getVehicleId(getApplicationContext()),SharedPreference.getSchoolId(getApplicationContext()));
		}

		@Override
		protected void onPostExecute(ArrayList<Trip> result) {
			ActivityDropList.this.result = result;
			updateListView(result);
			
			super.onPostExecute(result);
		}
	}
	
	@Override
	protected void onResume() {
		if (!NetworkConnectivity.isGPSConnected(getApplicationContext())){
			AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDropList.this);
			builder.setTitle("No GPS not found");
			builder.setMessage("Please start your GPS");
			builder.setCancelable(false);
			builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(Utils.intentForGPS());
				}
			});
			if (dialog!=null) 
				if (dialog.isShowing())
					dialog.cancel();
			builder.create().show();
		}
		super.onResume();
	}
}
