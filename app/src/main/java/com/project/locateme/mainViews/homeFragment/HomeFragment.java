package com.project.locateme.mainViews.homeFragment;


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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.locationManager.Location;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.R;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Andrew
 * @since 25/1/2017
 * @version 1.0
 */

public class HomeFragment extends Fragment {
    private RecyclerView.Adapter<RecyclerView.ViewHolder> friendAdapter;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> eventAdapter;
    private ArrayAdapter<Event> eventArrayAdapter;
    private ArrayList<Event> events;
    private ArrayAdapter<Profile> friendsArrayAdapter;
    private ArrayList<Profile> profiles;
    private View view;
    private JsonObjectRequest friendObjectRequest;
    private JsonObjectRequest eventObjectRequest;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    @BindView(R.id.fragment_home_events_list)
    //RecyclerView eventRecyclerView;
     ListView eventsListView;
    @BindView(R.id.fragment_home_event_text)
    TextView eventText; //hidden by default, should be set to visible later
    @BindView(R.id.fragment_home_friends_list)
    //RecyclerView friendRecyclerView;
    ListView friendsListView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(getActivity());
        setEventListViewItems();
        setFriendsListViewItems();

        return view;
    }

    private void setFriendsListViewItems() {
        //dummy data for testing
        friendsArrayAdapter = new FriendsAdapter(getActivity(), R.id.fragment_home_friends_list, new ArrayList<Profile>(), FriendsAdapter.usage.SMALL_LIST);
        friendsListView.setAdapter(friendsArrayAdapter);

        //// TODO: 1/29/2017 uncomment these
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("id", sharedPreferences.getString(getString(R.string.user_id), ""));
//            jsonObject.put("pass", sharedPreferences.getString(getString(R.string.user_password), ""));
//            //// TODO: 1/29/2017 insert the location into the request, here abdo :D
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        friendObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.GET_CLOSEST_FRIENDS, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                //// TODO: 1/29/2017 fill here the friend list
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        requestQueue.add(friendObjectRequest);


        //friendAdapter = new FriendsRecyclerAdapter(getContext(), new ArrayList<Profile>(), FriendsRecyclerAdapter.usage.SMALL_LIST);
        //RecyclerView.LayoutManager friendLayoutManager = new LinearLayoutManager(getContext());
        //friendRecyclerView.setLayoutManager(friendLayoutManager);
        //friendRecyclerView.setAdapter(friendAdapter);
    }

    private void setEventListViewItems() {
        //dummy data for events
        eventText.setVisibility(View.VISIBLE);
        //eventRecyclerView.setVisibility(View.VISIBLE);
        eventsListView.setVisibility(View.VISIBLE);
        Event e = new Event(), e1 = new Event();
        e1.setName("Test 2");
        e1.setDateOfEvent(new Timestamp(156465789));
        //TODO : Update Models
        //e1.setLocation(new Location(132.13, 45.33, "NY"));
        e.setName("Test 1");
        e.setDateOfEvent(new Timestamp(156465789));
        //TODO : Update Models
        //e.setLocation(new Location(32.2265, 21.55556, "Giza"));
        events = new ArrayList<>();
        events.addAll(Arrays.asList(e, e, e1, e1, e1, e1, e1, e1, e1, e1, e1, e1));
        eventArrayAdapter = new EventsAdapter(getContext(), R.id.fragment_home_events_list, events);
        eventsListView.setAdapter(eventArrayAdapter);
        //// TODO: 1/29/2017 uncomment these
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("id", sharedPreferences.getString(getString(R.string.user_id), ""));
//            jsonObject.put("pass", sharedPreferences.getString(getString(R.string.user_password), ""));
//        } catch (JSONException exception) {
//            exception.printStackTrace();
//        }
//        eventObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.GET_UPCOMING_EVENTS, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                //// TODO: 1/29/2017 fill here the event list
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        requestQueue.add(eventObjectRequest);



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
