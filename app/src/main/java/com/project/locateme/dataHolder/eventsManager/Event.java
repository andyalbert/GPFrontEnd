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

public class Event  implements Serializable{
    int id; //// TODO: 08/03/17 this is private and string -_-
    private String name;
    private String description;
    private Area area;
    private Timestamp dateOfEvent;
    private Timestamp deadline; //what is this ??
    private boolean state;
    private Suggestion suggestion;
    private String imageURL;//// TODO: 08/03/17 remove this, it's in the area


    public Event(){
        imageURL = null;
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

    public boolean isState() {
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

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
