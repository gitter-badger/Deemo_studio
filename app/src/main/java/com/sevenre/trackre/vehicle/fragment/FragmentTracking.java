package com.sevenre.trackre.vehicle.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sevenre.trackre.vehicle.R;
import com.sevenre.trackre.vehicle.database.LiveDatabaseHandler;
import com.sevenre.trackre.vehicle.datatypes.Stop;
import com.sevenre.trackre.vehicle.listadapter.NextStopAdapter;
import com.sevenre.trackre.vehicle.network.Server;
import com.sevenre.trackre.vehicle.utils.SharedPreference;
import com.sevenre.trackre.vehicle.utils.view.CancelTripDialogBox;
import com.sevenre.trackre.vehicle.utils.view.CompleteDialogBox;

public class FragmentTracking extends Fragment implements OnClickListener {

	View rootView;
	//TextView route_no, route_name, route_time;
	View cancel, completed;
	ListView nextStop;	
	ArrayList<Stop> result;
	Timer updateNextStop;
	
	public static FragmentTracking newInstance() {
		return new FragmentTracking();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_tracking, container, false);
		new SetUpUI().execute();
		return rootView;
	}

	class SetUpUI extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			setUpUI();
			return null;
		}
		
	}
	private void setUpUI() {
		//route_no = (TextView) rootView.findViewById(R.id.active_trip_active_route_no);
		//route_name = (TextView) rootView.findViewById(R.id.active_trip_active_route_name);
		//route_time = (TextView) rootView.findViewById(R.id.active_trip_active_route_time);

		cancel = rootView.findViewById(R.id.active_trip_cancel);
		completed = rootView.findViewById(R.id.active_trip_completed);

		cancel.setOnClickListener(FragmentTracking.this);
		completed.setOnClickListener(FragmentTracking.this);

		nextStop = (ListView) rootView.findViewById(R.id.active_trip_next_stop_list_view);
		nextStop.setDividerHeight(0);
		
		updateNextStop = new Timer();
		updateNextStop.schedule(updateNextStopTask, 10000, 10000);

		result = (new LiveDatabaseHandler(getActivity())).getTimeTable();
		updateListView(result);
	}

	TimerTask updateNextStopTask = new TimerTask() {
		@Override
		public void run() {
			Date current = new Date();
			current.setTime(System.currentTimeMillis());
			Stop nextStop = new Stop();
			for (Stop stop : result){
				Date next = stop.get_time();
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// type = true ~ completed
		// type = false ~ cancel
		case R.id.active_trip_cancel:
			CancelTripDialogBox d = new CancelTripDialogBox(getActivity(), "Are you sure you want to Cancel trip?");
			d.show();
			break;
		case R.id.active_trip_completed:
			CompleteDialogBox d1 = new CompleteDialogBox(getActivity(), "Are you sure you want to Complete trip?");
			d1.show();
			break;
		}
	}

	void showCustomDialog(final boolean type) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(type ? "Complete Trip" : "Cancel Trip");
		builder.setMessage("Are you sure to "+ (type ? "omplete trip" : "cancel trip") + "?");
		builder.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getActivity().finish();
			}
		});
		builder.create().show();
	}

	private void updateListView(ArrayList<Stop> result) {
		nextStop.setAdapter(new NextStopAdapter(getActivity(), result));
	}

	class GetNextStop extends AsyncTask<String, Integer, ArrayList<Stop>> {

		@Override
		protected ArrayList<Stop> doInBackground(String... arg0) {
			return Server.getStopList(SharedPreference.getTripId(getActivity()), SharedPreference.getSchoolId(getActivity()));
		}

		@Override
		protected void onPostExecute(ArrayList<Stop> result) {
			FragmentTracking.this.result = result;
			updateListView(result);
			super.onPostExecute(result);
		}
	}
}


