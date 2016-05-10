package com.example.nikolai.roadbumpplotter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikolai on 09-05-2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "MapPlots"; // the name of our database
    private static final int DB_VERSION = 2; // the version of the database

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE PLOTS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "LATITUDE DOUBLE, "
                    + "LONGTITUDE DOUBLE);");
        }
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE DRINK ADD COLUMN FAVORITE NUMERIC;");
        }
    }

    private static void insertPlot(SQLiteDatabase db, String name,
                                    double latitude, double longtitude) {
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("NAME", name);
        drinkValues.put("LATITUDE", latitude);
        drinkValues.put("LONGTITUDE", longtitude);
        db.insert("PLOTS", null, drinkValues);
    }

}
