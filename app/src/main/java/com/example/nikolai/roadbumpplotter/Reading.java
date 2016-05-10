package com.example.nikolai.roadbumpplotter;

/**
 * Created by Nikolai on 10-05-2016.
 */
public class Reading {
    private String name;
    private double latitude;
    private double longtitude;

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {

        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
