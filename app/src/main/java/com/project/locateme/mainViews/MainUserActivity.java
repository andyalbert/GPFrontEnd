package com.project.locateme.mainViews;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.project.locateme.R;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.mainViews.SearchView.MyExpandableListViewAdapter;
import com.project.locateme.mainViews.SearchView.SearchGroup;
import com.project.locateme.updatingUserLocation.GPSAndInternetStateChangeReceiver;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Andrew
 * @since 25/1/2017
 * @version 1.0
 */

public class MainUserActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener{
    private SearchView search;

    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        //For SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) findViewById(R.id.search);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);
        search.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });
        ButterKnife.bind(this);
        setViewPagerTabs();
    }

    private void setViewPagerTabs() {
        MainViewsAdapter mainViewsAdapter = new MainViewsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainViewsAdapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_home);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_message);
        tabLayout.getTabAt(2).setIcon(R.mipmap.ic_notification);
        tabLayout.getTabAt(3).setIcon(R.mipmap.ic_settings);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(getApplicationContext(), "selected", Toast.LENGTH_SHORT).show();
                if(tab.getPosition() == 1)//// TODO: 1/25/2017 to be done later, after fixing the mipmap overload
                    tab.setIcon(R.mipmap.ic_message_selected);
//                switch(tab.getPosition()){
//                    case 0: tab.setIcon(R.d);
//                        break;
//                    case 1: tab.setIcon();
//                        break;
//                    case 2: tab.setIcon();
//                        break;
//                    case 3: tab.setIcon();
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 1)
                    tab.setIcon(R.mipmap.ic_message);
                Toast.makeText(getApplicationContext(), "unselected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: 1/27/2017 remove this from here and insert it in the after login activity so it got fixed every time
        // this is used to set the not enabled broadcast receiver of the updating user location into true
        PackageManager pm  = MainUserActivity.this.getPackageManager();
        ComponentName componentName = new ComponentName(MainUserActivity.this, GPSAndInternetStateChangeReceiver.class);
        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        //// TODO: 1/28/2017 transfer this to login
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.shared_preferences_name), MODE_PRIVATE);
        preferences.edit().putString(getString(R.string.user_id), "1").apply();//// TODO: 1/28/2017 this id is static for testing
        preferences.edit().putString(getString(R.string.user_password), "00000000").apply(); //// TODO: 1/28/2017 also static, must be changed
    }


    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
