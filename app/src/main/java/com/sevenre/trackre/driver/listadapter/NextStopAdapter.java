package com.sevenre.trackre.driver.listadapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sevenre.trackre.driver.R;
import com.sevenre.trackre.driver.datatypes.Stop;
import com.sevenre.trackre.driver.utils.Utils;

public class NextStopAdapter extends BaseAdapter {

	private ArrayList<Stop> data;
	LayoutInflater inflater;
	int i = 0;
	Activity activity;

	public NextStopAdapter(Activity a, ArrayList<Stop> d) {
		data = new ArrayList<Stop>();
		for (int i = 0; i < d.size(); i++) {
			Stop s = d.get(i);
			if ("RS".equalsIgnoreCase(s.getType())) 
				data.add(s);
		}
		activity = a;
		inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		vi = inflater.inflate(R.layout.list_item_active_trip_stop, null);

		if (position == 0) {
			vi.findViewById(R.id.route_line_top).setBackgroundColor(Color.TRANSPARENT);
		}

		if (position == data.size()-1) {
			vi.findViewById(R.id.route_line_bottom).setBackgroundColor(Color.TRANSPARENT);;
		}

		//TextView route_no = (TextView) vi.findViewById(R.id.active_trip_next_stop_no);
		TextView time = (TextView) vi.findViewById(R.id.active_trip_next_stop_time);
		TextView name = (TextView) vi.findViewById(R.id.active_trip_next_stop_name);
		
		//route_no.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.robotoCondensed));
		time.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.roboto));
		name.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.roboto));
		
		Stop t = (Stop) data.get(position);
		//route_no.setText(""+(Integer.parseInt(t.getNo())-1));
		time.setText(t.getTime().substring(0,5));
		name.setText(t.getName());
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

	
}