package com.project.locateme.MainViews.HomeFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.project.locateme.DateHolder.EventsManager.Event;
import com.project.locateme.DateHolder.LocationManager.Location;
import com.project.locateme.DateHolder.UserManagement.Profile;
import com.project.locateme.R;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andrew on 1/25/2017.
 */

public class HomeFragment extends Fragment {
    private ArrayAdapter<Event> eventArrayAdapter;
    private ArrayList<Event> events;
    private ArrayAdapter<Profile> friendsArrayAdapter;
    private ArrayList<Profile> profiles;
    private View view;
    @BindView(R.id.fragment_home_events_list)
    ListView eventsListView;
    @BindView(R.id.fragment_home_event_text)
    TextView eventText; //hidden by default, should be set to visible later
    @BindView(R.id.fragment_home_friends_list)
    ListView friendsListView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        setEventListViewItems();
        setFriendsListViewItems();

        return view;
    }

    private void setFriendsListViewItems() {
        //dummy data for testing
        friendsArrayAdapter = new FriendsAdapter(getActivity(), R.id.fragment_home_friends_list, new ArrayList<Profile>(), FriendsAdapter.usage.SMALL_LIST);
        friendsListView.setAdapter(friendsArrayAdapter);
    }

    private void setEventListViewItems() {
        //dummy data for events
        eventText.setVisibility(View.VISIBLE);
        eventsListView.setVisibility(View.VISIBLE);
        Event e = new Event(), e1 = new Event();
        e1.setName("Test 2");
        e1.setDateOfEvent(new Timestamp(156465789));
        e1.setLocation(new Location(132.13, 45.33, "NY"));
        e.setName("Test 1");
        e.setDateOfEvent(new Timestamp(156465789));
        e.setLocation(new Location(32.2265, 21.55556, "Giza"));
        events = new ArrayList<>();
        events.addAll(Arrays.asList(e, e, e1, e1, e1, e1, e1, e1, e1, e1, e1, e1));
        eventArrayAdapter = new EventsAdapter(getContext(), R.id.fragment_home_events_list, events);
        eventsListView.setAdapter(eventArrayAdapter);
    }

}
