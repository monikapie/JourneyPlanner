package com.example.pietyszukm.journeyplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by pietyszukm on 23.01.2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String TABLE_JOURNEYS = "journeys";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESC= "script";
    public static final String COLUMN_COST = "cost";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_URI = "uri";
    public static final String COLUMN_IMAGE = "image";



    private static final String DATABASE_NAME = "journeys.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_JOURNEYS + "( " + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME
            + " TEXT NOT NULL, " + COLUMN_DESC
            + " TEXT NOT NULL, " + COLUMN_COST
            + " INTEGER NOT NULL, " + COLUMN_COUNTRY
            + " TEXT NOT NULL, " + COLUMN_URI
            + " INTEGER NOT NULL, " + COLUMN_IMAGE
            + " BLOB); ";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHandler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNEYS);
        onCreate(db);
    }
}
