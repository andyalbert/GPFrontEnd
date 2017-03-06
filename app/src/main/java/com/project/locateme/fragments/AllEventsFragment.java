package com.project.locateme.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andrew
 * @version 1.0
 * @since 28/2/2017
 */

public class AllEventsFragment extends Fragment {
    private View view;
    private ArrayList<Event> events;
    private ArrayAdapter<Event> eventArrayAdapter;
    private JsonObjectRequest request;
    private RequestQueue requestQueue;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_all_events, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());

        setEventsListView();
        return view;
    }

    private void setEventsListView() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", preferences.getString(getString(R.string.user_id), ""));
            jsonObject.put("pass", preferences.getString(getString(R.string.user_password), ""));

        } catch (JSONException e){
            e.printStackTrace();
        }
        request = new JsonObjectRequest(Request.Method.POST, Constants.GET_ALL_EVENTS, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //todo fill the view
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "please check your connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap(){{put("Content-Type", "application/x-www-form-urlencoded");}};
            }
        };
    }

    @Override
    public void onDestroyView() {
        requestQueue.stop();
        super.onDestroyView();
    }
}
