package com.project.locateme.dataHolder.eventsManager;

import com.project.locateme.dataHolder.locationManager.Area;

import java.util.Date;

/**
 * @author andrew
 * @since 9/2/2017
 * @version 1.0
 */

public class Suggestion {
    private Date date;
    private Area area;
    private boolean state;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
