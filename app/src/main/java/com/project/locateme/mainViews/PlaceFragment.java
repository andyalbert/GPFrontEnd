package com.project.locateme.mainViews;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.locationManager.Area;
import com.project.locateme.dataHolder.locationManager.Location;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Andrew
 * @since 25/1/2017
 * @version 1.0
 */
public class PlaceFragment extends Fragment {

    private ArrayAdapter<Event> eventArrayAdapter;
    private ArrayList<Event> events;
    private ArrayList<Area> zones;
    private ArrayAdapter<Area> areaArrayAdapter;
    private View view;
    private JsonObjectRequest eventObjectRequest;
    private JsonObjectRequest zoneObjectRequest;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    @BindView(R.id.fragment_places_events_list)
            ListView eventsListView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_places, container, false);
        ButterKnife.bind(this, view);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(getActivity());
        setPlaceListViewItems();
        setEventListViewItems();

        return view;
    }

    private void setPlaceListViewItems(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", sharedPreferences.getString(getString(R.string.user_id), ""));
            jsonObject.put("pass", sharedPreferences.getString(getString(R.string.user_password), ""));
        } catch (JSONException e){
            e.printStackTrace();
        }

        zoneObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.GET_ZONES, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //todo fill
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap(){{put("Content-Type", "application/x-www-form-urlencoded");}};
            }
        };
    }

    private void setEventListViewItems() {
        //dummy data for events
        //eventRecyclerView.setVisibility(View.VISIBLE);
//        eventsListView.setVisibility(View.VISIBLE);
//        Event e = new Event(), e1 = new Event();
//        e1.setName("Test 2");
//        e1.setDateOfEvent(new Timestamp(156465789));
//        e1.setLocation(new Location(132.13, 45.33, "NY"));
//        e.setName("Test 1");
//        e.setDateOfEvent(new Timestamp(156465789));
//        e.setLocation(new Location(32.2265, 21.55556, "Giza"));
//        events = new ArrayList<>();
//        events.addAll(Arrays.asList(e, e, e1, e1, e1, e1, e1, e1, e1, e1, e1, e1));
//        eventArrayAdapter = new EventsAdapter(getContext(), R.id.fragment_place_events_list, events);
//        eventsListView.setAdapter(eventArrayAdapter);
        //// TODO: 1/29/2017 uncomment these
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", sharedPreferences.getString(getString(R.string.user_id), ""));
            jsonObject.put("pass", sharedPreferences.getString(getString(R.string.user_password), ""));
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        eventObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.GET_UPCOMING_EVENTS, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //// TODO: 1/29/2017 fill here the event list

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap(){{put("Content-Type", "application/x-www-form-urlencoded");}};
            }
        };
        requestQueue.add(eventObjectRequest);



        //eventAdapter = new EventRecyclerViewAdapter(getContext(), events);
        //RecyclerView.LayoutManager eventLayoutManager = new LinearLayoutManager(getContext());
        //eventRecyclerView.setLayoutManager(eventLayoutManager);
        //eventRecyclerView.setAdapter(eventAdapter);
    }

    @Override
    public void onDestroyView() {
        if(requestQueue != null)
            requestQueue.stop();
        super.onDestroyView();
    }
}
