package com.project.locateme.dataHolder.locationManager;

/**
 * @author Andrew
 * @version 1.0
 * @since 25/1/2017
 */

public class Location {
    private double longitude;
    private double latitude;
    private String name;

    public Location(double longitude, double latitude, String name){
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
