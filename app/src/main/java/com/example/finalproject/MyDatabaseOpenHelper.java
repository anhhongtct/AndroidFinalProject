package com.example.finalproject;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CurrencyDBFile";
    public static final int VERSION_NUM = 2;
    public static final String TABLE_NAME = "Currency";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "NAME";
    public static final String COL_ISFROM = "ISFROM";

    public MyDatabaseOpenHelper(Activity ctx){
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Make sure you put spaces between SQL statements and Java strings:
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NAME + " TEXT, " + COL_ISFROM + " BOOLEAN)");  ////////

        //add default record
        ContentValues newRowValues = new ContentValues();
        //put string name in the COL_NAME column:
        newRowValues.put(MyDatabaseOpenHelper.COL_NAME, "CAD");
        //put string email in the COL_ISFROM column:
        newRowValues.put(MyDatabaseOpenHelper.COL_ISFROM, true);
        //insert in the database:
        db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }
}
