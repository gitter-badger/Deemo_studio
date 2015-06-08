package com.sevenre.trackre.driver.utils.view;

import com.sevenre.trackre.driver.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GenerateViews {

	public static ProgressDialog getProgressDialog (Context context,
			String title, String msg) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.setCancelable(false);
		//dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
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
