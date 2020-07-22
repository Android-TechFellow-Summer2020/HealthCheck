package com.example.healthpass.models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Business {
    private String name;
    private String address;
    private LatLng latLng;

    public static Business fromJson(JSONObject jsonObject) {
        Business b = new Business();
        // Deserialize json into object fields
        try {

            b.name = jsonObject.getString("name");
            b.address = jsonObject.getString("vicinity");

            b.latLng = new LatLng(jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return b;
    }

    public static ArrayList<Business> fromJson(JSONArray jsonArray)
    {
        JSONObject businessJson;
        ArrayList<Business> businesses = new ArrayList<Business>(jsonArray.length());

        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++)
        {
            try {
                businessJson = jsonArray.getJSONObject(i);

                Business business = fromJson(businessJson);
                if (business != null) {
                    businesses.add(business);
                }

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

        return businesses;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
