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

import com.facebook.FacebookSdk;
import com.project.locateme.HolderActivity;
import com.project.locateme.R;
import com.project.locateme.updatingUserLocation.ProviderNetworkStateBroadcastReceiver;
import com.project.locateme.utilities.Constants;


import java.util.HashMap;

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
    private SharedPreferences preferences;
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
        FacebookSdk.sdkInitialize(getApplicationContext());
        preferences = getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);


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
                Intent intent = new Intent(getBaseContext(), HolderActivity.class);
                intent.putExtra(getString(R.string.fragment_name), Constants.SEARCH_FRAGMENT);
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("searchTerm" , "aa");
                intent.putExtra(Constants.HASHMAP , parameters);
                startActivity(intent);

            }
        });

        ButterKnife.bind(this);
        setViewPagerTabs();

        checkPermission();
        callBroadcast();
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
        Intent intent = new Intent(getBaseContext(), HolderActivity.class);
        intent.putExtra(getString(R.string.fragment_name), Constants.SEARCH_FRAGMENT);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("searchTerm" , query);
        intent.putExtra(Constants.HASHMAP , parameters);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void setViewPagerTabs() {
        viewPager.setAdapter(mainViewsAdapter);
        viewPager.setOffscreenPageLimit(4);//// TODO: 20/03/17 make this 3 back again

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_home);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_message);
        tabLayout.getTabAt(2).setIcon(R.mipmap.ic_notification);
        tabLayout.getTabAt(3).setIcon(R.mipmap.ic_settings);//// TODO: 20/03/17 remove this
        tabLayout.getTabAt(4).setIcon(R.mipmap.ic_settings);

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
