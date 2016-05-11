package com.example.nikolai.roadbumpplotter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap threadMap;
    private Location location;
    private LocationManager locationManager;
    Sensor sensor;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dbHelper = new DatabaseHelper(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        sensor = new Sensor(this, locationManager);
        sensor.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor.sensorListener();
        sensor.magThread.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensor.sensorManager.unregisterListener(sensor);
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensor.sensorListener();
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        threadMap = googleMap;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                db = dbHelper.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT LATITUDE, LONGTITUDE FROM PLOTS", null);

                if (c.moveToFirst()) {
//            while ( !c.isAfterLast() ) {
                    threadMap.addMarker(new MarkerOptions().position(new LatLng(c.getDouble(0), c.getDouble(1))));
//            }
                }
                c.close();
                db.close();
            }
        });
        thread.start();
    }

    public void newPlot(LatLng latLng)
    {
        threadMap.addMarker(new MarkerOptions().position(latLng));
    }

    @Override
    public void onLocationChanged(Location location) {
    }
}
