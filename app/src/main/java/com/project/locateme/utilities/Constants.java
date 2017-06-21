package com.project.locateme.utilities;

import android.graphics.Color;

/**
 * @author Andrew
 * @since 26/1/2017
 * @version 10
 *
 * this class should carry all the constants within the application
 */

public class Constants {
    //Fragment Names
    public static final String ALL_FRIENDS_FRAGMENT = "allFriends"; //used for all friends activity
    public static final String ALL_EVENTS_FRAGMENT = "allEvents";
    public static final String EVENT_FRAGMENT = "eventFragment";//used for the event fragment
    public static final String USER_LOCATION_FRAGMENT = "locationFragment";
    public static final String SELECT_ZONE_FRAGMENT = "selectZoneFragment";
    public static final String VIEW_ZONE_FRAGMENT = "viewZoneFragment";
    public static final String CREATE_EVENT_FRAGMENT = "createEventFragment";
    public static final String HASHMAP = "hashmap"; //used for getting the hashmap from sender to receiver activity
    public static final String Event_CHAT_FRAGMENT = "eventChatFragment";
    public static final String PROFILE_FRAGMENT = "profileFragment";
    public static final String CREATE_PLACE_FRAGMENT = "createPlace";
    public static final String INVITE_FRIENDS_FRAGMENT = "inviteFriends";
    public static final String VIEW_PARTICIPANTS_LOCATION_FRAGMENT = "participantsFragment";
    public static final String VIEW_SUGGESTIONS = "viewSuggestions";
    public static final String SEARCH_FRAGMENT = "searchFragment";



    //URLs
    public static final String UPDATE_LOCATION_URL = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/updateuserlocation";
    public static final String GET_ALL_FRIENDS = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/getfriends";
    public static final String GET_UPCOMING_EVENTS = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/getnearbyevents";
    public static final String CREATE_EVENT = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/createevent";
    public static final String GET_OWNERS_EVENT = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/getownersevent";
    public static final String GET_EVENT_USERS = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/geteventusers";
    public static final String GET_USERS_ON_MAP_REGION = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/getfriendsonmap";
    public static final String GET_ZONES = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/getuserareas";
    public static final String GET_ALL_EVENTS = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/getuserevents";
    public static final String GET_ZONE_FRIENDS_CURRENT_LOCATION = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/getareauserswithlocation";
    public static final String CREATE_ZONE = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/createareawithusers";
    public static final String ADD_FRIEND = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/addfriend";
    public static final String REMOVE_FRIEND = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/removefriend";
    public static final String ACCEPT_REQUEST = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/acceptfriendrequest";
    public static final String IGNORE_REQUEST = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/rejectfriendrequest";
    public static final String CREATE_LOCATION = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/createlocation";
    public static final String CREATE_AREA = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/createarea";
    public static final String GET_PARTICIPANTS = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/listofuserstoinvit";
    public static final String INVITE_TO_EVENT = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/sendinvitation";
    public static final String IGNORE_EVENT_INVITATION = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/rejecteventinvitation";
    public static final String ACCEPT_EVENT_INVITATION = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/accepteventinvitation";
    public static final String GET_SERVER_REAL_TIME = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/gettimestamp";
    public static final String GET_EVENT_PARTICIPANTS_AND_OWNER_CURRENT_LOCATION = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/geteventuserswithlocation";
    public static final String ADD_SUGGESTION = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/addsuggestion";
    public static final String GET_ALL_SUGGESTION = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/getsuggestions";
    public static final String ACCEPT_SUGGESTION = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/acceptsuggestion";
    public static final String DECLINE_SUGGESTION="http://firstapp-fciswpro.rhcloud.com/restapi/rest/rejectsuggestion";
    public static final String DELETE_EVENT = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/deleteevent";
    public static final String REGISTER_ACCOUNT = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/register";
    public static final String CHECK_REGISTRATION = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/checkregiseration";
    public static final String SEARCH_USER_BY_NAME = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/searchuserbyname";
    public static final String GET_USER_DETAILS = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/getuserdetails";
    public static final String GET_USER_BY_ID = "http://firstapp-fciswpro.rhcloud.com/restapi/rest/getuserbyid";

    public static final int STROKE_COLOR = Color.BLACK;
    public static final int STROKE_WIDTH = 5;
    public static final int FILL_COLOR = 0X553F51B5;

    public static final int FACEBOOK_LOGIN = 1;
    public static final int TWITTER_LOGIN = 0;
}
