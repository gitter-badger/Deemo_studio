package com.sevenre.trackre.vehicle.utils.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sevenre.trackre.vehicle.R;

public class GenerateViews {

	public static ProgressDialog getProgressDialog (Context context, String title, String msg) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.setCancelable(false);
		//dialog.setProgressStyle(R.style.AppTheme_ProgressDialog);
		return dialog;
	}
	
	public static View getEmptyView (Context context, String message) {
		TextView emptyView = new TextView(context);
		emptyView.setText(message);
		emptyView.setLayoutParams(new ViewGroup.LayoutParams(
		        ViewGroup.LayoutParams.FILL_PARENT,
		        ViewGroup.LayoutParams.FILL_PARENT));
		emptyView.setGravity(Gravity.CENTER);
		emptyView.setTextColor(Color.BLACK);
		emptyView.setTextSize(24f);
		return emptyView;
	}
}
