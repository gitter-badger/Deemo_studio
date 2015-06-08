package com.sevenre.trackre.driver.network;

import android.telephony.SmsManager;
import android.util.Log;

public class SMSHandler {

	public static void sendSms(String no, String msg) {
		msg = "TrackRE,"+msg;
		SmsManager smsManager = SmsManager.getDefault();
		//smsManager.sendTextMessage(no, null, msg, null, null);
	}
}
