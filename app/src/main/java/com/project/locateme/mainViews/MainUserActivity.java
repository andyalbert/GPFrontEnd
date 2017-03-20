package com.project.locateme.mainViews;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.project.locateme.R;
import com.project.locateme.updatingUserLocation.ProviderNetworkStateBroadcastReceiver;

import java.security.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.project.locateme.utilities.General.isOnline;

/**
 * @author Andrew
 * @since 25/1/2017
 * @version 1.0
 */

public class MainUserActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener{
    private SearchView search;

    private MainViewsAdapter mainViewsAdapter;
    private final int START_UPDATER_TIMER = 300000;//first update to be after 5 minutes
    private final int UPDATE_INTERVAL = 600000;// update each 10 minutes, can be added to pref and changed later
    private final int USER_LOCATION_REQUEST_CODE = 11112;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private BroadcastReceiver Updater = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //// TODO: 17/03/17 update now
          //  ((HomeFragment) mainViewsAdapter.getItem(0)).;
            //((PlaceFragment) mainViewsAdapter.getItem(1)).;
            Log.e(MainUserActivity.this.getLocalClassName(), "update initiated");
        }
    };
    private BroadcastReceiver AlarmTrigger = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(isOnline(context) && alarmManager == null){
                pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent("Update"), 0);
                alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, START_UPDATER_TIMER, UPDATE_INTERVAL, pendingIntent);
            } else if(alarmManager != null && !isOnline(context)){ //gps is turned off
                alarmManager.cancel(pendingIntent);
                alarmManager = null;
            }
        }
    };

    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        mainViewsAdapter = new MainViewsAdapter(getSupportFragmentManager());

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

        checkPermission();
        callBroadcast();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: 1/27/2017 remove this from here and insert it in the after login activity so it got fixed every time
        // this is used to set the not enabled broadcast receiver of the updating user location into true
        PackageManager pm  = MainUserActivity.this.getPackageManager();
        ComponentName componentName = new ComponentName(MainUserActivity.this, ProviderNetworkStateBroadcastReceiver.class);
        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        //// TODO: 1/28/2017 transfer this to login
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.shared_preferences_name), MODE_PRIVATE);
        preferences.edit().putString(getString(R.string.user_id), "3").apply();//// TODO: 1/28/2017 this id is static for testing
        preferences.edit().putString(getString(R.string.user_password), "00000000").apply(); //// TODO: 1/28/2017 also static, must be changed
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(alarmManager != null){
            alarmManager.cancel(pendingIntent);
            alarmManager = null;
        }
        unregisterReceiver(AlarmTrigger);
        unregisterReceiver(Updater);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerUpdaters();
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

    private void setViewPagerTabs() {
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

    //used for updating the google maps tab and the places/events tabs
    private void registerUpdaters() {
        IntentFilter filter = new IntentFilter();
        //  filter.addAction("android.location.PROVIDERS_CHANGED");
        filter.addAction("Update");
        registerReceiver(Updater, filter);

        IntentFilter alarmFilter = new IntentFilter();
        alarmFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        alarmFilter.addAction("My");
        registerReceiver(AlarmTrigger, alarmFilter);
        //new IntentFilter("Update")
        new Handler().postDelayed(new Runnable() {//this is used for triggering the alarm manger if it doesn' work at start
            @Override
            public void run() {
                sendBroadcast(new Intent("My"));
            }
        }, 2000);
    }

    //for checking the permission of accessing the network
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation("Permission Needed", "Location permission is important for updating your last position and for viewing your location on map, please provide");
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, USER_LOCATION_REQUEST_CODE);
            }
        }
    }

    /**
     * this method is for building an alert dialogue to pop up when the user refuses to grant the permission
     * @param title
     * @param message
     */
    private void showExplanation(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(MainUserActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, USER_LOCATION_REQUEST_CODE);
                    }
                });
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == USER_LOCATION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //// TODO: 20/03/17 check if this can be enabled, just disabled as the 22<= apis is not invoked
//                PackageManager pm  = MainUserActivity.this.getPackageManager();
//                ComponentName componentName = new ComponentName(MainUserActivity.this, ProviderNetworkStateBroadcastReceiver.class);
//                pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                        PackageManager.DONT_KILL_APP);

                callBroadcast();
            }
        }
    }

    private void callBroadcast() {
        Intent intent = new Intent("Initiate");
        sendBroadcast(intent);
    }
}
