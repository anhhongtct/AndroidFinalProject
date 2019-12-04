package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDataBaseOpenHelperRecipe extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "recipes";
    public static final int VERSION_NUM =2;
    public static final String TABLE_NAME = "Recipes";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_URL = "url";
    //image
    public static final String COL_IMAGE = "picture";

    public MyDataBaseOpenHelperRecipe(Activity ctx) {
        super(ctx,DATABASE_NAME,null,VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE +" TEXT,"
                +COL_URL+" TEXT,"+ COL_IMAGE +" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}

