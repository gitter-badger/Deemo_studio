package com.sevenre.trackre.vehicle.utils.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sevenre.trackre.vehicle.R;
import com.sevenre.trackre.vehicle.utils.Utils;

public class CancelTripDialogBox extends Dialog implements
		android.view.View.OnClickListener {

	Activity a;
	Dialog d;
	Button yes, no;
	String msg;

	public CancelTripDialogBox(Activity a, String msg) {
		super(a);
		this.a = a;
		this.msg = msg;
	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_box_cancel_trip);
		yes = (Button) findViewById(R.id.dialog_box_yes);
		no = (Button) findViewById(R.id.dialog_box_no);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
		yes.setTypeface(Utils.getTypeFace(a.getAssets(), Utils.erasBold));
		no.setTypeface(Utils.getTypeFace(a.getAssets(), Utils.erasBold));
		
		TextView tv = (TextView) findViewById(R.id.dialog_box_select_trip_text);
		tv.setText(msg);
		tv = (TextView) findViewById(R.id.dialog_box_title);
		tv.setText("Cancel Trip?");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_box_yes:
			dismiss();
			a.finish();
			break;
		case R.id.dialog_box_no:
			dismiss();
			break;
		}

	}

}