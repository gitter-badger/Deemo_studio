package com.sevenre.trackre.driver.listadapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sevenre.trackre.driver.R;
import com.sevenre.trackre.driver.database.LiveDatabaseHandler;
import com.sevenre.trackre.driver.datatypes.TaggingStop;
import com.sevenre.trackre.driver.network.Server;
import com.sevenre.trackre.driver.network.TrackService;
import com.sevenre.trackre.driver.utils.Log;
import com.sevenre.trackre.driver.utils.NetworkConnectivity;
import com.sevenre.trackre.driver.utils.SharedPreference;
import com.sevenre.trackre.driver.utils.Utils;

public class TaggingStopAdapter extends BaseAdapter implements android.widget.AdapterView.OnItemClickListener{

	private ArrayList<TaggingStop> data;
	LayoutInflater inflater;
	int i = 0;
	Activity activity;
	Resources resources;
	String tripId, tripStatus;
	
	public TaggingStopAdapter(Activity a, ArrayList<TaggingStop> d, Resources resources) {
		data = new ArrayList<TaggingStop>();
		for (int i = 0; i < d.size(); i++) {
			TaggingStop s = d.get(i);
			if ("RS".equalsIgnoreCase(s.getType())) 
				data.add(s);
		}
		activity = a;
		this.resources = resources;
		tripId = SharedPreference.getTripId(a);
		tripStatus = SharedPreference.getTripStatus(a);
		inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		vi = inflater.inflate(R.layout.list_item_tagging_stop, null);
		if (position == 0) {
			vi.findViewById(R.id.route_line_top).setBackgroundColor(Color.TRANSPARENT);
		}
		
		if (position == data.size()-1) {
			vi.findViewById(R.id.route_line_bottom).setBackgroundColor(Color.TRANSPARENT);;
		}

		TextView route_no = (TextView) vi.findViewById(R.id.active_trip_next_stop_no);
		TextView time = (TextView) vi.findViewById(R.id.active_trip_next_stop_time);
		TextView name = (TextView) vi.findViewById(R.id.active_trip_next_stop_name);
		
		route_no.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.robotoCondensed));
		time.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.roboto));
		name.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.roboto));
		
		TaggingStop t = (TaggingStop) data.get(position);
		route_no.setText(""+(Integer.parseInt(t.getNo())-1));
		time.setText(t.getTime());
		name.setText(t.getName());
		
		if (t.isTagged()) {
			route_no.setTextColor(resources.getColor(R.color.orange_light));
			time.setTextColor(resources.getColor(R.color.orange_light));
			name.setTextColor(resources.getColor(R.color.orange_light));
			//vi.findViewById(R.id.list_item_tagging_line).setBackgroundColor(resources.getColor(R.color.orange_light));
		}
		return vi;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return (position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
		if (!data.get(pos).isTagged())
			(new TagDialogBox(activity, pos)).show();
		notifyDataSetChanged();
	}

	void showCustomDialog(final int pos) {
		final TaggingStop t = data.get(pos);
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(t.getName() + "?");
		builder.setMessage("Are you sure you are standing at stop -  "  + t.getName()  + " ?");
		builder.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				data.remove(pos);
				data.add(pos, t);
				notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				t.setTagged(true);
				data.remove(pos);
				data.add(pos, t);
				notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		builder.create().show();		
	}
	
	public class TagDialogBox extends Dialog implements android.view.View.OnClickListener {

		Dialog d;
		Button yes, no;
		int pos;
		String msg, title;
		TaggingStop t;
		
		public TagDialogBox(Activity a, int pos) {
			super(a);
			this.pos = pos;
			t = data.get(pos);
			this.title =  t.getName() + "?";
			this.msg = "Are you sure you are standing at stop -  "  + t.getName()  + " ?";
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_box_tag_stop);
			yes = (Button) findViewById(R.id.dialog_box_yes);
			no = (Button) findViewById(R.id.dialog_box_no);
			yes.setOnClickListener(this);
			no.setOnClickListener(this);
			yes.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.erasBold));
			no.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.erasBold));
			
			TextView tv = (TextView) findViewById(R.id.dialog_box_select_trip_text);
			tv.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.roboto));
			tv.setText(msg);
			tv = (TextView) findViewById(R.id.dialog_box_title);
			tv.setText(title);
			tv.setMovementMethod(new ScrollingMovementMethod());
			tv.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.erasBold));
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.dialog_box_yes:
				if (!NetworkConnectivity.isGPSConnected(activity)) {
					Toast.makeText(activity, R.string.no_gps, 6000).show();
					break;
				}
				new TagPosition().execute();
				dismiss();
				break;
			case R.id.dialog_box_no:
				data.remove(pos);
				data.add(pos, t);
				notifyDataSetChanged();
				dismiss();
				break;
			}
		}
		
		public class TagPosition extends AsyncTask <TaggingStop, Integer, Boolean> {
			TaggingStop stop ;

			@Override
			protected Boolean doInBackground(TaggingStop... params) {
				stop = t;
				stop.setTripId(SharedPreference.getTripId(activity));
				stop.setLat(TrackService.mLat);
				stop.setLng(TrackService.mLng);
				stop.setDate(Server.getDate());
				stop.setTime(Server.getTime());
				stop.setSpeed(TrackService.mSpeed);
				return Server.tagStop(stop,true);
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				t.setTagged(true);
				data.remove(pos);
				data.add(pos, t);
				(new LiveDatabaseHandler(activity)).stopTagged(t.getId());
				notifyDataSetChanged();
				if (!result) {
					LiveDatabaseHandler db = new LiveDatabaseHandler(activity);
					stop.setTripId(SharedPreference.getTripId(activity));
					db.addTagStop(stop);
				} 
				super.onPostExecute(result);
			}
		}
	}
}