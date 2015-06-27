package com.sevenre.trackre.vehicle.network;

import android.telephony.SmsManager;

public class SMSHandler {

	public static void sendSms(String no, String msg) {
		msg = "TrackRE,"+msg;
		SmsManager smsManager = SmsManager.getDefault();
		//smsManager.sendTextMessage(no, null, msg, null, null);
	}
}
