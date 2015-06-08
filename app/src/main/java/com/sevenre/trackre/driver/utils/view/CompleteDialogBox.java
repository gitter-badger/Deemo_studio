package com.sevenre.trackre.driver.utils.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sevenre.trackre.driver.ActivityHome;
import com.sevenre.trackre.driver.R;
import com.sevenre.trackre.driver.database.LiveDatabaseHandler;
import com.sevenre.trackre.driver.datatypes.Stop;
import com.sevenre.trackre.driver.network.SMSHandler;
import com.sevenre.trackre.driver.network.Server;
import com.sevenre.trackre.driver.network.TrackService;
import com.sevenre.trackre.driver.utils.SharedPreference;
import com.sevenre.trackre.driver.utils.Utils;

public class CompleteDialogBox extends Dialog implements android.view.View.OnClickListener {

	Activity a;
	Dialog d;
	Button yes, no;
	String msg;
	ProgressDialog dialog;
	EditText driverPin;

	public CompleteDialogBox(Activity a, String msg) {
		super(a);
		this.a = a;
		this.msg = msg;
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_box_complete_trip);
		yes = (Button) findViewById(R.id.dialog_box_yes);
		no = (Button) findViewById(R.id.dialog_box_no);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
		yes.setTypeface(Utils.getTypeFace(a.getAssets(), Utils.erasBold));
		no.setTypeface(Utils.getTypeFace(a.getAssets(), Utils.erasBold));
		
		TextView tv  = (TextView) findViewById(R.id.dialog_box_title);
		tv.setText("Complete Trip?");
		
		driverPin = (EditText)findViewById(R.id.dialog_box_driver_pin);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_box_yes:
			
			Intent statusIntent = new Intent(Utils.STATUS_CHANGED_INTENT);
			statusIntent.putExtra(Utils.STATUS, "STOP");
			a.sendBroadcast(statusIntent);
			dismiss();
			
			try {
				if (Integer.parseInt(driverPin.getText().toString())== SharedPreference.getPassCode(a)) {
					new CompleteTrip().execute();
					dialog = GenerateViews.getProgressDialog(a, "Completing trip", "Trying to end trip. Please wait for a while!");
					dialog.show();
				} else {
					Toast.makeText(a, "Incorrect Pin", Toast.LENGTH_LONG).show();
					show();
				}
			} catch (Exception e) {
				Toast.makeText(a, "Incorrect Pin", Toast.LENGTH_LONG).show();
				show();
			}
			
			break;
		case R.id.dialog_box_no:
			dismiss();
			break;
		}
	}
	
	class CompleteTrip extends AsyncTask<String, Integer, Boolean> {

		Stop stop;
		double speed;
		String tripId, status;
		int driverId;
		
		@Override
		protected Boolean doInBackground(String... params) {
			stop = new Stop();
			stop.setDate(Server.getDate());
			stop.setTime(Server.getTime());
			stop.setLat(TrackService.mLat);
			stop.setLng(TrackService.mLng);
			speed = TrackService.mSpeed;
			tripId = SharedPreference.getTripId(a);
			status = SharedPreference.getTripStatus(a);
			driverId = SharedPreference.getDriverId(a);
			return Server.completeTrip(tripId, status, stop.getTime(), stop.getDate(), ""+stop.getLat(), ""+stop.getLng(), ""+speed, "complete",driverId, true);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			//$required_params = array('tripId','tripStatus','date','time','lat','long','speed','isLive');
			
			if (!result) {
				String sms = tripId + "," +
						status + "," + 
						stop.getDate() + "," +
						stop.getTime()+ "," +
						stop.getLat() + "," +
						stop.getLng() + "," + speed;
				SMSHandler.sendSms("+4917636477199", sms);
				
				LiveDatabaseHandler db = new LiveDatabaseHandler(a);
				String[] s = {
						tripId,
						status,
						stop.getTime(),
						stop.getDate(),
						""+ stop.getLat(),
						""+ stop.getLng(),
						""+ speed,
						"complete"};
				db.addTripActivity(s);		
			}
			
			Intent i = new Intent(a.getApplicationContext(),ActivityHome.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			while (Utils.isServiceRunning(a)) {
				
			}
			dialog.dismiss();
			a.finish();
			a.startActivity(i);
			super.onPostExecute(result);
		}
	}
}