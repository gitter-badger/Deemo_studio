package com.sevenre.trackre.driver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sevenre.trackre.driver.database.PushDatabase;
import com.sevenre.trackre.driver.network.Server;
import com.sevenre.trackre.driver.utils.Constants;
import com.sevenre.trackre.driver.utils.NetworkConnectivity;
import com.sevenre.trackre.driver.utils.SharedPreference;
import com.sevenre.trackre.driver.utils.Utils;
import com.sevenre.trackre.driver.utils.view.GenerateViews;

public class ActivityLogin extends Activity implements OnClickListener {

	EditText vehicleNo;
	Button enter;
	ProgressDialog dialog;
	String schoolId;
	Context mContext;
	Toast toast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		schoolId = SharedPreference.getSchoolId(mContext);
		if (schoolId.equals(Utils.ERROR)) {
			Intent i = new Intent(ActivityLogin.this,ActivityAuthenticate.class);
			Constants.isAuthentic = false;
			startActivity(i);
		}
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		setUpUI();
	}

	private void setUpUI() {
		vehicleNo = (EditText) findViewById(R.id.login_vehicle_no);
		enter = (Button) findViewById(R.id.login_enter);
		vehicleNo.setGravity(Gravity.CENTER);
		vehicleNo.setHint("Vehicle No");
		vehicleNo.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
		enter.setOnClickListener(ActivityLogin.this);
		vehicleNo.setTypeface(Utils.getTypeFace(getAssets(),Utils.roboto));
		enter.setTypeface(Utils.getTypeFace(getAssets(),Utils.roboto));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_enter:
			if (!NetworkConnectivity.isConnectedToInternet(mContext)){
				if (toast!=null)
					toast.cancel();
				toast = Toast.makeText(mContext, R.string.no_internet, Toast.LENGTH_LONG);
				toast.show();
				break;
			}
			String vehicle = vehicleNo.getText().toString();
			if (vehicle.length() < Constants.VEHICLE_NO_LENGTH){
				if (toast!=null)
					toast.cancel();
				toast = Toast.makeText(mContext, "Vehicle No is not correct", Toast.LENGTH_LONG);
				toast.show();
			} else {
				if (dialog!= null)
					if (dialog.isShowing())
						dialog.dismiss();
				dialog = GenerateViews.getProgressDialog(this, "Verifying bus number",
						"We are trying to verify your details, please wait for a while");
				dialog.show();
				SharedPreference.setVehicleNo(mContext, vehicle);
				new GetVehicleId().execute(vehicle);
			}
		}
	}
	
	public class GetVehicleId extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			return Server.getVehicleId(params[0],SharedPreference.getSchoolId(mContext));
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if (Constants.isLoginSuccess) {
				SharedPreference.setVehicleId(getApplicationContext(), result);
				Intent i = new Intent(ActivityLogin.this, ActivityHome.class);
				startActivity(i); 
			} else {
				if (toast!=null)
					toast.cancel();
				toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_error), Toast.LENGTH_LONG);
				toast.show();
			}
			
			super.onPostExecute(result);
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (Utils.isServiceRunning(mContext)) {
			startActivity(new Intent (ActivityLogin.this, MainActivity.class));
		} else if (Utils.isTaggingServiceRunning(getApplicationContext())) {
			startActivity(new Intent(ActivityLogin.this, ActivityTagging.class));
		}
		
		if (!NetworkConnectivity.isConnectedToInternet(mContext)){
			if (dialog!= null)
				if (dialog.isShowing())
					dialog.dismiss();
			
			AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLogin.this);
			builder.setTitle("No network not found");
			builder.setMessage("Please start your data Plan");
			builder.setCancelable(false);
			builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					startActivity(Utils.intentForNetworkAccess());
				}
			});
			builder.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		} else {
			new PushDatabase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mContext);
		}
		
		if (!NetworkConnectivity.isGPSConnected(mContext)){
			if (dialog!= null)
				if (dialog.isShowing())
					dialog.dismiss();
			AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLogin.this);
			builder.setTitle("No GPS not found");
			builder.setMessage("Please start your GPS");
			builder.setCancelable(false);
			builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivityForResult(Utils.intentForGPS(),1);
				}
			});
			builder.create().show();
		}
	}
	
	@Override
	protected void onStop() {
		if (toast!=null)
			toast.cancel();
		super.onStop();
	}
}
