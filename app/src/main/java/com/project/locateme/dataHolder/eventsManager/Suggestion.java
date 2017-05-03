package com.project.locateme.dataHolder.eventsManager;

import com.project.locateme.dataHolder.locationManager.Area;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author andrew
 * @since 9/2/2017
 * @version 1.0
 */

public class Suggestion {

    private String id;
    private Timestamp date;
    private boolean state;
    private String userId ;
    private String userName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
