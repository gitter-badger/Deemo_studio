package com.sevenre.trackre.vehicle.listadapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.sevenre.trackre.vehicle.R;
import com.sevenre.trackre.vehicle.database.InfoDatabaseHandler;
import com.sevenre.trackre.vehicle.datatypes.Contact;
import com.sevenre.trackre.vehicle.datatypes.ParkStop;
import com.sevenre.trackre.vehicle.network.Server;
import com.sevenre.trackre.vehicle.utils.SharedPreference;
import com.sevenre.trackre.vehicle.utils.Utils;
import com.sevenre.trackre.vehicle.utils.view.TagParkStopDialogBox;

public class OtherInfoExpandableListAdapter extends BaseExpandableListAdapter {

	Activity activity;
	TagParkStopDialogBox dialog;
	LayoutInflater inflater;
	//parent 0 - Park Station
	List<ParkStop> parkStop;
	//parent 1 - School Contact
	List<Contact> schoolContact;
	//parent 2 - Emergency Contact
	List<Contact> emergencyContact;

	public OtherInfoExpandableListAdapter(Activity activity ) {
		this.activity = activity;
		this.inflater = LayoutInflater.from(activity.getApplicationContext());
		parkStop = new ArrayList<ParkStop>();
		schoolContact = InfoDatabaseHandler.getContactInfoSchool();
		emergencyContact = InfoDatabaseHandler.getContactInfoEmergency();
		new GetParkStation().execute(SharedPreference.getSchoolId(activity));
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		View resultView = convertView;
		switch (groupPosition) {
		case 0 : // Park stop
			resultView = inflater.inflate(R.layout.list_item_park_stop, null);
			TextView tv = (TextView)resultView.findViewById(R.id.name);
			tv.setText(parkStop.get(childPosition).getName());
			//ImageView img = (ImageView)resultView.findViewById(R.id.img_map);
			//new DownloadImageTask(img,parkStop.get(childPosition).getLat(),parkStop.get(childPosition).getLng()).execute();
       
			resultView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (dialog!=null)
						if (dialog.isShowing())
							dialog.cancel();
					 MapFragment f = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.dialog_tag_park_map);
					 if (f != null) 
						 activity.getFragmentManager().beginTransaction().remove(f).commit();
					dialog = new TagParkStopDialogBox (activity,parkStop.get(childPosition));
					dialog.show();
				}
			});
			return resultView;
			
		case 1 : // school contact
			resultView = inflater.inflate(R.layout.list_item_contact, null);
			TextView name = (TextView) resultView.findViewById(R.id.name);
			TextView no = (TextView) resultView.findViewById(R.id.contact_no);
			name.setText(schoolContact.get(childPosition).getName());
			no.setText(schoolContact.get(childPosition).getPhoneNumber());
			resultView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

                    new AlertDialog.Builder(activity)
                            .setTitle("Call " + schoolContact.get(childPosition).getName()+"?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + schoolContact.get(childPosition).getPhoneNumber()));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
					
				}
			});
			return resultView;
			
		case 2 : // emergency contact
			resultView = inflater.inflate(R.layout.list_item_contact, null);
			TextView name1 = (TextView) resultView.findViewById(R.id.name);
			TextView no2 = (TextView) resultView.findViewById(R.id.contact_no);
			name1.setText(emergencyContact.get(childPosition).getName());

			no2.setText(emergencyContact.get(childPosition).getPhoneNumber());
			resultView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

                    new AlertDialog.Builder(activity)
                            .setTitle("Call " + emergencyContact.get(childPosition).getName()+"?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + emergencyContact.get(childPosition).getPhoneNumber()));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();


				}
			});
			return resultView;
		}
		return null;
	}

	@Override
	public int getChildrenCount(int pos) {
		switch (pos) {
		case 0 : return parkStop.size();
		case 1 : return schoolContact.size();
		case 2 : return emergencyContact.size();
		default:
			return 0;
		}
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		View resultView = convertView;
		resultView = inflater.inflate(R.layout.list_item_other_info_parent, null);
		TextView tv = (TextView)resultView.findViewById(R.id.name);
		switch (groupPosition) {
		case 0 :
			tv.setText("Park Station");
			break;
		case 1 :
			tv.setText("School Contact");
			break;
		case 2 :
			tv.setText("Emergency Contact");
			break;
		default:
			break;
		}
		
		return resultView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	class GetParkStation extends AsyncTask<String, Integer, ArrayList<ParkStop>> {

		@Override
		protected ArrayList<ParkStop> doInBackground(String... params) {
			parkStop = Server.getParkStation(SharedPreference.getSchoolId(activity));
			return (ArrayList<ParkStop>) parkStop;
		}
		
		@Override
		protected void onPostExecute(ArrayList<ParkStop> result) {
			OtherInfoExpandableListAdapter.this.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}
}
