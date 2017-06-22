package com.project.locateme.utilities;


import android.support.v4.app.Fragment;

import com.project.locateme.fragments.AllEventsFragment;
import com.project.locateme.fragments.CreateEventFragment;
import com.project.locateme.fragments.EventChatFragment;
import com.project.locateme.fragments.InviteFriendsFragment;
import com.project.locateme.fragments.ViewSuggestions;
import com.project.locateme.fragments.allFriendsFragment.AllFriendsFragment;
import com.project.locateme.fragments.EventFragment;
import com.project.locateme.googleMap.AddZoneFragment;
import com.project.locateme.googleMap.EventParticipantsLocationFragment;
import com.project.locateme.googleMap.UserLocationFragment;
import com.project.locateme.googleMap.ViewZoneFragment;
import com.project.locateme.mainViews.CreatePlaceFragment;
import com.project.locateme.mainViews.PlaceFragment;
import com.project.locateme.mainViews.PrefFragment;
import com.project.locateme.mainViews.SearchView.SearchFragment;
import com.project.locateme.mainViews.UserProfileFragment;
import com.project.locateme.mainViews.homeFragment.HomeFragment;
import com.project.locateme.mainViews.NotificationFragment;

/**
 * @author Andrew
 * @version 1.2
 * @since 25/1/2017
 */

public class FragmentsFactory {
    public static Fragment getFragmentForTabs(int position){
        Fragment fragment;
        if(position == 0)
            fragment = new HomeFragment();
        else if(position == 1)
            fragment = new PlaceFragment();
        else if(position == 2)
            fragment = new NotificationFragment();
        else
            fragment = new PrefFragment();
        return fragment;
    }
    public static Fragment getFragmentForActivityHolder(String name){
        android.support.v4.app.Fragment fragment = null;
        if(name.equals(Constants.ALL_FRIENDS_FRAGMENT))
            fragment = new AllFriendsFragment();
        else if(name.equals(Constants.EVENT_FRAGMENT))
            fragment = new EventFragment();
        else if(name.equals(Constants.USER_LOCATION_FRAGMENT))
            fragment = new UserLocationFragment();
        else if(name.equals(Constants.SELECT_ZONE_FRAGMENT))
            fragment = new AddZoneFragment();
        else if(name.equals(Constants.VIEW_ZONE_FRAGMENT))
            fragment = new ViewZoneFragment();
        else if(name.equals(Constants.CREATE_EVENT_FRAGMENT))
            fragment = new CreateEventFragment();
        else if(name.equals(Constants.Event_CHAT_FRAGMENT))
            fragment = new EventChatFragment();
        else if(name.equals(Constants.PROFILE_FRAGMENT))
            fragment = new UserProfileFragment();
        else if(name.equals(Constants.CREATE_PLACE_FRAGMENT))
            fragment = new CreatePlaceFragment();
        else if(name.equals(Constants.INVITE_FRIENDS_FRAGMENT))
            fragment = new InviteFriendsFragment();
        else if(name.equals(Constants.ALL_EVENTS_FRAGMENT))
            fragment = new AllEventsFragment();
        else if(name.equals(Constants.VIEW_PARTICIPANTS_LOCATION_FRAGMENT))
            fragment = new EventParticipantsLocationFragment();
        else if(name.equals(Constants.VIEW_SUGGESTIONS))
            fragment = new ViewSuggestions();
        else if(name.equals(Constants.SEARCH_FRAGMENT))
            fragment = new SearchFragment();

        //add more as we go ahead in the application
        return fragment;
    }
}
