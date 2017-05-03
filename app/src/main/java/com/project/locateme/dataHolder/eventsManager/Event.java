package com.project.locateme.dataHolder.eventsManager;

import com.project.locateme.dataHolder.locationManager.Area;
import com.project.locateme.dataHolder.locationManager.Location;
import com.project.locateme.dataHolder.userManagement.Profile;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @author Andrew
 * @since 25/1/2017
 * @version 1.2
 */

public class Event  implements Serializable {
    private String name;
    private String description;
    private Area area;
    private Timestamp dateOfEvent;
    private Timestamp deadline; //what is this ??
    private boolean state;
    private Suggestion suggestion;
    private String id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Timestamp getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(Timestamp dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

}
