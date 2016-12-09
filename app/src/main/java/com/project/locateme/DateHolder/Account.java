package com.project.locateme.DateHolder;

import java.io.Serializable;

/**
 * @author Andrew Albert
 * @version 1.0
 * @since 12/9/2016
 */

public class Account implements Serializable {
    private String password;
    private String id;
    private String type;
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
