package com.sevenre.trackre.driver;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sevenre.trackre.driver.datatypes.Trip;
import com.sevenre.trackre.driver.listadapter.TripAdapter;
import com.sevenre.trackre.driver.network.Server;
import com.sevenre.trackre.driver.network.TaggingService;
import com.sevenre.trackre.driver.utils.Constants;
import com.sevenre.trackre.driver.utils.NetworkConnectivity;
import com.sevenre.trackre.driver.utils.SharedPreference;
import com.sevenre.trackre.driver.utils.Utils;
import com.sevenre.trackre.driver.utils.view.GenerateViews;
import com.sevenre.trackre.driver.utils.view.StartTripDialogBox;

import java.util.ArrayList;

public class ActivitySelectTrip extends ActionBarActivity {

    ArrayList<Trip> result;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trip);
        ActionBar bar = getActionBar();

        if (bar != null) {
            bar.setBackgroundDrawable(Constants.ACTION_BAR_COLOR_DRAWABLE);
            bar.setTitle("Pick Up");
            bar.setDisplayHomeAsUpEnabled(true);
        } else {
            //Toast.makeText(getApplication(),"emply action bar", Toast.LENGTH_LONG).show();
        }

        TextView title = ((TextView)findViewById(R.id.select_trip_title));
        title.setText(SharedPreference.getVehicleNo(getApplication()));
        title.setTypeface(Utils.getTypeFace(getAssets(),Utils.roboto));

        dialog = new ProgressDialog(ActivitySelectTrip.this);
        dialog.setTitle("Getting trips");
        dialog.setMessage("Trip list is being fetched, please wait");
        dialog.setButton("Cancel",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        new GetTrip().execute();
    }

    public void updateListView(ArrayList<Trip> results) {
        ListView lv = (ListView) findViewById(R.id.select_trip_list_view);
        lv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        lv.setAdapter(new TripAdapter(this, results, R.layout.list_item_trip_info));
        View emptyView = GenerateViews.getEmptyView(getApplicationContext(), "No Trips found for this vehicle!\n" + SharedPreference.getVehicleNo(getApplicationContext()));
        lv.setEmptyView(findViewById(R.id.empty));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!NetworkConnectivity.isConnectedToInternet(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
                    return;
                }
                final Intent s = new Intent(ActivitySelectTrip.this,
                        TaggingService.class);
                if (dialog != null)
                    if (dialog.isShowing())
                        dialog.cancel();
                StartTripDialogBox d;
                Trip t = (Trip) result.get(position);
                if (t.getTripStatus().contains("track")) {
                    final Intent i = new Intent(ActivitySelectTrip.this,
                            MainActivity.class);
                    d = new StartTripDialogBox(ActivitySelectTrip.this, i, s, t);
                } else {
                    final Intent i = new Intent(ActivitySelectTrip.this,
                            ActivityTagging.class);
                    d = new StartTripDialogBox(ActivitySelectTrip.this, i, s, t);
                }
                d.show();

            }
        });

        dialog.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_trip_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class GetTrip extends AsyncTask<String, Integer, ArrayList<Trip>> {

        @Override
        protected ArrayList<Trip> doInBackground(String... arg0) {
            return Server.getDropTrip(
                    SharedPreference.getVehicleId(getApplicationContext()),
                    SharedPreference.getSchoolId(getApplicationContext()));
        }

        @Override
        protected void onPostExecute(ArrayList<Trip> result) {
            ActivitySelectTrip.this.result = result;
            updateListView(ActivitySelectTrip.this.result);
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onResume() {
        if (!NetworkConnectivity.isGPSConnected(getApplicationContext())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    ActivitySelectTrip.this);
            builder.setTitle("No GPS not found");
            builder.setMessage("Please start your GPS");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(Utils.intentForGPS());
                        }
                    });
            if (dialog != null)
                if (dialog.isShowing())
                    dialog.cancel();
            builder.create().show();
        }
        super.onResume();
    }
}
