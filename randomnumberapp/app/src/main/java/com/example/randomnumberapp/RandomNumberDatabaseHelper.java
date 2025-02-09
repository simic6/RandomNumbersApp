package com.example.randomnumberapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RandomNumberDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "random_numbers.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "random_numbers";
    public static final String COLUMN_ID = "id"; // ID column to track order
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_TIMESTAMP = "timestamp"; // Timestamp for original order

    // Create table query
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NUMBER + " INTEGER, " +
                    COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);"; // Timestamp

    public RandomNumberDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to clear the history
    public void clearHistory(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
