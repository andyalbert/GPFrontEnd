package com.project.locateme.DateHolder;

import android.media.Image;

import java.io.Serializable;

/**
 * Created by khaled on 26/01/17.
 */

public class Event implements Serializable {
    private String name;
    private Location location;
    private String description;
    private Image image;
    private Double radius;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }


}
