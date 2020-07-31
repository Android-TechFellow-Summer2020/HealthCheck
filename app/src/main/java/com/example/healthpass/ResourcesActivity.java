package com.example.healthpass;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.asynchttpclient.callback.TextHttpResponseHandler;
import com.example.healthpass.adapter.HospitalsAdapter;
import com.example.healthpass.models.hospital;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class ResourcesActivity extends AppCompatActivity {

    public static final String TAG = "ResourcesActivity";
    private static final int REQUEST_FINE_LOCATION = 200;

    public String Link1 = "https://services7.arcgis.com/LXCny1HyhQCUSueu/arcgis/rest/services/Definitive_Healthcare_USA_Hospital_Beds/FeatureServer/0/query?where=COUNTY_NAME%20%3D%20'";
    public String Link2 = "'&outFields=*&outSR=4326&f=json";
    List<hospital> hospitals;
    RecyclerView rvHospitalList;
    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        btnHome = findViewById(R.id.btnHome);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResourcesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        saveLocation();

    }

    private void displayRecyclerView(double lat, double lon)
    {
        hospitals = new ArrayList<>();
        rvHospitalList = findViewById(R.id.rvHospitalList);
        final HospitalsAdapter hospitalsAdapter = new HospitalsAdapter(this, hospitals);
        rvHospitalList.setAdapter(hospitalsAdapter);
        rvHospitalList.setLayoutManager(new LinearLayoutManager(this));

        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        Log.d(TAG, "Latitude: "+ lat);
        Log.d(TAG, "Longitude: "+ lon);
        params.put("lat", (long) lat;
        params.put("lon", (long) lon);
        client.get("https://geo.fcc.gov/api/census/area", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                //Log.d("DEBUG ARRAY", json.jsonArray.toString());
                // Access a JSON object response with `json.jsonObject`
                try {
                    Log.d("DEBUG OBJECT", json.jsonObject.get("results").toString());
                    JSONArray jsonArray = (JSONArray) json.jsonObject.get("results");

                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                    Log.d("County Name: ", jsonObject.get("county_name").toString());
                    String county_name = jsonObject.get("county_name").toString();
                    county_name = county_name.toUpperCase();
                    if(county_name.contains(" "))
                    {
                        county_name = county_name.replaceAll(" ", "%20");
                    }

                    Link1 = Link1 + county_name + Link2;
                    Log.d("URL", Link1);

                    client.get(Link1, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            try {
                                Log.d("JSONArray", json.jsonObject.getJSONArray("features").toString());

                                JSONArray jsonArray1= json.jsonObject.getJSONArray("features");

                                for(int i = 0; i<jsonArray1.length();i++) {
                                    JSONObject info = (JSONObject) jsonArray1.get(i);
                                    JSONObject attributes = (JSONObject) info.get("attributes");
                                    JSONObject geometry = (JSONObject) info.get("geometry");
                                    hospital h = new hospital(attributes);
                                    h.setLat((Double) geometry.get("y"));
                                    h.setLon((Double) geometry.get("x"));

                                    hospitals.add(h);

                                }
                                hospitalsAdapter.notifyDataSetChanged();
                                Log.d("hospitals: ", hospitals.toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("Error!", throwable.toString());
            }
        });
    }

    private void saveLocation() {

        if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION);
        }
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissions Not Granted ", Toast.LENGTH_SHORT).show();
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            displayRecyclerView(location.getLatitude(), location.getLongitude());
                            //Toast.makeText(getApplicationContext(), "Lat: " + location.getLatitude()+ " Lon: " + location.getLongitude() + "now: " + LocalDateTime.now().toString(), Toast.LENGTH_SHORT ).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }
}



/*public class ResourcesActivity extends AppCompatActivity {

    Button btnNearbyHospitals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        btnNearbyHospitals = findViewById(R.id.btnNearbyHospitals);

        btnNearbyHospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResourcesActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });
    }
}*/