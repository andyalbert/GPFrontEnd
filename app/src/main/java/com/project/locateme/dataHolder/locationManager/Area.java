package com.project.locateme.dataHolder.locationManager;

import com.project.locateme.dataHolder.userManagement.Profile;

import java.util.HashMap;

/**
 * @author andrew
 * @version 1.0
 * @since 9/2/2017
 */

public class Area {
    private double radius;
    private HashMap<Profile, Boolean> accounts;
    private Location location;

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public HashMap<Profile, Boolean> getAccounts() {
        return accounts;
    }

    public void setAccounts(HashMap<Profile, Boolean> accounts) {
        this.accounts = accounts;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


}
