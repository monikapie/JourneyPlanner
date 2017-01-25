package com.example.pietyszukm.journeyplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pietyszukm on 23.01.2017.
 */

public class JourneyDataSource {
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHandler databaseHandler;
    private String[] allColumns = { DatabaseHandler.COLUMN_ID,
            DatabaseHandler.COLUMN_NAME,
            DatabaseHandler.COLUMN_DESC,
            DatabaseHandler.COLUMN_COST,
            DatabaseHandler.COLUMN_COUNTRY,
            DatabaseHandler.COLUMN_URI,
            DatabaseHandler.COLUMN_IMAGE};

    public JourneyDataSource(Context context) {
        this.context = context;
        databaseHandler = new DatabaseHandler(context);
    }

    public void open() throws SQLException {
        database = databaseHandler.getWritableDatabase();
    }

    public void close() {
        databaseHandler.close();
    }

    public Journey addJourney(Journey journey) {
        ContentValues values = new ContentValues();
        Journey journeyToDatabase = journey;
        values.put(DatabaseHandler.COLUMN_NAME, journeyToDatabase.getName());
        values.put(DatabaseHandler.COLUMN_DESC, journeyToDatabase.getDescription());
        values.put(DatabaseHandler.COLUMN_COST, journeyToDatabase.getCost());
        values.put(DatabaseHandler.COLUMN_COUNTRY, journeyToDatabase.getCountry());
        values.put(DatabaseHandler.COLUMN_URI, journeyToDatabase.getUri());
        values.put(DatabaseHandler.COLUMN_IMAGE, journeyToDatabase.getThumbnail());

        long insertId = database.insert(DatabaseHandler.TABLE_JOURNEYS, null,
                values);
        Cursor cursor = database.query(DatabaseHandler.TABLE_JOURNEYS,
                allColumns, DatabaseHandler.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Journey newJourney = cursorToJourney(cursor);
        cursor.close();
        return newJourney;
    }

    public void updateJourney(int journeyId, Journey journey) {
        System.out.println("Journey updated with id: " + journeyId);
        ContentValues values = new ContentValues();
        Journey journeyToDatabase = journey;
        values.put(DatabaseHandler.COLUMN_NAME, journeyToDatabase.getName());
        values.put(DatabaseHandler.COLUMN_DESC, journeyToDatabase.getDescription());
        values.put(DatabaseHandler.COLUMN_COST, journeyToDatabase.getCost());
        values.put(DatabaseHandler.COLUMN_COUNTRY, journeyToDatabase.getCountry());
        values.put(DatabaseHandler.COLUMN_URI, journeyToDatabase.getUri());
        values.put(DatabaseHandler.COLUMN_IMAGE, journeyToDatabase.getThumbnail());
        database.update(DatabaseHandler.TABLE_JOURNEYS, values, DatabaseHandler.COLUMN_ID
                + " = " + journeyId, null);
    }

    public void deleteJourney(int journeyId) {
        System.out.println("Journey deleted with id: " + journeyId);
        database.delete(DatabaseHandler.TABLE_JOURNEYS, DatabaseHandler.COLUMN_ID
                + " = " + journeyId, null);
    }

    public Journey getJourney(int id) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();

        Cursor cursor = db.query(databaseHandler.TABLE_JOURNEYS, allColumns, databaseHandler.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Journey journey = new Journey(cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getBlob(0));

        return journey;
    }

    public List<Journey> getAllJourneys() {
        List<Journey> journeys = new ArrayList<>();
        SQLiteDatabase db = databaseHandler.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHandler.TABLE_JOURNEYS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Journey comment = cursorToJourney(cursor);
            journeys.add(comment);
            cursor.moveToNext();
        }
        cursor.close();
        return journeys;
    }

    private Journey cursorToJourney(Cursor cursor) {
        Journey journey = new Journey();
        journey.setId(cursor.getInt(0));
        journey.setName(cursor.getString(1));
        journey.setDescription(cursor.getString(2));
        journey.setCost(cursor.getInt(3));
        journey.setCountry(cursor.getString(4));
        journey.setUri(cursor.getString(5));
        journey.setThumbnail(cursor.getBlob(6));

        return journey;
    }
}
