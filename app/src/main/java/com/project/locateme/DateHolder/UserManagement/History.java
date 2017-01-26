package com.project.locateme.DateHolder.UserManagement;

import com.project.locateme.DateHolder.LocationManager.Location;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by Andrew on 1/26/2017.
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
