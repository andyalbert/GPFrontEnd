package com.project.locateme.updatingUserLocation;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.project.locateme.R;
import com.project.locateme.utilities.General;


/**
 * @author Andrew
 * @since 27/1/2017
 * @version 2.2
 */

public class ProviderNetworkStateBroadcastReceiver extends BroadcastReceiver {
    private static AlarmManager alarmManager;
    private static PendingIntent pendingIntent;
    private int durationBetweenUpdates;

    @Override
    public void onReceive(Context context, Intent intent) {
        //will only continue execution if permission is granted
        if(!isPermissionGranted(context)) //// TODO: 20/03/17 not tested, careful !
            return;


        //used to close in incognito mode
        if(intent.getAction().equals("Initiate") && intent.getIntExtra("disable", 0) == 1){
            if(alarmManager != null){
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                alarmManager = null;
            }
            return;
        }

        //only triggered explicitly from the app, to update the duration time
        if(intent.getAction().equals("Initiate") && alarmManager != null){
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            pendingIntent = PendingIntent.getService(context, 0, new Intent(context, AlarmService.class), 0);
            durationBetweenUpdates = getDurationBetweenUpdates(context);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, 1, durationBetweenUpdates, pendingIntent);
            return;
        }

        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && General.isOnline(context) && alarmManager == null){//gps is turned on, and the mobile is online
            //getting data saved on the device by the user pref
            durationBetweenUpdates = getDurationBetweenUpdates(context);
            pendingIntent = PendingIntent.getService(context, 0, new Intent(context, AlarmService.class), 0);
            alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, 1, durationBetweenUpdates, pendingIntent);
        } else if((!General.isOnline(context) || !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) && alarmManager != null){ //gps is turned off
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            alarmManager = null;
        }
    }

    private int getDurationBetweenUpdates(Context context){
        return context.getSharedPreferences(context.getString(R.string.shared_preferences_name), Context.MODE_PRIVATE).
                getInt(context.getString(R.string.location_update_duration), 300000);
    }

    private boolean isPermissionGranted(Context context){
        return  ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}