package com.sevenre.trackre.vehicle.listadapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sevenre.trackre.vehicle.R;
import com.sevenre.trackre.vehicle.datatypes.Trip;
import com.sevenre.trackre.vehicle.utils.Utils;

public class TripAdapter extends BaseAdapter {

	private ArrayList<Trip> data;
	LayoutInflater inflater;
	Activity activity;
	int i = 0;
	int inflate;

	public TripAdapter(Activity a, ArrayList<Trip> d, int l) {
		data = d;
		inflate = l;
		inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		activity = a;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		vi = inflater.inflate(inflate, null);
		Trip t = (Trip) data.get(position);
		
		TextView route_no = (TextView) vi.findViewById(R.id.list_item_trip_route_no);
		route_no.setText(t.getName());
		route_no.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.roboto));
		
		TextView time = (TextView) vi.findViewById(R.id.list_item_trip_time);
		time.setText(t.getTime());
		time.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.roboto));
		
		TextView description = (TextView) vi.findViewById(R.id.list_item_trip_description);
		if (t.getTripDescription() == null || t.getTripDescription().length()<=1 )
			description.setText("Not set");
		else
			description.setText(t.getTripDescription());
		description.setTypeface(Utils.getTypeFace(activity.getAssets(), Utils.roboto));
		
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