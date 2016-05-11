package com.example.nikolai.roadbumpplotter;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Nikolai on 04-05-2016.
 */
public class Sensor extends FragmentActivity implements SensorEventListener {

    public SensorManager sensorManager;
    private float threshold = 5;
    private float current;
    private float latest;
    private ArrayList<Reading> readingsList = new ArrayList<Reading>();
    Location myCurrentLocation;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Context localContext;

    public Sensor(Context context)
    {
        localContext = context;
        dbHelper = new DatabaseHelper(localContext);


    }

    public void sensorListener(){
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == android.hardware.Sensor.TYPE_ACCELEROMETER) {
            current = event.values[1];
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }


    public float sumOfReadings()
    {
        float sum=0;
        for(int i = 0; i < readingsList.size() ; i++)
        {
            sum += readingsList.get(i).getReading();
        }
        return sum;
    }


    Thread magThread = new Thread(new Runnable() {
        public void run() {
            boolean running = true;
            while(running) {
                if (current != 0) {
                    if (readingsList.size() == 20) {
                        readingsList.remove(0);
                        Reading read = new Reading(latest - current);
                        latest = current;
                        readingsList.add(read);
                    } else {
                        Reading read = new Reading(latest - current);
                        latest = current;
                        readingsList.add(read);
                    }
                }
                try {
                    magThread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (sumOfReadings() > threshold)
                {
                    db  = dbHelper.getWritableDatabase();
                    ContentValues cv=new ContentValues();
                    cv.put("NAME", "Bump");
                    cv.put("LATITUDE", myCurrentLocation.getLatitude());
                    cv.put("LONGTITUDE", myCurrentLocation.getLongitude());
                    db.insert("PLOTS", null, cv);
                    db.close();
                }
            }
        }
    });



    private class Reading {
        private float reading;
        private String time;

        public Reading(float current) {
            reading = current;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public float getReading() {

            return reading;
        }

        public void setReading(float reading) {
            this.reading = reading;
        }
    }

    public void setLocaion (Location location)
    {
        myCurrentLocation = location;
    }
}
