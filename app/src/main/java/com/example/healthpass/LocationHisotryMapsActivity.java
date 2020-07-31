package com.example.healthpass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationHisotryMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_hisotry_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(LocationHisotryMapsActivity.this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Intent intent = this.getIntent();
        double mlattitude = intent.getDoubleExtra("latitude",0);
        double mlongitude = intent.getDoubleExtra("longitude", 0);
        String marker = intent.getStringExtra("marker");

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mlattitude,mlongitude)) // My position
                .zoom(14)           // Zoom Level
                .bearing(0)         // camera position, (0 north , 180 south )
                .tilt(90)           // Inclinaison de la camera
                .build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        LatLng position = new LatLng(mlattitude,mlongitude);
        googleMap.addMarker(new MarkerOptions().position(position).title(marker));
    }
}