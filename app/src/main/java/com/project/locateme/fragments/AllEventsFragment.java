package com.project.locateme.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.locationManager.Area;
import com.project.locateme.dataHolder.locationManager.Location;
import com.project.locateme.mainViews.homeFragment.EventsAdapter;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author andrew
 * @version 2.0
 * @since 28/2/2017
 */

public class AllEventsFragment extends Fragment {
    private Unbinder unbinder;
    private View view;
    private ArrayList<Pair<Event, EventFragment.UserState>> events, filteredEvents;
    private EventsAdapter eventArrayAdapter;
    private StringRequest request;
    private RequestQueue requestQueue;
    private SharedPreferences preferences;
    @BindView(R.id.fragment_all_events_list_view)
    ListView listView;
    @BindView(R.id.fragment_all_events_search_view)
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("All events");
        view = inflater.inflate(R.layout.fragment_all_events, container, false);
        unbinder = ButterKnife.bind(this, view);
        requestQueue = Volley.newRequestQueue(getActivity());
        preferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() == 0) {
                    eventArrayAdapter.setData(events);
                } else{
                    filteredEvents = new ArrayList<>();
                    for(Pair<Event, EventFragment.UserState> p : events)
                        if(p.first.getName().contains(newText))
                            filteredEvents.add(p);
                    eventArrayAdapter.setData(filteredEvents);
                }
                eventArrayAdapter.notifyDataSetChanged();
                return false;
            }
        });

        setEventsListView();
        return view;
    }

    private void setEventsListView() {
        Uri uri = Uri.parse(Constants.GET_ALL_EVENTS).buildUpon()
                .appendQueryParameter("userid", preferences.getString(getString(R.string.user_id), ""))
                .appendQueryParameter("pass", preferences.getString(getString(R.string.user_password), ""))
                .build();

        request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Log.e("All" , response);
                    JSONArray array = new JSONArray(response);
                    int state;
                    events = new ArrayList<>();
                    Event event;
                    Location location;
                    Area area;
                    JSONObject object, locationObject, areaObject;
                    for(int i = 0;i < array.length();i++){
                        object = array.getJSONObject(i);
                        areaObject = object.getJSONObject("area");
                        locationObject = areaObject.getJSONObject("location");
                        event = new Event();
                        location = new Location();
                        area = new Area();

                        location.setLongitude(locationObject.getDouble("longitude"));
                        location.setLatitude(locationObject.getDouble("latitude"));
                        location.setId(locationObject.getString("location_id"));
                        location.setName(locationObject.getString("name"));
                        area.setLocation(location);
                        area.setRadius(areaObject.getDouble("redius"));
                        area.setId(areaObject.getString("area_id"));
                        area.setImageURL(areaObject.getString("image"));
                        event.setArea(area);
                        event.setId(object.getString("event_id"));
                        event.setName(object.getString("name"));
                        event.setDescription(object.getString("description"));
                        event.setDateOfEvent(General.convertStringToTimestamp(object.getString("dateOfEvent")));
                        event.setDeadline(General.convertStringToTimestamp(object.getString("deadline")));
                        event.setState(object.getInt("eventState") == 2);

                        state = object.getInt("userStatus");
                        events.add(new Pair<>(event, state == 2 ? EventFragment.UserState.OWNER : EventFragment.UserState.PARTICIPANT));
                    }
                    eventArrayAdapter = new EventsAdapter(getActivity(), R.layout.fragment_all_events, events, false, EventsAdapter.EventList.LARGE);
                    listView.setAdapter(eventArrayAdapter);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "please check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        request.setTag("tag");
        requestQueue.add(request);
    }

    @Override
    public void onDestroyView() {
        if(requestQueue != null)
            requestQueue.cancelAll("tag");
        super.onDestroyView();
        unbinder.unbind();
    }
}
