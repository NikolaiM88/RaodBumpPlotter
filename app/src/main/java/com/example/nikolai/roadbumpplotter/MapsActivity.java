package com.example.nikolai.roadbumpplotter;

import android.content.Context;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Location location;
    private LocationManager locationManager;
    Sensor sensor;
    Context localContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        sensor = new Sensor(localContext, locationManager);
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
        //Here is the bumps from the database supposed to be inserted, so the bumps is marked
        //when the see bumps button is pressed.

        //googleMap.addMarker(new MarkerOptions().position(new LatLng(55.3965, 10.3827)));
    }

    public void newPlot(LatLng latLng)
    {
        mMap.addMarker(new MarkerOptions().position(latLng));
    }

    @Override
    public void onLocationChanged(Location location) {
        if(sensor.latLng != null){
            mMap.addMarker(new MarkerOptions().position(sensor.getLatLng()));
        }
    }
}
