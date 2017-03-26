package com.project.locateme.mainViews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.project.locateme.utilities.FragmentsFactory;

/**
 * @author Andrew
 * @since 25/1/2017
 * @version 1.0
 */

public class MainViewsAdapter extends FragmentPagerAdapter {


    public MainViewsAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return FragmentsFactory.getFragmentForTabs(position);
    }

    @Override
    public int getCount() {
        return 5; //// TODO: 1/25/2017 check this number for any further modification
        // TODO: 20/03/17 make this only 4 back
    }
}
