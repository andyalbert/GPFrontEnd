package com.project.locateme.updatingUserLocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.locateme.R;
import com.project.locateme.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrew on 1/27/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        JSONObject json = new JSONObject();
        double latitude = 0, longitude = 0;
//// TODO: 1/28/2017 abdo, add the code to get the location here, note : only get it once !!

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
}
