package com.upt.cti.myapplication;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int REQ_PERMISSION = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        final LatLng faculty  = new LatLng(45.7450248,21.2275766);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(faculty));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(18));

        if(checkPermission()) {
            mMap.setMyLocationEnabled(true);
        } else {
            askPermission();
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getPosition().equals(faculty)) {
                    Toast.makeText(MapsActivity.this,"I'm sutdying",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        switch (requestCode) {
            case REQ_PERMISSION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
                if(checkPermission()) {
                    mMap.setMyLocationEnabled(true);
                }
        }
    }

    public void drawPolyLineOnMap(List<LatLng> list, GoogleMap googleMap) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.GREEN);
        polylineOptions.width(8);
        polylineOptions.addAll(list);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(LatLng latLng: list) {
            builder.include(latLng);
        }

        builder.build();
    }


    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == REQ_PERMISSION);
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQ_PERMISSION);
    }

}
