package com.sevenre.trackre.driver;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ExpandableListActivity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.sevenre.trackre.driver.database.InfoDatabaseHandler;
import com.sevenre.trackre.driver.listadapter.ContactAdapter;
import com.sevenre.trackre.driver.listadapter.OtherInfoExpandableListAdapter;
import com.sevenre.trackre.driver.utils.Constants;

public class ActivityOtherInfo extends ActionBarActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_info);
		ActionBar bar = getActionBar();

		if (bar != null) {
			bar.setBackgroundDrawable(Constants.ACTION_BAR_COLOR_DRAWABLE);
			bar.setDisplayHomeAsUpEnabled(true);
		}

		ExpandableListView list =  (ExpandableListView)findViewById(R.id.other_info_expandable_list);
		list.setAdapter(new OtherInfoExpandableListAdapter(this));
		int[] colors = { 0, R.color.violet_3, 0 }; // red for the example
		list.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		list.setDividerHeight(1);
		list.setChildDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
//		ListView lv = (ListView) findViewById(R.id.list_contacts);
//		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6A3466")));
//		getActionBar().setTitle("Contact List");
//		int[] colors = { 0, R.color.violet_3, 0 }; // red for the example
//		lv.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
//		lv.setDividerHeight(3);
//		ContactAdapter adapter = new ContactAdapter(getApplicationContext(), InfoDatabaseHandler.getContactInfo(), getResources()); 
//		lv.setAdapter(adapter);
//		lv.setOnItemClickListener(adapter);
	}
}
