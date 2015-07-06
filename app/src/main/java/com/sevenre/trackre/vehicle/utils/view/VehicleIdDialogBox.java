package com.sevenre.trackre.vehicle.utils.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.IconTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.android.iconify.Iconify;
import com.sevenre.trackre.vehicle.R;
import com.sevenre.trackre.vehicle.activity.AlertDialogRadioBus;
import com.sevenre.trackre.vehicle.datatypes.Bus;
import com.sevenre.trackre.vehicle.network.Server;
import com.sevenre.trackre.vehicle.utils.Constants;
import com.sevenre.trackre.vehicle.utils.SharedPreference;

import java.util.ArrayList;


public class VehicleIdDialogBox extends Dialog implements
		android.view.View.OnClickListener {

	Context activity;
	View cancel, login;
	Intent intent;
	Dialog dialog;
	Context mContext;
	FragmentManager fm;

	public VehicleIdDialogBox(Context  activity, Intent intent) {
		super(activity);
		this.activity = activity;
		this.intent = intent;
	}

	public VehicleIdDialogBox(Context  activity, Intent intent, FragmentManager fm) {
		super(activity);
		this.activity = activity;
		this.intent = intent;
		this.fm = fm;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getContext();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_box_vehicle_id);

		setCancelable(true);
		setCanceledOnTouchOutside(false);

		findViewById(R.id.dialog_box_no).setOnClickListener(this);;
		findViewById(R.id.dialog_box_yes).setOnClickListener(this);
		findViewById(R.id.dialog_box_select_bus).setOnClickListener(this);

		TextView tv = (TextView) findViewById(R.id.dialog_box_title);
		tv.setText("Select Vehicle");
		TextView vehcileNo = (TextView)findViewById(R.id.dialog_box_vehicle_id);
		vehcileNo.setText(SharedPreference.getVehicleNo(mContext));

		IconTextView yes = (IconTextView)findViewById(R.id.dialog_box_icon_yes);
		IconTextView no = (IconTextView)findViewById(R.id.dialog_box_icon_no);
		IconTextView bus = (IconTextView)findViewById(R.id.dialog_box_icon_select_bus);

		yes.setText(Iconify.IconValue.fa_check.formattedName());
		no.setText(Iconify.IconValue.fa_times.formattedName());
		bus.setText(Iconify.IconValue.fa_chevron_circle_right.formattedName());

		yes.setTextSize(27);
		no.setTextSize(27);
		bus.setTextSize(27);

		Iconify.addIcons(yes);
		Iconify.addIcons(no);
		Iconify.addIcons(bus);

        if (!(SharedPreference.getVehicleNo(mContext).length()>0)) {
            findViewById(R.id.dialog_box_yes).setVisibility(View.GONE);
        }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.dialog_box_no:
				dismiss();
				break;
			case R.id.dialog_box_yes:
				new GetVehicleId().execute(SharedPreference.getVehicleNo(mContext));
				dismiss();
				if (dialog!=null)
					if (dialog.isShowing())
						dialog.dismiss();
				dialog = GenerateViews.getProgressDialog(activity, "Verify vehicle", "Getting details for this vehicle!");
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				break;
			case R.id.dialog_box_select_bus:
				dismiss();
				if (dialog!=null)
					if (dialog.isShowing())
						dialog.dismiss();
				dialog = GenerateViews.getProgressDialog(activity, "Verify vehicle", "Getting all vehicle list!");
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();

				new GetVehileList().execute(SharedPreference.getSchoolId(mContext));
				break;
		}
	}

	public class GetVehileList extends AsyncTask<String, Integer, ArrayList<Bus>> {

		@Override
		protected ArrayList<Bus> doInBackground(String... strings) {
			return Server.getAllVehicle(SharedPreference.getSchoolId(mContext));
		}

		@Override
		protected void onPostExecute(ArrayList<Bus> buses) {
			super.onPostExecute(buses);
			if (dialog!=null)
				if (dialog.isShowing())
					dialog.dismiss();

			Constants.busList = buses;

			AlertDialogRadioBus alert = new AlertDialogRadioBus();
			Bundle b  = new Bundle();
			b.putInt(Constants.TRIP,intent.getIntExtra(Constants.TRIP,Constants.TRIP_DROP));
			b.putInt("position", 1);
			alert.setArguments(b);
			alert.show(fm,"");
		}
	}

	public class GetVehicleId extends AsyncTask<String, Integer, String> {

		String vehicleNo;
		@Override
		protected String doInBackground(String... params) {
			vehicleNo = params[0];
			return Server.getVehicleId(params[0].replaceAll(" ", ""),SharedPreference.getSchoolId(mContext));
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if (Constants.isLoginSuccess) {
				SharedPreference.setVehicleId(activity.getApplicationContext(), result);
				SharedPreference.setVehicleNo(activity.getApplicationContext(), vehicleNo);
				activity.startActivity(intent);
			} else {
				Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
				show();
			}
			super.onPostExecute(result);
		}
	}
}
