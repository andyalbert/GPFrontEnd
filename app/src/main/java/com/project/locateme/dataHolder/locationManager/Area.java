package com.project.locateme.dataHolder.locationManager;

import com.project.locateme.dataHolder.userManagement.Profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author andrew
 * @version 1.0
 * @since 9/2/2017
 */

public class Area implements Serializable {
    private double radius;
    //private HashMap<Profile, Boolean> accounts;
    private Location location;
    private String imageURL;
    private String id;
    private ArrayList<Profile> accounts;

    public ArrayList<Profile> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Profile> accounts) {
        this.accounts = accounts;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

//    public HashMap<Profile, Boolean> getAccounts() {
//        return accounts;
//    }
//
//    public void setAccounts(HashMap<Profile, Boolean> accounts) {
//        this.accounts = accounts;
//    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


}
