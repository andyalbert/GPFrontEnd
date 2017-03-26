package com.project.locateme.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.project.locateme.HolderActivity;
import com.project.locateme.ImagePickerActivity;
import com.project.locateme.R;
import com.project.locateme.customViews.NestedListView;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.userManagement.Account;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by khaled on 1/28/2017.
 */

public class EventFragment extends Fragment {
    @BindView(R.id.fragment_event_image)
    ImageView eventImage;
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
    @BindView(R.id.fragment_event_delete)
    Button deleteEvent;
    @BindView(R.id.fragment_event_chat_button)
    Button chatEvent;
    private View view;
    private Event model;
    private EventUsersAdapter eventUsersAdapter;
    private ArrayList<Profile> eventUsersArray;
    private HashMap<String, Object> params;
    private boolean isOwner = false;
    private StringRequest stringRequest;
    private RequestQueue requestQueue ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);
        requestQueue = Volley.newRequestQueue(getActivity());
        collapsingToolbar.setTitle("EventName");
        //collapsingToolbar.setBackgroundColor();
        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
                startActivity(intent);
            }
        });
        acceptEvent.setVisibility(View.GONE);
        declineEvent.setVisibility(View.GONE);
        deleteEvent.setVisibility(View.GONE);
        chatEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> params = new HashMap<String , Object>();
                params.put("eventName", model.getName());
                Intent intent = new Intent(getActivity(), HolderActivity.class);
                intent.putExtra(getString(R.string.fragment_name), Constants.Event_CHAT_FRAGMENT);
                intent.putExtra(Constants.HASHMAP , params);
                startActivity(intent);
            }
        });
        initializeEvent();
        setupAdminView();
        initializeUsersListItems();
        return view;
    }
    //TODO : Accept and decline Event Invitation ? ?

    public void setupAdminView() {
        Uri uri = Uri.parse(Constants.GET_OWNERS_EVENT).buildUpon()
                //TODO : get user ID from shared preferences
                .appendQueryParameter("ownerid", "1")
                .build();

        stringRequest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray data = null;
                try {
                    data = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (data.length() == 0) {
                    isOwner = false;
                } else {
                    isOwner = true;
                    acceptEvent.setVisibility(View.VISIBLE);
                    declineEvent.setVisibility(View.VISIBLE);
                    deleteEvent.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "An Error Occurred. Try Again Later", Toast.LENGTH_LONG);
                error.printStackTrace();
            }
        });
        requestQueue.add(stringRequest);

    }

    public void initializeEvent() {
        //TODO: Dont forget to initialize Event Users List
        params = (HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP);
        model = new Event();
        model = (Event) params.get("eventModel");
        collapsingToolbar.setTitle(model.getName());
        description.setText(model.getDescription());
        //dateTextview.setText(model.getDeadline().toString());
        //Glide.with(getActivity()).load(model.getImageURL()).into(eventImage);
    }

    public void initializeUsersListItems() {
        eventUsersArray = new ArrayList<>();
        eventUsersAdapter = new EventUsersAdapter(eventUsersArray, R.layout.fragment_event,
                getActivity());
        eventUsersListView.setAdapter(eventUsersAdapter);
        Uri usersUri = Uri.parse(Constants.GET_EVENT_USERS).buildUpon()
                .appendQueryParameter("eventid" , String.valueOf(model.getId())).build();

        //TODO : Updater list from backend
        StringRequest usersRequest = new StringRequest(Request.Method.POST, usersUri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray users = null;
                try {
                    users = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            for(int i=0 ; i<users.length() ; i++){
                Profile tempObject = new Profile();
                Account tempAccount = new Account();
                JSONObject iterator = null;
                try {
                    iterator = (JSONObject)users.get(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    tempAccount.setId(iterator.getString("User_Id"));
                    tempObject.setFirstName(iterator.getString("firstName"));
                    tempObject.setLastName(iterator.getString("lastName"));
                    tempObject.setPictureURL(iterator.getString("pictureURL"));
                    tempObject.setHomeTown(iterator.getString("homeTown"));
                    //tempObject.setBirthday(General.convertStringToTimestamp(iterator.getString("birthday")));
                    tempObject.setEmail(iterator.getString("email"));
                    tempObject.setName(iterator.getString("name"));
                    tempAccount.setProfile(tempObject);
                    eventUsersArray.add(tempObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                eventUsersAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(usersRequest);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.list_item_event_user, null);
                holder = new Holder();
                holder.name = (TextView) convertView.findViewById(R.id.list_item_event_user_name);
                holder.image = (ImageView) convertView.findViewById(R.id.list_item_event_user_image);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
                holder.name.setText(profileArrayList.get(position).getName());
                Glide.with(context).load(profileArrayList.get(position).getPictureURL()).into(holder.image);
            }
            return convertView;
        }

        class Holder {
            public TextView name;
            public ImageView image;
        }

    }
}

