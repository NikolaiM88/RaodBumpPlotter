package com.example.nikolai.roadbumpplotter;


import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
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
    public LatLng latLng;
    private float threshold = 5;
    private float current;
    private float latest;
    private ArrayList<Reading> readingsList = new ArrayList<Reading>();
    MapsActivity mapsActivity1;

    public Sensor(MapsActivity mapsActivity)
    {
        mapsActivity1 = mapsActivity;
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

    public LatLng getLatLng() {
        return latLng;
    }


    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
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
                    mapsActivity1.newPlot(new LatLng(5.123123, 8.123213));
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
}
