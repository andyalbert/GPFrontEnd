package com.project.locateme.DateHolder;

import android.provider.ContactsContract;

import java.util.Date;

/**
 * Created by khaled on 26/01/17.
 */

public class Location {
    private String longitude;
    private String latitude ;
    private Date date;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
