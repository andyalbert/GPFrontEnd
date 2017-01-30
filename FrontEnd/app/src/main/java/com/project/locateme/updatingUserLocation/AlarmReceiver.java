package com.project.locateme.updatingUserLocation;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.locateme.R;
import com.project.locateme.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * @author Andrew
 * @since 27/1/2017
 * @version 1.1
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        JSONObject json = new JSONObject();
        Location loc = getLocation();
        double latitude = loc.getLatitude(), longitude = loc.getLongitude();
        try {
            SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.shared_preferences_name), context.MODE_PRIVATE);
            json.put("id", pref.getString(context.getString(R.string.user_id), ""));
            json.put("pass", pref.getString(context.getString(R.string.user_password), ""));
            json.put("lat", latitude);
            json.put("long", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.UPDATE_LOCATION_URL, json, null, null); // note, both listeners are null as there is no response
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    private Location getLocation() {
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //useless if condition , but don't erase it , by abdo
            }
        Location l1 = mLocationManager.getLastKnownLocation("passive");
        Location l2 = mLocationManager.getLastKnownLocation("network");
        if(l1.getAccuracy() >= l2.getAccuracy()) {
            return l1;
        }
        else{
            return l2;
        }

    }
}
