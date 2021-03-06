package com.project.locateme.mainViews;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.locationManager.Area;
import com.project.locateme.dataHolder.locationManager.Location;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.fragments.EventFragment;
import com.project.locateme.mainViews.homeFragment.EventsAdapter;
import com.project.locateme.mainViews.homeFragment.ZonesAdapter;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Andrew
 * @since 25/1/2017
 * @version 1.0
 */
public class PlaceFragment extends Fragment {

    private ArrayAdapter<Event> eventArrayAdapter;
    private ArrayList<Pair<Event, EventFragment.UserState>> events;
    private ArrayList<Area> zones;
    private ZonesAdapter areaArrayAdapter;
    private View view;
    private StringRequest zoneObjectRequest;
    private StringRequest eventObjectRequest;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private final String VOLLEY_TAG = "placeFragment";
    @BindView(R.id.fragment_places_events_list)
    ExpandableHeightListView eventsListView;
    @BindView(R.id.fragment_places_no_zones)
    TextView noZonesText;
    @BindView(R.id.fragment_places_no_events)
    TextView noEventsText;
    @BindView(R.id.fragment_places_zones_list)
    ExpandableHeightListView zonesListView;


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
        Uri uri = Uri.parse(Constants.GET_ZONES).buildUpon()
                .appendQueryParameter("userid", sharedPreferences.getString(getString(R.string.user_id), ""))
                .appendQueryParameter("pass", sharedPreferences.getString(getString(R.string.user_password), ""))
                .build();
        Log.d("shit i", uri.toString());

        zoneObjectRequest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray array = null;
                try {
                    JSONObject temp = new JSONObject(response);
                    array = temp.getJSONArray("object");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(array != null || array.length() > 0){
                    noZonesText.setVisibility(View.GONE);
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
                            //// TODO: 10/03/17 uncomment when fixed
                            //area.setImageURL(currentObject.getString("image"));
                            area.setRadius(currentObject.getDouble("redius"));

                            location.setLatitude(currentLocation.getDouble("latitude"));
                            location.setLongitude(currentLocation.getDouble("longitude"));
                            location.setName(currentLocation.getString("name"));
                            location.setId(currentLocation.getString("location_id"));

                            for(int j = 0;profiles!= null && j < profiles.length();j++){
                                currentProfile = profiles.getJSONObject(j);
                                profile = new Profile();

                                profile.setUserId(currentProfile.getInt("user_Id"));//// TODO: 08/03/17 string not int
                                //// TODO: 10/03/17 uncomment
                                //profile.setPictureURL(currentLocation.getString("pictureURL"));
                                profile.setName(currentProfile.getString("name"));
                                profile.setFirstName(currentProfile.getString("firstName"));
                                profile.setLastName(currentProfile.getString("lastName"));
                                profile.setHomeTown(currentProfile.getString("homeTown"));
                                //// TODO: 10/03/17 uncomment
//                                profile.setBirthday(currentProfile.getString("birthday"));
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
                    zonesListView.setExpanded(true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "error loading your zones, please try again later", Toast.LENGTH_SHORT).show();
            }
        });

        zoneObjectRequest.setTag(VOLLEY_TAG);
        requestQueue.add(zoneObjectRequest);
    }

    private void setEventListViewItems() {
        Uri uri = Uri.parse(Constants.GET_UPCOMING_EVENTS).buildUpon()
                .appendQueryParameter("userid", sharedPreferences.getString(getString(R.string.user_id), ""))
                .appendQueryParameter("pass", sharedPreferences.getString(getString(R.string.user_password), ""))
                .build();

        eventObjectRequest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainObj = new JSONObject(response);
                    JSONArray array = mainObj.getJSONArray("object");
                    if(array.length() > 0) {
                        noEventsText.setVisibility(View.INVISIBLE);
                        eventsListView.setVisibility(View.VISIBLE);
                    }
                    int moreExist = mainObj.getInt("moreExist"), state;
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
                    eventArrayAdapter = new EventsAdapter(getActivity(), R.layout.fragment_places, events, moreExist == 2, EventsAdapter.EventList.SMALL);
                    eventsListView.setAdapter(eventArrayAdapter);
                    eventsListView.setExpanded(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "error loading your events, please try again later", Toast.LENGTH_SHORT).show();
            }
        });

        eventObjectRequest.setTag(VOLLEY_TAG);
        requestQueue.add(eventObjectRequest);
    }

    @Override
    public void onDestroyView() {
        if(requestQueue != null)
            requestQueue.cancelAll(VOLLEY_TAG);
        super.onDestroyView();
    }
}
