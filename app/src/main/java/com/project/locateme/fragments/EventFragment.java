package com.project.locateme.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.locateme.ImagePickerActivity;
import com.project.locateme.R;
import com.project.locateme.customViews.NestedListView;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.userManagement.Profile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by khaled on 1/28/2017.
 */

public class EventFragment extends Fragment {
    @BindView(R.id.fragment_event_add_image)
    Button addImageButton;
    @BindView(R.id.fragment_event_description)
    TextView description;
    @BindView(R.id.fragment_event_date_view)
    TextView dateTextview;
    @BindView(R.id.fragment_event_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.fragment_event_users_list)
    NestedListView eventUsersListView;
    @BindView(R.id.fragment_event_accept_event)
    Button acceptEvent;
    @BindView(R.id.fragment_event_decline_even)
    Button declineEvent;
    private View view;
    private Event model;
    private EventUsersAdapter eventUsersAdapter;
    private ArrayList<Profile> eventUsersArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);

        collapsingToolbar.setTitle("EventName");
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
                startActivity(intent);
            }
        });
        acceptEvent.setVisibility(View.GONE);
        declineEvent.setVisibility(View.GONE);
        //initializeEvent();
        //initializeUsersListItems();
        return view;
    }

    public void initializeEvent() {
        //TODO: Dont forget to initialize Event Users List
        Intent intent = getActivity().getIntent();
        model = new Event();
        model = (Event) intent.getSerializableExtra("event");
        collapsingToolbar.setTitle(model.getName());
        description.setText(model.getDescription());
        dateTextview.setText(model.getDateOfEvent().toString());
    }

    public void initializeUsersListItems() {
        eventUsersAdapter = new EventUsersAdapter(eventUsersArray, R.layout.fragment_event,
                getActivity());
        eventUsersListView.setAdapter(eventUsersAdapter);
        //TODO : update list from backend
    }

    class EventUsersAdapter extends ArrayAdapter<Profile> {
        private ArrayList<Profile> profileArrayList;
        private Context context;
        private int resource;

        public EventUsersAdapter(ArrayList<Profile> profileArrayList, int resource, Context context) {
            super(context, resource);
            this.profileArrayList = profileArrayList;
            this.context = context;
            this.resource = resource;
        }

        @Override
        public int getCount() {
            return profileArrayList.size();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Holder holder;
            if(convertView == null){
                convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.list_item_event_user, null);
                holder = new Holder();
                holder.name =(TextView) convertView.findViewById(R.id.list_item_event_user_name);
                holder.image=(ImageView) convertView.findViewById(R.id.list_item_event_user_image);
                convertView.setTag(holder);
            }
            else {
                holder = (Holder)convertView.getTag();
                holder.name.setText(profileArrayList.get(position).getName());
                Glide.with(context).load(profileArrayList.get(position).getPictureURL()).into(holder.image);
            }
            return convertView;
        }
        class Holder{
            public TextView name;
            public ImageView image;
        }

    }
}

