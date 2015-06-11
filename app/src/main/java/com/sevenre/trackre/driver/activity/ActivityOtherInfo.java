package com.sevenre.trackre.driver.activity;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import com.sevenre.trackre.driver.R;
import com.sevenre.trackre.driver.listadapter.OtherInfoExpandableListAdapter;

public class ActivityOtherInfo extends AppCompatActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_info);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

		ExpandableListView list =  (ExpandableListView)findViewById(R.id.other_info_expandable_list);
		list.setAdapter(new OtherInfoExpandableListAdapter(this));
//		int[] colors = { 0, R.color.violet_3, 0 }; // red for the example
//		list.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
//		list.setDividerHeight(1);
//		list.setChildDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
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
