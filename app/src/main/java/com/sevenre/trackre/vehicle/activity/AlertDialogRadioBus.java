package com.sevenre.trackre.vehicle.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

import com.sevenre.trackre.vehicle.datatypes.Bus;
import com.sevenre.trackre.vehicle.utils.Constants;
import com.sevenre.trackre.vehicle.utils.SharedPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionService;

public class AlertDialogRadioBus extends DialogFragment{

    AlertPositiveListener alertPositiveListener;

    List<Bus> busList;
    int TRIP_TYPE;

    interface AlertPositiveListener {
        public void onPositiveClick(int position);
    }

    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        try{
            alertPositiveListener = (AlertPositiveListener) activity;
        } catch (ClassCastException e){ }
    }

    OnClickListener positiveListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            AlertDialog alert = (AlertDialog)dialog;
            int position = alert.getListView().getCheckedItemPosition();
            SharedPreference.setVehicleId(getActivity(), busList.get(position).getBusId());
            SharedPreference.setVehicleNo(getActivity(), busList.get(position).getBusNo());
            Intent intent = new Intent(getActivity(),ActivitySelectTrip.class);
            intent.putExtra(Constants.TRIP,TRIP_TYPE);
            startActivity(intent);
            dismiss();
        }
    };

    OnClickListener negativeListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            AlertDialog alert = (AlertDialog)dialog;
            int position = alert.getListView().getCheckedItemPosition();
            dismiss();
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int position = bundle.getInt("position");
        TRIP_TYPE = bundle.getInt(Constants.TRIP);
        busList = Constants.busList;

        ArrayList<String> list = new ArrayList<>();
        for (Bus o: busList) {
            list.add(o.getBusNo());
        }
        String[] stringArray = list.toArray(new String[list.size()]);

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle("Select Bus");
        b.setSingleChoiceItems(stringArray, position, null);
        b.setPositiveButton("OK",positiveListener);
        b.setNegativeButton("Cancel", negativeListener);
        AlertDialog d = b.create();
        return d;
    }
}
