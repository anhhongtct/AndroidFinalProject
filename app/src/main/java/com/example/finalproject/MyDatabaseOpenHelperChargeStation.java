package com.example.finalproject;


import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author: Tran Thi Anh Hong
 * @version 01
 * @date 12/2019
 * This is the Database helper class for app Charging Station
 */

public class MyDatabaseOpenHelperChargeStation extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDatabaseFile";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "ChargingStation";
    public static final String COL_ID = "_id";
    public static final String COL_LAT = "Latitude";
    public static final String COL_LONG = "Longitude";
    public static final String COL_NAME= "Name";
    public static final String COL_PHONE= "PhoneNo";


    public MyDatabaseOpenHelperChargeStation(Activity ctx){
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    public void onCreate(SQLiteDatabase db)
    {
        //Make sure you put spaces between SQL statements and Java strings:
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_LAT + " TEXT," + COL_LONG + " TEXT,"+ COL_PHONE + " TEXT," + COL_NAME + " TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    public void deleteRow(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " =? ", new String[] { id });
        db.close();
    }
}
