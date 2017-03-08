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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.locationManager.Area;
import com.project.locateme.dataHolder.locationManager.Location;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.mainViews.homeFragment.ZonesAdapter;
import com.project.locateme.utilities.Constants;

import org.json.JSONArray;
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
    private ZonesAdapter areaArrayAdapter;
    private View view;
    private JsonObjectRequest eventObjectRequest;
    private JsonObjectRequest zoneObjectRequest;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    @BindView(R.id.fragment_places_events_list)
    ListView eventsListView;
    @BindView(R.id.fragment_places_no_zones)
    TextView noZoesText;
    @BindView(R.id.fragment_places_zones_list)
    ListView zonesListView;


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
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", sharedPreferences.getString(getString(R.string.user_id), ""));
            jsonObject.put("pass", sharedPreferences.getString(getString(R.string.user_password), ""));
        } catch (JSONException e){
            e.printStackTrace();
        }

        zoneObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.GET_ZONES, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray array = null; //todo make sure that's the real name
                try {
                    array = response.getJSONArray("object");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(array != null || array.length() > 0){
                    noZoesText.setVisibility(View.INVISIBLE);
                    zonesListView.setVisibility(View.VISIBLE);
                    zones = new ArrayList<>();

                    Area area;
                    Location location;
                    ArrayList<Profile> accounts;
                    Profile profile;
                    JSONObject currentObject = null;
                    JSONObject currentLocation = null;
                    JSONArray profiles = null;
                    JSONObject currentProfile = null;
                    for(int i = 0;i < array.length();i++){
                        area = new Area();
                        location = new Location();
                        accounts = new ArrayList<>();
                        try{
                            currentObject = array.getJSONObject(i);
                            currentLocation = currentObject.getJSONObject("location");
                            profiles = currentObject.getJSONArray("users");

                            area.setId(currentObject.getString("area_id"));
                            area.setImageURL(currentObject.getString("image"));
                            area.setRadius(currentObject.getDouble("redius"));

                            location.setLatitude(currentLocation.getDouble("latitude"));
                            location.setLongitude(currentLocation.getDouble("longitude"));
                            location.setName(currentLocation.getString("name"));
                            location.setId(currentLocation.getString("location_id"));

                            for(int j = 0;profiles!= null && j < profiles.length();j++){
                                currentProfile = profiles.getJSONObject(j);
                                profile = new Profile();

                                profile.setUserId(currentProfile.getInt("user_Id"));//// TODO: 08/03/17 string not int
                                profile.setPictureURL(currentLocation.getString("pictureURL"));
                                profile.setName(currentProfile.getString("name"));
                                profile.setFirstName(currentLocation.getString("firstName"));
                                profile.setLastName(currentLocation.getString("lastName"));
                                profile.setHomeTown(currentLocation.getString("homeTown"));
                                profile.setBirthday(currentLocation.getString("birthday"));
                                profile.setState(Profile.FriendShipState.FRIEND);

                                accounts.add(profile);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                        area.setAccounts(accounts);
                        area.setLocation(location);
                        zones.add(area);
                    }
                    areaArrayAdapter = new ZonesAdapter(getActivity(), R.id.fragment_places_zones_list, zones);
                    zonesListView.setAdapter(areaArrayAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "error loading your zones, please try again later", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap(){{put("Content-Type", "application/x-www-form-urlencoded");}};
            }
        };
    }

    private void setEventListViewItems() {
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
    }

    @Override
    public void onDestroyView() {
        if(requestQueue != null)
            requestQueue.stop();
        super.onDestroyView();
    }
}
