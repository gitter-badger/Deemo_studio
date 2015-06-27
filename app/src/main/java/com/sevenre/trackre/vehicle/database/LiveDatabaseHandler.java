package com.sevenre.trackre.vehicle.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.sevenre.trackre.vehicle.datatypes.BusPosition;
import com.sevenre.trackre.vehicle.datatypes.Stop;
import com.sevenre.trackre.vehicle.datatypes.TaggingStop;
import com.sevenre.trackre.vehicle.utils.Log;

public class LiveDatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "SevenREDB";
	private final String TABLE_TAG_POSITION = "TagPosition";
	private final String TABLE_TRIP_ACTIVITY = "TripActivity";
	private final String TABLE_TAG_STOP = "TagStop";
	private final String TABLE_TIME_TABLE = "TimeTable";
	
	//common rows
	private final String LAT = "lat";
	private final String LNG = "lng";
	private final String SPEED = "speed";
	private final String DATE = "date";
	private final String TIME = "time";
	private final String ID = "id";
	
	private final String TRIP_ID = "tripId";
	private final String TRIP_STATUS = "tripStatus";
	private final String STOP_ID = "stopId";
	private final String ACTIVITY = "activity";
	private final String NO = "no";
	private final String TYPE = "type";
	private final String NAME = "name";
	private final String ISTAG = "isTag";
	private final String ISTAGSTOP = "isTagStop";
	private final String ISREACHED = "isReached";
	
	final String CREATE_TABLE_TAGPOSITION = "CREATE TABLE IF NOT EXISTS " + TABLE_TAG_POSITION + " ( " +
			ID + " INTEGER PRIMARY KEY   AUTOINCREMENT, "+
			TRIP_ID + " TEXT, " +
			TRIP_STATUS + " TEXT, " +
			DATE + " TEXT, " +
			TIME + " TEXT, " +
			LAT + " TEXT, " +
			LNG +  " TEXT, " +
			SPEED+ " TEXT ) ";
	
	final String CREATE_TABLE_TRIPACTIVITY = "CREATE TABLE IF NOT EXISTS " + TABLE_TRIP_ACTIVITY + " ( " +
			ID + " INTEGER PRIMARY KEY   AUTOINCREMENT, "+
			TRIP_ID + " TEXT, " +
			TRIP_STATUS + " TEXT, " +
			DATE + " TEXT, " +
			TIME + " TEXT, " +
			LAT + " TEXT, " +
			LNG +  " TEXT, " +
			SPEED + " TEXT,  " +
			ACTIVITY + " TEXT)";
			
	final String CREATE_TABLE_TAGSTOP = "CREATE TABLE IF NOT EXISTS " + TABLE_TAG_STOP + " ( " +
			ID + " INTEGER PRIMARY KEY   AUTOINCREMENT, "+
			STOP_ID + " TEXT, " +
			DATE + " TEXT, " +
			TIME + " TEXT, " +
			LAT + " TEXT, " +
			LNG +  " TEXT, " +
			SPEED + " TEXT, " +
			TRIP_ID + " TEXT ) ";
	
	final String CREATE_TABLE_TIMETABLE = "CREATE TABLE " + TABLE_TIME_TABLE + " ( " +
			STOP_ID + " TEXT, " +   //0
			NO + " TEXT, " +		//1
			TYPE + " TEXT, " +		//2
			NAME + " TEXT, " +		//3
			TIME + " TEXT, " +		//4
			LAT + " TEXT, " +		//5
			LNG +  " TEXT, " +		//6
			ISTAG + " INTEGER, " +	//7
			ISTAGSTOP + " INTEGER, " +	//8
			ISREACHED + " INTEGER )";	//9
	
	public LiveDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	public LiveDatabaseHandler(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_TRIPACTIVITY);
		db.execSQL(CREATE_TABLE_TAGPOSITION);
		db.execSQL(CREATE_TABLE_TAGSTOP);
		db.execSQL(CREATE_TABLE_TIMETABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {	}

	public void addTagPosition (List<BusPosition> positions) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			for (int i = 0; i < positions.size() ; i++) {
				BusPosition pos = positions.get(i);
				ContentValues values = new ContentValues();
			    values.put(TRIP_ID, pos.getTripId());
			    values.put(TRIP_STATUS, pos.getTripStatus());
			    values.put(DATE, pos.getDate());
			    values.put(TIME, pos.getTime());
			    values.put(LAT, pos.getLat());
			    values.put(LNG, pos.getLng());
			    values.put(SPEED, pos.getSpeed());
			    db.insert(TABLE_TAG_POSITION, null, values);
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<BusPosition> getAllTagPosition () {
		List<BusPosition> positions = new ArrayList<BusPosition>();
		try {
			String select = "SELECT * FROM " + TABLE_TAG_POSITION;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(select, null);
			if (cursor.moveToFirst()) {
			    do {
			        BusPosition pos = new BusPosition();
			        pos.setId(cursor.getString(0));
			        pos.setTripId(cursor.getString(1));
			        pos.setTripStatus(cursor.getString(2));
			        pos.setDate(cursor.getString(3));
			        pos.setTime(cursor.getString(4));
			        pos.setLat(cursor.getString(5));
			        pos.setLng(cursor.getString(6));
			        pos.setSpeed(cursor.getString(7));
			        positions.add(pos);
			    } while (cursor.moveToNext());
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return positions;
	}
	
	public void removeTagPosition (List<String> key) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			for (int i = 0; i < key.size(); i++) {
				db.delete(TABLE_TAG_POSITION, ID + " = ?",
			        new String[] { String.valueOf(key.get(i)) });
			}
			Log.e("Removed tag stop with keys");
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeAllTagPosition() {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG_POSITION);
			db.execSQL(CREATE_TABLE_TAGPOSITION);
			Log.e("Removed all tag stop");
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopTagged(String stopId) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(ISTAG, 1);
			db.update(TABLE_TIME_TABLE, values, STOP_ID + " =? " , new String[] {stopId});
			Log.e("Stop tagged : " + stopId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateTimeTable(ArrayList<Stop> stops) {
		SQLiteDatabase db;
		try {
			db = this.getWritableDatabase();
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIME_TABLE);
			db.execSQL(CREATE_TABLE_TIMETABLE);
			for (int i = 0; i < stops.size(); i++) {
				Stop stop = stops.get(i);
				ContentValues values = new ContentValues();
				values.put(STOP_ID, stop.getId());
				values.put(NAME, stop.getName());
				values.put(LAT, stop.getLat());
				values.put(LNG, stop.getLng());
				values.put(TIME, stop.getServerTime());
				values.put(NO, stop.getNo());
				values.put(TYPE, stop.getType());
				values.put(ISTAG, 0);
				values.put(ISTAGSTOP, 0);
				values.put(ISREACHED, 0);
				db.insert(TABLE_TIME_TABLE, null, values);
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public ArrayList<Stop> getTimeTable() {
		ArrayList<Stop> stop = new ArrayList<Stop>();
		try {
			String select = "SELECT * FROM " + TABLE_TIME_TABLE;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(select, null);
			if (cursor.moveToFirst()) {
			    do {
			        Stop pos = new Stop();
			        pos.setId(cursor.getString(0));
			        pos.setNo(cursor.getString(1));
			        pos.setType(cursor.getString(2));
			        pos.setName(cursor.getString(3));
			        pos.setTime(cursor.getString(4));
			        pos.setLat(Double.parseDouble(cursor.getString(5)));
			        pos.setLng(Double.parseDouble(cursor.getString(6)));
			        pos.setReached(cursor.getInt(9)==0?false:true);
			        stop.add(pos);
			    } while (cursor.moveToNext());
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stop;
	}
	
	public ArrayList<TaggingStop> getTaggingTimeTable() {
		ArrayList<TaggingStop> stop = new ArrayList<TaggingStop>();
		try {
			String select = "SELECT * FROM " + TABLE_TIME_TABLE;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(select, null);
			if (cursor.moveToFirst()) {
			    do {
			        TaggingStop pos = new TaggingStop();
			        pos.setId(cursor.getString(0));
			        pos.setNo(cursor.getString(1));
			        pos.setType(cursor.getString(2));
			        pos.setName(cursor.getString(3));
			        pos.setTime(cursor.getString(4));
			        pos.setLat(Double.parseDouble(cursor.getString(5)));
			        pos.setLng(Double.parseDouble(cursor.getString(6)));
			        pos.setTagged(cursor.getInt(7)==0?false:true);
			        pos.setReached(Integer.parseInt(cursor.getString(9))==0?false:true);
			        stop.add(pos);
			    } while (cursor.moveToNext());
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stop;
	}
	
	public void setReached (int id) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.rawQuery("Update " + TABLE_TIME_TABLE + " SET " + 
					ISREACHED + " = '1' WHERE " 
					+ ID + " = " + id, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isReached (int id) {
		try {
			String select = "SELECT * FROM " + TABLE_TIME_TABLE;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(select, null);
			if (cursor.moveToFirst()) {
			    do {
					if (cursor.getInt(9) == 1)
						return true;
			    } while (cursor.moveToNext());
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void addTripActivity(String[] s) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(TRIP_ID, s[0]);
			values.put(TRIP_STATUS, s[1]);
			values.put(TIME, s[2]);
			values.put(DATE, s[3]);
			values.put(LAT, s[4]);
			values.put(LNG, s[5]);
			values.put(SPEED, s[6]);
			values.put(ACTIVITY, s[7]);
			db.insert(TABLE_TRIP_ACTIVITY, null, values);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String[]> getAllTripActivity() {
		List<String[]> result = new ArrayList<String[]>();
		try {
			String select = "SELECT * FROM " + TABLE_TRIP_ACTIVITY;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(select, null);
			if (cursor.moveToFirst()) {
			    do {
			        String[] str = {
			        		cursor.getString(0), //key
			        		cursor.getString(1), //tripid
			        		cursor.getString(2), //status
			        		cursor.getString(3), //date
			        		cursor.getString(4), //time
			        		cursor.getString(5), //lat
			        		cursor.getString(6), //lng
			        		cursor.getString(7), //speed
			        		cursor.getString(8) //activity
			        };
			        		
			        result.add(str);
			    } while (cursor.moveToNext());
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result ;
	}
	
	public void removeTripActivity (List<String> key) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			for (int i = 0; i < key.size(); i++) {
				db.delete(TABLE_TRIP_ACTIVITY, ID + " = ?",
			        new String[] { String.valueOf(key.get(i)) });
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeAllTripActivity() {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP_ACTIVITY);
			db.execSQL(CREATE_TABLE_TRIPACTIVITY);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<TaggingStop> getAllTagStop() {
		List<TaggingStop> result = new ArrayList<TaggingStop>();
		try {
			String select = "SELECT * FROM " + TABLE_TAG_STOP;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(select, null);
			if (cursor.moveToFirst()) {
			    do {
			    	TaggingStop stop = new TaggingStop();
			    	stop.setDbId(cursor.getString(0));
			    	stop.setId(cursor.getString(1));
			    	stop.setDate(cursor.getString(2));
			    	stop.setTime(cursor.getString(3));
			    	stop.setLat(Double.parseDouble(cursor.getString(4)));
			    	stop.setLng(Double.parseDouble(cursor.getString(5)));
			    	stop.setSpeed(Double.parseDouble(cursor.getString(6)));
			    	stop.setTripId(cursor.getString(7));
			    	result.add(stop);
			    } while (cursor.moveToNext());
			}
			db.close();
		} catch (Exception e) {
		}
		return result ;
	}
	
	public void addTagStop (TaggingStop stop) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(STOP_ID, stop.getId());
			values.put(DATE, stop.getDate());
			values.put(TIME, stop.getTime());
			values.put(LAT, stop.getLat());
			values.put(LNG, stop.getLng());
			values.put(SPEED, stop.getSpeed());
			values.put(TRIP_ID, stop.getTripId() );
			db.insert(TABLE_TAG_STOP, null, values);
			Log.e("Inserted tag stop " + stop.toString());
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeTagStop(List<String> key) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			for (int i = 0; i < key.size(); i++) {
				db.delete(TABLE_TAG_STOP, ID + " = ?",
			        new String[] { String.valueOf(key.get(i)) });
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeAllTagStop() {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG_STOP);
			db.execSQL(CREATE_TABLE_TAGSTOP);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
