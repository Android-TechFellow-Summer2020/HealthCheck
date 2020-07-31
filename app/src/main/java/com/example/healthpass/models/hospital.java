package com.example.healthpass.models;


import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class hospital {
    String name;
    String address;
    String city;
    String state;
    String countyName;
    String zipCode;
    String licensedBeds;
    String ICUBeds;
    double bedUtilization;
    String averageVentilatorUsage;

    hospital()
    {

    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    double lat;

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    double lon;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getLicensedBeds() {
        return licensedBeds;
    }

    public String getICUBeds() {
        return ICUBeds;
    }

    public double getBedUtilization() {
        return bedUtilization;
    }

    public String getAverageVentilatorUsage() {
        return averageVentilatorUsage;
    }

    public hospital(JSONObject jsonObject) throws JSONException
    {
        name = jsonObject.getString("HOSPITAL_NAME");
        address = jsonObject.getString("HQ_ADDRESS");
        city = jsonObject.getString("HQ_CITY");
        countyName = jsonObject.getString("COUNTY_NAME");
        state = jsonObject.getString("HQ_STATE");
        zipCode = jsonObject.getString("HQ_ZIP_CODE");
        licensedBeds = jsonObject.getString("NUM_LICENSED_BEDS");
        ICUBeds = jsonObject.getString("NUM_ICU_BEDS");
        bedUtilization = jsonObject.getDouble("BED_UTILIZATION");
        averageVentilatorUsage = jsonObject.getString("AVG_VENTILATOR_USAGE");

    }
}
