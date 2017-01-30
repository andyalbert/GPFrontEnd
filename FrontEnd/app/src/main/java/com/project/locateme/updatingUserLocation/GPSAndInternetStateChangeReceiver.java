package com.project.locateme.updatingUserLocation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.project.locateme.utilities.General;

/**
 * @author Andrew
 * @since 27/1/2017
 * @version 1.0
 */

public class GPSAndInternetStateChangeReceiver extends BroadcastReceiver {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private int durationBetweenUpdates;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().matches("android.location.PROVIDERS_CHANGED")){

            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && General.isOnline(context)){//gps is turned on, and the mobile is online
                //getting data saved on the device by the user pref
                //int durationBetweenUpdates = context.getSharedPreferences().getInt(); // TODO: 1/27/2017 fix this 
                pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), 0);
                alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, 1, durationBetweenUpdates, pendingIntent);
            } else{ //gps is turned off
                alarmManager.cancel(pendingIntent);
            }
        }
    }
}
