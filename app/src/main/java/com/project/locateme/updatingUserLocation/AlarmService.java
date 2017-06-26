package com.project.locateme.updatingUserLocation;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.locateme.R;
import com.project.locateme.utilities.Constants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author andrew
 * @version 1.0
 * @since 20/3/2017
 */
public class AlarmService extends Service implements LocationListener {
    private LocationManager mLocationManager;
    private RequestQueue queue;
    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getLocation(AlarmService.this);
        Log.d("hi", "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    private void getLocation(Context context) {

        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);

        new Timer().schedule(new TimerTask() {//close the service in case of bad network
            @Override
            public void run() {
                if(queue != null)
                    queue.cancelAll("tag");
                mLocationManager.removeUpdates(AlarmService.this);
                stopSelf();
            }
        }, 20000);
    }

    @Override
    public void onDestroy() {
        Log.d("hi", "onDestroy: ");
        super.onDestroy();
    }

    /**
     * in this method, a request is sent to the server to update the location, then the service is killed :(
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        mLocationManager.removeUpdates(this);
        Log.d("location", location.getLatitude() + " " + location.getLongitude());

        SharedPreferences pref = getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);
        Uri uri = Uri.parse(Constants.UPDATE_LOCATION_URL).buildUpon()
                .appendQueryParameter("userid", pref.getString(getString(R.string.user_id), ""))
                .appendQueryParameter("pass", pref.getString(getString(R.string.user_password), ""))
                .appendQueryParameter("lat", String.valueOf(location.getLatitude()))
                .appendQueryParameter("lon", String.valueOf(location.getLongitude()))
                .build();

        StringRequest request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                stopSelf();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopSelf();
            }
        });

        queue = Volley.newRequestQueue(this);
        request.setTag("tag");
        queue.add(request);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}