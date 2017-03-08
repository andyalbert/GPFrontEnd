package com.project.locateme.models.android.specific;

import com.project.locateme.dataHolder.locationManager.Location;

import java.sql.Timestamp;

/**
 * @author andrew
 * @since 8/2/2017
 * @version 1.0
 *
 * this class acts as a model for the users userId and their current locations
 */

public class UserLocation {
    private int userId;
    private Location location;
    private Timestamp lastLocationUpdate;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Timestamp getLastLocationUpdate() {
        return lastLocationUpdate;
    }

    public void setLastLocationUpdate(Timestamp lastLocationUpdate) {
        this.lastLocationUpdate = lastLocationUpdate;
    }
}
