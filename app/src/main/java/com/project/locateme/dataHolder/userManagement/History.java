package com.project.locateme.dataHolder.userManagement;

import com.project.locateme.dataHolder.locationManager.Location;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * @author Andrew
 * @since 26/1/2017
 * @version 1.1
 */

public class History {
    private Location location;
    private Timestamp timestamp;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
