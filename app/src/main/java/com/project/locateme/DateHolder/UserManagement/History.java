package com.project.locateme.dateHolder.userManagement;

import com.project.locateme.dateHolder.locationManager.Location;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * @author Andrew
 * @since 26/1/2017
 * @version 1.0
 */

public class History {
    private HashMap<Timestamp, Location> positions;

    public HashMap<Timestamp, Location> getPositions() {
        return positions;
    }

    public void setPositions(HashMap<Timestamp, Location> positions) {
        this.positions = positions;
    }
}
