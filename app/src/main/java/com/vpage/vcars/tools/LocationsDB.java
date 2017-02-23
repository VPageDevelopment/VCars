package com.vpage.vcars.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vpage.vcars.pojos.VLocationTrack;
import com.vpage.vcars.tools.utils.LogFlag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class LocationsDB extends SQLiteOpenHelper{

	private static final String TAG = LocationsDB.class.getName();

	public static String DB_PATH = "/data/data/com.vpage.vcars/databases/VcarLocationMarkerLite.sqlite";
	
	/** Database name */
	public static String DB_NAME = "VcarLocationMarkerLite.sqlite";

	//Table name of DB
	public static String TB_NAME = "tblVcarTrack";
	
	/** Field 1 of the table locations, which is the primary key */
	public static final String FIELD_ROW_ID = "id";
	
	/** Field 2 of the table locations, stores the latitude */
    public static final String FIELD_LAT = "lat";
    
    /** Field 3 of the table locations, stores the longitude*/
    public static final String FIELD_LNG = "lng";

	/** Field 4 of the table locations, stores the zoom level of map*/
	public static final String FIELD_TIME = "time";

	/** Field 4 of the table locations, stores the zoom level of map*/
	public static final String FIELD_LOCATION = "location";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    /** An instance variable for SQLiteDatabase */
    private SQLiteDatabase myDataBase;

	private final Context context;

	public LocationsDB(Context context) {

		super(context,DB_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

    @Override
    public void onCreate(SQLiteDatabase db) {

        myDataBase = db;
        String sql = 	"create table " + TB_NAME + " ( " +
                FIELD_ROW_ID + " integer primary key autoincrement , " +
                FIELD_LNG + " double , " +
                FIELD_LAT + " double , " +
                FIELD_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP , " +
                FIELD_LOCATION + " text " +
                " ) ";

        myDataBase.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        myDataBase = db;
    }

    public boolean createDataBase() {
        boolean isExist = checkDBExists();
        if (isExist) {
            if (LogFlag.bLogOn) Log.d(TAG + "exist", "Exist");
            myDataBase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
            onUpgrade(myDataBase, myDataBase.getVersion(), DATABASE_VERSION);
            close();
        } else {
            this.getReadableDatabase();
            copyDataBase();
        }

        return isExist;
    }

    private boolean checkDBExists() {
        try {
            File file = new File(DB_PATH);
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
           if (LogFlag.bLogOn) Log.e(TAG, e.toString());
            return false;
        }

        return false;
    }


	private void copyDataBase() {

		try {
			//Open your local db as the input stream
			InputStream myInput = context.getAssets().open(DB_NAME);

			// Path to the just created empty db
			String outFileName = DB_PATH;

			//Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);

			//transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			//Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (Exception e) {
			if (LogFlag.bLogOn) Log.e(TAG, e.toString());
		}
	}

    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        SQLiteDatabase.releaseMemory();
        super.close();
    }


	public List<VLocationTrack> openDataBaseData(VLocationTrack locationTrack) throws SQLException {
        List<VLocationTrack> vLocationTrackList = new ArrayList<>();
        try {

            myDataBase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);

            // Creating an instance of ContentValues
            ContentValues contentValues = new ContentValues();

            // Setting latitude in ContentValues
            contentValues.put(LocationsDB.FIELD_LAT, locationTrack.getLatitude() );

            // Setting longitude in ContentValues
            contentValues.put(LocationsDB.FIELD_LNG, locationTrack.getLongitude());

            // Setting longitude in ContentValues
            contentValues.put(LocationsDB.FIELD_TIME, locationTrack.getDate());

            // Setting longitude in ContentValues
            contentValues.put(LocationsDB.FIELD_LOCATION, locationTrack.getLocation());

            myDataBase.insert(TB_NAME, null, contentValues);

            int numRows = (int) DatabaseUtils.longForQuery(myDataBase, "select count(*) from " + TB_NAME, null);
            if (LogFlag.bLogOn)Log.d(TAG , "numRows: "+numRows);

            String selectQuery = "SELECT  * FROM " + TB_NAME;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            VLocationTrack vLocationTrack = new VLocationTrack();

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        double lng = cursor.getDouble(cursor.getColumnIndex(FIELD_LNG));
                        double lat = cursor.getDouble(cursor.getColumnIndex(FIELD_LAT));
                        String dateTime = cursor.getString(cursor.getColumnIndex(FIELD_TIME));
                        String location = cursor.getString(cursor.getColumnIndex(FIELD_LOCATION));

                        vLocationTrack.setLongitude(lng);
                        vLocationTrack.setLatitude(lat);
                        vLocationTrack.setDate(dateTime);
                        vLocationTrack.setLocation(location);
                        vLocationTrackList.add(vLocationTrack);

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            myDataBase.close(); // Closing database connection

        }catch (SQLiteCantOpenDatabaseException e){
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }
        if (LogFlag.bLogOn) Log.d(TAG, "vLocationTrackList: "+vLocationTrackList.toString());

		return vLocationTrackList;
	}


}
