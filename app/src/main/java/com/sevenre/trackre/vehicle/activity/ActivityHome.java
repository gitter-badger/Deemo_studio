package com.sevenre.trackre.vehicle.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.IconTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.android.iconify.Iconify;
import com.sevenre.trackre.vehicle.R;
import com.sevenre.trackre.vehicle.database.PushDatabase;
import com.sevenre.trackre.vehicle.qrcode.CaptureActivity;
import com.sevenre.trackre.vehicle.utils.Constants;
import com.sevenre.trackre.vehicle.utils.NetworkConnectivity;
import com.sevenre.trackre.vehicle.utils.SharedPreference;
import com.sevenre.trackre.vehicle.utils.Utils;
import com.sevenre.trackre.vehicle.utils.view.VehicleIdDialogBox;

import java.util.ArrayList;
import java.util.List;

public class ActivityHome extends Activity implements OnClickListener {

	View pick, drop, setting, simply_track, other_info;
	AlertDialog.Builder builder;
	Dialog dialog;
	Context mContext;
	String schoolId;
    GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		mContext = getApplicationContext();
		setUpUI();
	}

	private void setUpUI() {
//		pick = findViewById(R.id.home_pick_up);
//		drop = findViewById(R.id.home_drop);
//
//		simply_track = findViewById(R.id.home_just_track);
//		other_info = findViewById(R.id.home_other_info);
//
//		pick.setOnClickListener(ActivityHome.this);
//		drop.setOnClickListener(ActivityHome.this);
//
//		other_info.setOnClickListener(ActivityHome.this);
//		simply_track.setOnClickListener(ActivityHome.this);

        gridView = (GridView)findViewById(R.id.home_screen_grid_view);
        gridView.setAdapter(new GridAdapter());

		TextView tv = (TextView)findViewById(R.id.school_info_home_screen);
		tv.setText(SharedPreference.getSchoolName(mContext) + ", " + SharedPreference.getSchoolCity(mContext));
		tv.setMovementMethod(new ScrollingMovementMethod());
		tv.setTypeface(Utils.getTypeFace(getAssets(), Utils.roboto));
	}

	
	
	@Override
	public void onClick(View v) {
//		Intent i = new Intent();
//		switch (v.getId()) {
//		case R.id.home_drop:
//			if(Utils.isServiceRunning(getApplicationContext())) {
//				i.setClass(ActivityHome.this, MainActivity.class);
//				startActivity(i);
//			} else if (Utils.isTaggingServiceRunning(getApplicationContext())) {
//				i.setClass(ActivityHome.this, ActivityTagging.class);
//				startActivity(i);
//			}else {
//				if (NetworkConnectivity.isConnectedToInternet(getApplicationContext())) {
//					i.setClass(ActivityHome.this, ActivitySelectTrip.class);
//					i.putExtra(Constants.TRIP, Constants.TRIP_DROP);
//					if (dialog!=null)
//						if (dialog.isShowing())
//							dialog.dismiss();
//					dialog = new VehicleIdDialogBox(ActivityHome.this,i);
//					dialog.show();
//				}
//				else {
//					Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
//					break;
//				}
//			}
//			break;
//		case R.id.home_pick_up:
//			if(Utils.isServiceRunning(getApplicationContext())) {
//				i.setClass(ActivityHome.this, MainActivity.class);
//				startActivity(i);
//			} else if (Utils.isTaggingServiceRunning(getApplicationContext())) {
//				i.setClass(ActivityHome.this, ActivityTagging.class);
//				startActivity(i);
//			} else {
//				if (NetworkConnectivity.isConnectedToInternet(getApplicationContext())) {
//					i.setClass(ActivityHome.this, ActivitySelectTrip.class);
//					i.putExtra(Constants.TRIP, Constants.TRIP_PICK_UP);
//					if (dialog!=null)
//						if (dialog.isShowing())
//							dialog.dismiss();
//					dialog = new VehicleIdDialogBox(ActivityHome.this,i);
//					dialog.show();
//
//				}
//				else {
//					Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
//					break;
//				}
//			}
//			break;
//		case R.id.home_just_track:
//			i.setClass(ActivityHome.this, ActivitySimplyTrack.class);
//			startActivity(i);
//			break;
//		case R.id.home_other_info:
//			i.setClass(ActivityHome.this, ActivityOtherInfo.class);
//			startActivity(i);
//			break;
//		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		schoolId = SharedPreference.getSchoolId(mContext);
		if (schoolId.equals(Utils.ERROR)) {
			Intent i = new Intent(ActivityHome.this,ActivityAuthenticate.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			Constants.isAuthentic = false;
			startActivity(i);
		}
		if (!NetworkConnectivity.isConnectedToInternet(getApplicationContext())){
			new PushDatabase().execute(getApplicationContext());
			
			builder = new AlertDialog.Builder(ActivityHome.this);
			builder.setTitle("Network connectivity not found");
			builder.setMessage("Please start your Data Plan");
			builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(Utils.intentForNetworkAccess());
				}
			});
			builder.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			if (dialog!= null)
				if (dialog.isShowing())
					dialog.dismiss();
			dialog = builder.create();
		}
	}

    class GridAdapter extends BaseAdapter {

        List<IconData> data;

        public GridAdapter() {
            data = new ArrayList<>();
            data.add(new IconData(Iconify.IconValue.fa_home,"Drop"));
            data.add(new IconData(Iconify.IconValue.fa_hospital_o,"Pick UP"));
            data.add(new IconData(Iconify.IconValue.fa_map_marker,"Simply Track"));
            data.add(new IconData(Iconify.IconValue.fa_group,"Attendance"));
            data.add(new IconData(Iconify.IconValue.fa_briefcase,"Extras"));
            data.add(new IconData(Iconify.IconValue.fa_cog,"Settings"));

        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int pos, View v, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.icon_home_screen,null);
            view.setMinimumHeight(gridView.getHeight() / 3);

            IconTextView icon = (IconTextView) view.findViewById(R.id.icon_home_icon);
            icon.setText(data.get(pos).icon.formattedName());
            icon.setTextSize(75);
            Iconify.addIcons(icon);

            TextView text = (TextView)view.findViewById(R.id.icon_home_text);
            text.setText(data.get(pos).name);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent();
                    switch (pos) {
                        case 0:
                            if (Utils.isServiceRunning(getApplicationContext())) {
                                i.setClass(ActivityHome.this, MainActivity.class);
                                startActivity(i);
                            } else if (Utils.isTaggingServiceRunning(getApplicationContext())) {
                                i.setClass(ActivityHome.this, ActivityTagging.class);
                                startActivity(i);
                            } else {
                                if (NetworkConnectivity.isConnectedToInternet(getApplicationContext())) {
                                    i.setClass(ActivityHome.this, ActivitySelectTrip.class);
                                    i.putExtra(Constants.TRIP, Constants.TRIP_DROP);
                                    if (dialog != null)
                                        if (dialog.isShowing())
                                            dialog.dismiss();
                                    dialog = new VehicleIdDialogBox(ActivityHome.this, i);
                                    dialog.show();
                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            break;
                        case 1:
                            if (Utils.isServiceRunning(getApplicationContext())) {
                                i.setClass(ActivityHome.this, MainActivity.class);
                                startActivity(i);
                            } else if (Utils.isTaggingServiceRunning(getApplicationContext())) {
                                i.setClass(ActivityHome.this, ActivityTagging.class);
                                startActivity(i);
                            } else {
                                if (NetworkConnectivity.isConnectedToInternet(getApplicationContext())) {
                                    i.setClass(ActivityHome.this, ActivitySelectTrip.class);
                                    i.putExtra(Constants.TRIP, Constants.TRIP_PICK_UP);
                                    if (dialog != null)
                                        if (dialog.isShowing())
                                            dialog.dismiss();
                                    dialog = new VehicleIdDialogBox(ActivityHome.this, i);
                                    dialog.show();

                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            break;
                        case 2:
                            i.setClass(ActivityHome.this, ActivitySimplyTrack.class);
                            startActivity(i);
                            break;
                        case 3:
                            i.setClass (ActivityHome.this,CaptureActivity.class);
                            startActivityForResult(i,21);
                            break;
                        case 4:
                            i.setClass(ActivityHome.this, ActivityOtherInfo.class);
                            startActivity(i);
                            break;
                        case 5:
                            Toast.makeText(mContext,"Currently all settings are done automatically, nothing can be changed", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            });
            return view;
        }
    }

    class IconData {
        Iconify.IconValue icon;
        String name;

        public IconData(Iconify.IconValue icon, String name) {
            this.icon = icon;
            this.name = name;
        }
    }
}
