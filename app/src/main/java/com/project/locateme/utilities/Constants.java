package com.project.locateme.utilities;

/**
 * @author Andrew
 * @since 26/1/2017
 * @version 1.0
 *
 * this class should carry all the constants within the application
 */

public class Constants {
    //Fragment Names
    public static final String ALL_FRIENDS_FRAGMENT = "allFriends"; //used for all friends activity
    public static final String EVENT_FRAGMENT = "eventFragment";//used for the event fragment
    public static final String USER_LOCATION_FRAGMENT = "locationFragment";
    public static final String SELECT_ZONE_FRAGMENT = "selectZoneFragment";
    public static final String VIEW_ZONE_FRAGMENT = "viewZoneFragment";
    public static final String CREATE_EVENT_FRAGMENT = "createEventFragment";
    public static final String HASHMAP = "hashmap"; //used for getting the hashmap from sender to receiver activity
    public static final String Event_CHAT_FRAGMENT = "eventChatFragment";
    public static final String PROFILE_FRAGMENT = "profileFragment";

    //URLs
    public static final String UPDATE_LOCATION_URL = ""; //// TODO: 1/28/2017 this from backend
    public static final String GET_ALL_FRIENDS = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/getfriends";
    public static final String GET_CLOSEST_FRIENDS = "";//// TODO: 1/29/2017 same
    public static final String GET_UPCOMING_EVENTS = ""; //// TODO: 1/29/2017 same
    public static final String CREATE_EVENT = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/createevent";
    public static final String GET_OWNERS_EVENT = " http://firstapp-fciswpro.rhcloud.com/restapi/rest/getownersevent";
    public static final String GET_EVENT_USERS = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/geteventusers";
    public static final String GET_USERS_ON_MAP_REGION = ""; //// TODO: 23/02/17 update this
    public static final String GET_ZONES = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/getuserareas";
    public static final String GET_ALL_EVENTS = ""; //// TODO: 28/02/17 fill this
    public static final String GET_ZONE_FRIENDS_CURRENT_LOCATION = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/getareauserswithlocation";
    public static final String ADD_FRIEND = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/addfriend";
    public static final String REMOVE_FRIEND = "";
    public static final String ACCEPT_REQUEST = "";
    public static final String IGNORE_REQUEST = "";
}
