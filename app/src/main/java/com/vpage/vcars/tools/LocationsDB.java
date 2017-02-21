package com.vpage.vcars.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vpage.vcars.pojos.VLocationTrack;
import com.vpage.vcars.tools.utils.LogFlag;

import java.util.ArrayList;
import java.util.List;


public class LocationsDB extends SQLiteOpenHelper{

	private static final String TAG = LocationsDB.class.getName();
	
	/** Database name */
	private static String DBNAME = "locationmarkersqlite";
	
	/** Version number of the database */
	private static int VERSION = 1;
	
	/** Field 1 of the table locations, which is the primary key */
	public static final String FIELD_ROW_ID = "_id";
	
	/** Field 2 of the table locations, stores the latitude */
    public static final String FIELD_LAT = "lat";
    
    /** Field 3 of the table locations, stores the longitude*/
    public static final String FIELD_LNG = "lng";

	/** Field 4 of the table locations, stores the zoom level of map*/
	public static final String FIELD_TIME = "time";

	/** Field 4 of the table locations, stores the zoom level of map*/
	public static final String FIELD_LOCATION = "location";
    
    /** Field 4 of the table locations, stores the zoom level of map*/
    public static final String FIELD_ZOOM = "zom";
    
    /** A constant, stores the the table name */
    private static final String DATABASE_TABLE = "locations";
    
    /** An instance variable for SQLiteDatabase */
    private SQLiteDatabase mDB;  
    

    /** Constructor */
	public LocationsDB(Context context) {
		super(context, DBNAME, null, VERSION);	
		this.mDB = getWritableDatabase();
	}
	

	/** This is a callback method, invoked when the method getReadableDatabase() / getWritableDatabase() is called 
	  * provided the database does not exists 
	* */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = 	"create table " + DATABASE_TABLE + " ( " +
							FIELD_ROW_ID + " integer primary key autoincrement , " +
							FIELD_LNG + " double , " +
							FIELD_LAT + " double , " +
				            FIELD_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP , " +
				            FIELD_LOCATION + " text , " +
							FIELD_ZOOM + " text " +
						" ) ";
                				
		db.execSQL(sql);
	}
	
	/** Inserts a new location to the table locations */
	public long insert(ContentValues contentValues){
		long rowID = mDB.insert(DATABASE_TABLE, null, contentValues);
		return rowID;
		
	}


	/** Deletes all locations from the table */
	public List<VLocationTrack> select(){
		Cursor c = mDB.rawQuery("SELECT * FROM "+DATABASE_TABLE, null);
		if (LogFlag.bLogOn) Log.d(TAG, "getCount: "+c.getCount());
		List<VLocationTrack> vLocationTrackList = new ArrayList<>();
		VLocationTrack vLocationTrack = new VLocationTrack();
		if(c.moveToFirst()){
			do{

				double lng = c.getDouble(c.getColumnIndex(FIELD_LNG));
				double lat = c.getDouble(c.getColumnIndex(FIELD_LAT));
				String dateTime = c.getString(c.getColumnIndex(FIELD_TIME));
				String location = c.getString(c.getColumnIndex(FIELD_LOCATION));

				vLocationTrack.setLongitude(lng);
				vLocationTrack.setLatitude(lat);
				vLocationTrack.setDateTime(dateTime);
				vLocationTrack.setLocation(location);
				vLocationTrackList.add(vLocationTrack);

			}while(c.moveToNext());
		}
		c.close();
		mDB.close();
		return vLocationTrackList;
	}



	/** Deletes all locations from the table */
	public int del(){
		int cnt = mDB.delete(DATABASE_TABLE, null , null);		
		return cnt;
	}
	
	/** Returns all the locations from the table */
	public Cursor getAllLocations(){
        return mDB.query(DATABASE_TABLE, new String[] { FIELD_ROW_ID,  FIELD_LAT , FIELD_LNG, FIELD_TIME , FIELD_LOCATION, FIELD_ZOOM } , null, null, null, null, null,null);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
