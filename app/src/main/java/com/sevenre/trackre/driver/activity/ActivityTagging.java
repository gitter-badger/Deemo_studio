package com.sevenre.trackre.driver.activity;

import java.util.ArrayList;

import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.sevenre.trackre.driver.R;
import com.sevenre.trackre.driver.database.LiveDatabaseHandler;
import com.sevenre.trackre.driver.datatypes.TaggingStop;
import com.sevenre.trackre.driver.listadapter.TaggingStopAdapter;
import com.sevenre.trackre.driver.network.Server;
import com.sevenre.trackre.driver.utils.Constants;
import com.sevenre.trackre.driver.utils.Log;
import com.sevenre.trackre.driver.utils.NetworkConnectivity;
import com.sevenre.trackre.driver.utils.SharedPreference;
import com.sevenre.trackre.driver.utils.Utils;
import com.sevenre.trackre.driver.utils.view.CancelTripDialogBox;
import com.sevenre.trackre.driver.utils.view.CompleteDialogBox;

public class ActivityTagging extends ActionBarActivity implements OnClickListener{
	
	ArrayList<TaggingStop> result;
	ListView lv;
	Context context;
	Dialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tagging);
		context = getApplicationContext();
		setUpUI();
	}
	
	private void setUpUI() {
		lv = (ListView)findViewById(R.id.tagging_list_view);
		ActionBar bar = getSupportActionBar();

		if (bar != null) {
			bar.setBackgroundDrawable(Constants.ACTION_BAR_COLOR_DRAWABLE);
			bar.setTitle(SharedPreference.getTripId(context) + " " + SharedPreference.getTripStatus(context));
			bar.setDisplayHomeAsUpEnabled(true);
		}

//		((ImageButton)findViewById(R.id.tagging_add_new_stop)).setEnabled(false);
//		Calendar c = Calendar.getInstance();
//		((EditText)findViewById(R.id.edit_text_add_stop_hr)).setText(c.get(Calendar.HOUR));
//		((EditText)findViewById(R.id.edit_text_add_stop_min)).setText(c.get(Calendar.MINUTE));
//		if (c.get(Calendar.HOUR_OF_DAY)>12){
//			((Button)findViewById(R.id.button_add_stop_am_pm)).setText("PM");
//		} else {
//			((Button)findViewById(R.id.button_add_stop_am_pm)).setText("AM");
//		}
//		EditText et = (EditText)findViewById(R.id.edit_text_add_stop_name);
//		et.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				if (s.length() > 3){
//					((ImageButton)findViewById(R.id.tagging_add_new_stop)).setEnabled(true);
//				} else {
//					((ImageButton)findViewById(R.id.tagging_add_new_stop)).setEnabled(false);
//				}
//			}
//			
//			@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//			
//			@Override public void afterTextChanged(Editable s) { }
//		});
	}

	@Override
	public void onClick(View v) {
		
	}
	
	public void taggingOnClick(View v) {
		switch (v.getId()){
		case R.id.tagging_button_cancel:
			if (dialog!=null) 
				if (dialog.isShowing())
					dialog.cancel();
			dialog = new CancelTripDialogBox(this, "Are you sure you want to Cancel trip?");
			dialog.show();
			break;
		case R.id.tagging_button_completed:
			if (dialog!=null) 
				if (dialog.isShowing())
					dialog.cancel();
			dialog = new CompleteDialogBox(this, "Are you sure you want to Complete trip?");
			dialog.show();
			break;
//		case R.id.tagging_button_add:
//			if (Constants.isDev)
//				break;
//			if (!Constants.isDev)
//				break;
//			findViewById(R.id.taggin_ll_add_details).setVisibility(View.VISIBLE);
//			findViewById(R.id.tagging_ll_controls).setVisibility(View.GONE);
//			Calendar c = Calendar.getInstance();
//			((EditText)findViewById(R.id.edit_text_add_stop_hr)).setText(""+c.get(Calendar.HOUR));
//			((EditText)findViewById(R.id.edit_text_add_stop_min)).setText(""+c.get(Calendar.MINUTE));
//			if (c.get(Calendar.HOUR_OF_DAY)>12){
//				((Button)findViewById(R.id.button_add_stop_am_pm)).setText("PM");
//			} else {
//				((Button)findViewById(R.id.button_add_stop_am_pm)).setText("AM");
//			}
//			break;
//		case R.id.tagging_add_new_stop_cancel:
//			findViewById(R.id.tagging_ll_controls).setVisibility(View.VISIBLE);
//			findViewById(R.id.taggin_ll_add_details).setVisibility(View.GONE);
//			hideKeyboard();
//			break;
//		case R.id.tagging_add_new_stop:
//			findViewById(R.id.tagging_ll_controls).setVisibility(View.VISIBLE);
//			findViewById(R.id.taggin_ll_add_details).setVisibility(View.GONE);
//			hideKeyboard();
//			break;
		}
	}
	
	void hideKeyboard(){
		InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}
	
	private void updateListView(ArrayList<TaggingStop> result) {
		this.result = result;
		TaggingStopAdapter adapter = new TaggingStopAdapter(this, result, getResources());
		lv.setAdapter(adapter);
//		int[] colors = { 0, R.color.violet_3, 0 }; // red for the example
//		lv.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		lv.setDividerHeight(0);
		lv.setOnItemClickListener(adapter);
	}
	
	class GetTaggingStop extends AsyncTask<String, Integer, ArrayList<TaggingStop>> {

		@Override
		protected ArrayList<TaggingStop> doInBackground(String... arg0) {
			return Server.getTaggingStopList(SharedPreference.getTripId(context), SharedPreference.getSchoolId(context));
		}

		@Override
		protected void onPostExecute(ArrayList<TaggingStop> result) {
			updateListView(result);
			super.onPostExecute(result);
		}
	}
	
	@Override
	protected void onResume() {
		if (!NetworkConnectivity.isGPSConnected(context)){
			AlertDialog.Builder builder = new AlertDialog.Builder(ActivityTagging.this);
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
		result = (new LiveDatabaseHandler(context)).getTaggingTimeTable();
		for (TaggingStop o : result )
		{
			if (o.isTagged()) {
				Log.e(o.getName());
			}
		}
		updateListView(result);
		super.onResume();
	}
	
}
