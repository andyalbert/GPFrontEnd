package com.project.locateme.utilities;




import android.support.v4.app.Fragment;

import com.project.locateme.fragments.AllFriendsFragment;
import com.project.locateme.mainViews.homeFragment.HomeFragment;
import com.project.locateme.mainViews.MessageFragment;
import com.project.locateme.mainViews.NotificationFragment;
import com.project.locateme.mainViews.SettingsFragment;

/**
 * @author Andrew
 * @version 1.1
 * @since 25/1/2017
 */

public class FragmentsFactory {
    public static Fragment getFragmentForTabs(int position){
        Fragment fragment;
        if(position == 0)
            fragment = new HomeFragment();
        else if(position == 1)
            fragment = new MessageFragment();
        else if(position == 1)
            fragment = new NotificationFragment();
        else
            fragment = new SettingsFragment();
        return fragment;
    }
    public static Fragment getFragmentForActivityHolder(String name){
        Fragment fragment = null;
        if(name.equals(Constants.ALL_FRIENDS_FRAGMENT))
            fragment = new AllFriendsFragment();
        //add more as we go ahead in the application
        return fragment;
    }
}
