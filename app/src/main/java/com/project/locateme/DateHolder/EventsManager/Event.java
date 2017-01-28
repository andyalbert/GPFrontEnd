package com.project.locateme.dateHolder.eventsManager;

import com.project.locateme.dateHolder.locationManager.Location;
import com.project.locateme.dateHolder.userManagement.Profile;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @author Andrew
 * @since 25/1/2017
 * @version 1.0
 */

public class Event {
    private String name;
    private String description;
    private String imageURL;
    private Location location;
    private double radius;
    private ArrayList<Profile> users;
    private Timestamp dateOfEvent;
    private Timestamp deadline; //what is this ??
    private boolean state;

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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public ArrayList<Profile> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Profile> users) {
        this.users = users;
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
}
