package com.sevenre.trackre.vehicle.database;

import java.util.ArrayList;
import java.util.List;

import com.sevenre.trackre.vehicle.datatypes.Contact;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InfoDatabaseHandler  extends SQLiteOpenHelper {

	
	private static final String DATABASE_NAME = "Info";
	private static final int DATABASE_VERSION = 1;

	public InfoDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	public static List<Contact> getContactInfo() {
		List<Contact> contacts = new ArrayList<Contact>();
		contacts.add(new Contact("Track RE", "+4915163005818"));
		contacts.add(new Contact("Pratip", "+4917624325146"));
		contacts.add(new Contact("Neeraj", "+4917684304578"));
		contacts.add(new Contact("Principal", "+4915163005818"));
		contacts.add(new Contact("Ambulance", "111"));
		contacts.add(new Contact("Fire", "101"));
		return contacts;
	}
}
