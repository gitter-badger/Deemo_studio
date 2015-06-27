package com.sevenre.trackre.vehicle.listadapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sevenre.trackre.vehicle.R;
import com.sevenre.trackre.vehicle.datatypes.Contact;

public class ContactAdapter extends BaseAdapter implements android.widget.AdapterView.OnItemClickListener {

	LayoutInflater inflater;
	Context context;
	List<Contact> data;
	Resources resources;
	
	public ContactAdapter(Context a, List<Contact> list, Resources resources) {
		data = list;
		context = a;
		this.resources = resources;
		inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int pos) {
		return data.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		vi = inflater.inflate(R.layout.list_item_contact, null);
		
		TextView name = (TextView) vi.findViewById(R.id.name);
		TextView no = (TextView) vi.findViewById(R.id.contact_no);
		
		name.setText(data.get(position).getName());
		no.setText(data.get(position).getPhoneNumber());
		return vi;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.get(pos).getPhoneNumber()));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
