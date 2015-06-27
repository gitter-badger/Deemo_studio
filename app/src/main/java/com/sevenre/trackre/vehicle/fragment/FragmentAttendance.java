package com.sevenre.trackre.vehicle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.sevenre.trackre.vehicle.R;
import com.sevenre.trackre.vehicle.qrcode.CaptureActivity;

public class FragmentAttendance extends Fragment{

	View rootView, scan_button;
	public static FragmentAttendance newInstance() {
		return new FragmentAttendance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_attendance, container,false);
		scan_button = rootView.findViewById(R.id.attendance_scan_stundent_id);
		scan_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent (getActivity(),CaptureActivity.class);
				startActivityForResult(i,21);
			}
		});
		return rootView;
	}
}
