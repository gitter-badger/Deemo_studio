package com.sevenre.trackre.driver.utils.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sevenre.trackre.driver.R;
import com.sevenre.trackre.driver.network.Server;
import com.sevenre.trackre.driver.utils.Constants;
import com.sevenre.trackre.driver.utils.SharedPreference;
import com.sevenre.trackre.driver.utils.Utils;


public class VehicleIdDialogBox extends Dialog implements
		android.view.View.OnClickListener {

	Context activity;
	Button cancel, login;
	EditText vehcileNo;
	Intent intent;
	Dialog dialog;
	
	public VehicleIdDialogBox(Context  activity, Intent intent) {
		super(activity);
		this.activity = activity;
		this.intent = intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_box_vehicle_id);
		cancel = (Button)findViewById(R.id.dialog_box_cancel);
		login = (Button)findViewById(R.id.dialog_box_login);
		setCancelable(true);
		setCanceledOnTouchOutside(false);
		cancel.setOnClickListener(this);
		login.setOnClickListener(this);
		cancel.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.erasBold));
		login.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.erasBold));
		
		TextView tv = (TextView) findViewById(R.id.dialog_box_title);
		tv.setText("Vehicle No");
		vehcileNo = (EditText)findViewById(R.id.dialog_box_vehicle_id);
		vehcileNo.setText(SharedPreference.getVehicleNo(getContext()));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_box_cancel:
			dismiss();
			break;
		case R.id.dialog_box_login:
			if (vehcileNo.getText().toString().length() < 6) {
				Toast.makeText(getContext(), "Enter proper number", Toast.LENGTH_SHORT).show();
				break;
			}
			new GetVehicleId().execute(vehcileNo.getText().toString());
			dismiss();
			dialog = GenerateViews.getProgressDialog(activity, "Verify vehicle", "Getting details for this vehicle!");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			break;
		}
	}
	
	public class GetVehicleId extends AsyncTask<String, Integer, String> {

		String vehicleNo;
		@Override
		protected String doInBackground(String... params) {
			vehicleNo = params[0];
			return Server.getVehicleId(params[0].replaceAll(" ", ""),SharedPreference.getSchoolId(activity.getApplicationContext()));
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
