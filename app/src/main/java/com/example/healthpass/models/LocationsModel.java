package com.example.healthpass.models;

public class LocationsModel {
    private double lat;
    private double lon;
    private String datetime;
    private String id;

    public LocationsModel() {
    }

    public LocationsModel(float lat, float lon, String dateTime, String id) {
        this.lat = lat;
        this.lon = lon;
        this.datetime = dateTime;
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getDatetime() {
        return datetime;
    }


    public String getId()
    {
        return id;
    }

    @Override
    public String toString() {
        return "LocationsModel{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", dateTime='" + datetime + '\'' +
                '}';
    }
}
