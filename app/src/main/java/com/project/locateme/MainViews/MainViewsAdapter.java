package com.project.locateme.MainViews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.locateme.Utilities.FragmentsFactory;

/**
 * Created by Andrew on 1/25/2017.
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
        return 4; //// TODO: 1/25/2017 check this number for any further modification
    }
}
