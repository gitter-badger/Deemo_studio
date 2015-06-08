package com.sevenre.trackre.driver;

import java.util.Locale;

import com.sevenre.trackre.driver.fragment.FragmentAttendance;
import com.sevenre.trackre.driver.fragment.FragmentTracking;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(243,187,90)));
		actionBar.setIcon(getResources().getDrawable(R.drawable.icon_home));
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#B5C0D0")));
		
		new SetupUI().execute();
	}

	class SetupUI extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			
			mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);
			mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					getActionBar().setSelectedNavigationItem(position);
				}
			});

			ActionBar actionBar = getSupportActionBar();
			for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
				actionBar.addTab(actionBar.newTab()
						.setText(mSectionsPagerAdapter.getPageTitle(i))
						.setTabListener( MainActivity.this));
			}
			return null;
		}
		
	}
	
	@Override
	public void onTabSelected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return FragmentTracking.newInstance();
			case 1:
				return FragmentAttendance.newInstance();
			}
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
}
