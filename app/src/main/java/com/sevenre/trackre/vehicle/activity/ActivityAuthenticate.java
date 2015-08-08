package com.sevenre.trackre.vehicle.activity;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.sevenre.trackre.vehicle.R;
import com.sevenre.trackre.vehicle.network.Server;
import com.sevenre.trackre.vehicle.utils.Constants;
import com.sevenre.trackre.vehicle.utils.NetworkConnectivity;
import com.sevenre.trackre.vehicle.utils.SharedPreference;
import com.sevenre.trackre.vehicle.utils.view.GenerateViews;

public class ActivityAuthenticate extends Activity implements OnClickListener{

	EditText schoolId;
	Button enter;
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_authenticate);
		setUpUI();
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	private void setUpUI() {
		schoolId = (EditText) findViewById(R.id.login_vehicle_no);
		enter = (Button) findViewById(R.id.login_enter);
		schoolId.setGravity(Gravity.CENTER);
		schoolId.setHint("Customer No");
		schoolId.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
		enter.setOnClickListener(ActivityAuthenticate.this);
		enter.setText("Authenticate");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_enter:
			if (!NetworkConnectivity.isConnectedToInternet(getApplicationContext())){
				Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
				break;
			}
			String schoolID_ = schoolId.getText().toString();
			dialog = GenerateViews.getProgressDialog(this, "Authenticating Customer No",
						"We are trying to verify your details, please wait for a while!");
			dialog.show();
			new GetSchoolId().execute(schoolID_);
		}
	}
	
	class GetSchoolId extends AsyncTask<String , Integer, Boolean> {

		String schoolId;

		@Override
		protected Boolean doInBackground(String... params) {
			schoolId = Server.authenticateApp(params[0]);
			String info = Server.getSchoolInfo(schoolId);
			SharedPreference.setSchoolInfo(getApplicationContext(), info);
			if (schoolId == null)
				return false; 
			else
				return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if (result) {
				SharedPreference.setSchoolId(getApplicationContext(), schoolId);
				Constants.isAuthentic = true;
				Intent i = new Intent(ActivityAuthenticate.this, ActivityHome.class);
				startActivity(i); 
			} else {
				Toast.makeText(getApplicationContext(), "Error is your Customer No", Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
	}
}