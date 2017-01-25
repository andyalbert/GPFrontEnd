package com.project.locateme.MainViews;


import android.support.v4.app.Fragment;

/**
 * Created by Andrew on 1/25/2017.
 */

public class FragmentsFactory {
    public static Fragment getFragment(int position){
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
}
