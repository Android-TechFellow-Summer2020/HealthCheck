package com.example.healthpass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.healthpass.models.Business;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.ResourceBundle;

import okhttp3.Headers;

import static com.google.android.gms.location.LocationServices.API;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static java.lang.String.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_FINE_LOCATION = 200; //??
    private GoogleMap mMap;
    public static final String TAG = "MapsActivity";


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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(checkPermissions()) {
            googleMap.setMyLocationEnabled(true);
        }

        getLastLocation();

    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION);
    }

    private void xxx() {
        // Add a marker to current location and move the camera
        LatLng westfieldNJ = new LatLng(40.659, -74.3474);
        mMap.addMarker(new MarkerOptions().position(westfieldNJ).title("Westfield, New Jersey"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(westfieldNJ));

    }

    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Permissions Not Granted ", Toast.LENGTH_SHORT).show();
            // return;
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                            findNearbyHospitals(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined


        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Add a marker to current location and move the camera
        // LatLng westfieldNJ = new LatLng(40.659, -74.3474);
        mMap.addMarker(new MarkerOptions().position(latLng).title("You are here!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    public void findNearbyHospitals(Location location)
    {
        String API_KEY = getString(R.string.API_Key);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", API_KEY);
        params.put("location",location.getLatitude() + "," + location.getLongitude());
        params.put("type","hospital");
        //params.put("keyword","emergency");
        params.put("radius", 20000);

        client.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JsonHttpResponseHandler.JSON json) {
                // Access a JSON object response with `json.jsonObject`
                Log.i("doRequest", json.jsonObject.toString());
                try {
                    Log.i("doRequest array", json.jsonObject.getJSONArray("results").toString());

                    JSONArray jsonArray = json.jsonObject.getJSONArray("results");
                    ArrayList<Business> businesses = Business.fromJson(jsonArray);

                    for (int i=0; i<businesses.size(); i++)
                    {
                        Business business = businesses.get(i);
                        Log.i(TAG, business.getName());

                        mMap.addMarker(new MarkerOptions().position(business.getLatLng()).title(business.getName()).snippet(business.getAddress()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(business.getLatLng(), 10));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.i("doRequest", "doRequest failed");

            }
                }
        );
    }
}