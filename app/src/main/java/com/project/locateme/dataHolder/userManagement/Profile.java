package com.project.locateme.dataHolder.userManagement;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Andrew Albert
 * @version 1.0
 * @since 12/9/2016
 */

public class Profile implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String homeTown;
    private String name;
    private String birthday;
    private String pictureURL;
    private History history;
    private FriendShipState state;
    private int userId;

    public enum FriendShipState{
        FRIEND, NOT_FRIEND, PENDING_REQUEST, ADD_REQUEST, NONE //used for self profile view
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

//    public String getBirthday(){
//        String value;
//        value = birthday.get(Calendar.DAY_OF_MONTH) + "/" + birthday.get(Calendar.MONTH) + "/" + birthday.get(Calendar.YEAR);
//        return value;
//    }

    /**
     * this a setter that uses a string date in the format day/month/year and parse it into the calender date
     * @param birthday
     */
    public void setBirthday(String birthday){
        if(birthday == null)
            return;
        this.birthday = birthday;
//        String[] dateInString = birthday.split("/");
//        Calendar calendar = new GregorianCalendar();
//        calendar.set(Calendar.YEAR, Integer.parseInt(dateInString[2]));
//        calendar.set(Calendar.MONTH, Integer.parseInt(dateInString[1]));
//        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateInString[0]));
//
//        this.birthday = calendar;
    }

//    public void setBirthday(Calendar birthday) {
//        this.birthday = birthday;
//    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public FriendShipState getState() {
        return state;
    }

    public void setState(FriendShipState state) {
        this.state = state;
    }
}
