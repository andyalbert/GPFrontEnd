package com.project.locateme.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.userManagement.Account;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author khaled, andrew
 * @since 28/1/2017
 * @version 1.2
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
    ListView eventUsersListView;
    @BindView(R.id.fragment_event_accept_event)
    Button acceptEvent;
    @BindView(R.id.fragment_event_decline_even)
    Button ignoreEvent;
    @BindView(R.id.fragment_event_delete)
    Button deleteEvent;
    @BindView(R.id.fragment_event_chat_button)
    Button chatEvent;
    @BindView(R.id.fragment_event_list_floating_button)
    FloatingActionButton mainActionButton;
    @BindView(R.id.fragment_event_edit_floating_button)
    FloatingActionButton editActionButton;
    @BindView(R.id.fragment_event_invite_floating_button)
    FloatingActionButton inviteActionButton;
    @BindView(R.id.fragment_event_transparent_layout)
    LinearLayout transparentLinearLayout;
    @BindView(R.id.fragment_event_notification_action)
    LinearLayout notificationActionLinearLayout;
    private View view;
    private Event event;
    private EventUsersAdapter eventUsersAdapter;
    private ArrayList<Profile> eventUsersArray;
    private HashMap<String, Object> params;
    private boolean isOwner = false;
    private StringRequest stringRequest, deleteReqest, ignoreRequest, acceptRequest;
    private RequestQueue requestQueue ;
    private SharedPreferences preferences;
    private final String REQUEST_TAG = "tag";
    private UserState userState;
    public enum UserState {
        OWNER, PARTICIPANT, INVITED
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);
        preferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(getActivity());
        //collapsingToolbar.setTitle("EventName");
        //collapsingToolbar.setBackgroundColor();
//        eventImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
//                startActivity(intent);
//            }
//        });
        initializeEvent();
        //initializeUsersListItems();
        chatEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> params = new HashMap<String , Object>();
                params.put("eventName", event.getName());
                params.put("deadline", event.getDeadline());
                Intent intent = new Intent(getActivity(), HolderActivity.class);
                intent.putExtra(getString(R.string.fragment_name), Constants.Event_CHAT_FRAGMENT);
                intent.putExtra(Constants.HASHMAP , params);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initializeActionButtonsListeners() {
        mainActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transparentLinearLayout.setVisibility(View.VISIBLE);
                editActionButton.setVisibility(View.VISIBLE);
                if(userState == UserState.OWNER)
                    inviteActionButton.setVisibility(View.VISIBLE);
                mainActionButton.setClickable(false);
            }
        });
        transparentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
                mainActionButton.setClickable(true);
                editActionButton.setVisibility(View.GONE);
                inviteActionButton.setVisibility(View.GONE);
            }
        });
        inviteActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HolderActivity.class);
                intent.putExtra(getString(R.string.fragment_name), Constants.INVITE_FRIENDS_FRAGMENT);
                intent.putExtra(Constants.HASHMAP, new HashMap(){
                    {
                        put("eventId", event.getId());
                    }
                });
                startActivity(intent);
            }
        });
    }

    public void initializeEvent() {
        params = (HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP);
        event = (Event) params.get("eventModel");
        userState = (UserState) params.get("userStatus");

        switch (userState){
            case OWNER:
                deleteEvent.setVisibility(View.VISIBLE);
                deleteEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(Constants.DELETE_EVENT).buildUpon()
                                .appendQueryParameter("eventid", event.getId())
                                .appendQueryParameter("userid", preferences.getString(getString(R.string.user_id), ""))
                                .appendQueryParameter("pass", preferences.getString(getString(R.string.user_password), ""))
                                .build();
                        deleteReqest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                getActivity().finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), "Couldn't delete right now, please try again later", Toast.LENGTH_SHORT).show();
                            }
                        });
                        deleteReqest.setTag(REQUEST_TAG);
                        requestQueue.add(deleteReqest);
                    }
                });
            case PARTICIPANT:
                mainActionButton.setVisibility(View.VISIBLE);
                chatEvent.setEnabled(true);
                break;
            case INVITED:
                notificationActionLinearLayout.setVisibility(View.VISIBLE);
                ignoreEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(Constants.IGNORE_EVENT_INVITATION).buildUpon()
                                .appendQueryParameter("eventid", event.getId())
                                .appendQueryParameter("userid", preferences.getString(getString(R.string.user_id), ""))
                                .appendQueryParameter("pass", preferences.getString(getString(R.string.user_password), ""))
                                .build();

                        ignoreRequest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                getActivity().finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), "Couldn't complete your request, please try again", Toast.LENGTH_SHORT).show();
                            }
                        });

                        ignoreRequest.setTag(REQUEST_TAG);
                        requestQueue.add(ignoreRequest);
                    }
                });
                acceptEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(Constants.ACCEPT_EVENT_INVITATION).buildUpon()
                                .appendQueryParameter("eventid", event.getId())
                                .appendQueryParameter("userid", preferences.getString(getString(R.string.user_id), ""))
                                .appendQueryParameter("pass", preferences.getString(getString(R.string.user_password), ""))
                                .build();

                        acceptRequest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                notificationActionLinearLayout.setVisibility(View.INVISIBLE);
                                chatEvent.setEnabled(true);
                                mainActionButton.setVisibility(View.VISIBLE);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), "Couldn't complete your request, please try again", Toast.LENGTH_SHORT).show();
                            }
                        });

                        acceptRequest.setTag(REQUEST_TAG);
                        requestQueue.add(acceptRequest);
                    }
                });
        }

        initializeActionButtonsListeners();
        //initializeUsersListItems();

        collapsingToolbar.setTitle(event.getName());
        collapsingToolbar.setBackgroundColor(15);
        description.setText(event.getDescription());
        dateTextview.setText(General.convertTimeatampToString(event.getDateOfEvent()));
        Glide.with(getActivity()).load(event.getArea().getImageURL()).into(eventImage);
    }

    public void initializeUsersListItems() {
        eventUsersArray = new ArrayList<>();
        eventUsersAdapter = new EventUsersAdapter(eventUsersArray, R.layout.fragment_event,
                getActivity());
        eventUsersListView.setAdapter(eventUsersAdapter);
        Uri usersUri = Uri.parse(Constants.GET_EVENT_USERS).buildUpon()
                .appendQueryParameter("eventid" , String.valueOf(event.getId())).build();

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
            Log.e("jos" , users.toString());
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
                    tempAccount.setId(iterator.getString("user_Id"));
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
                Log.e("eventUsers" , String.valueOf(eventUsersArray.size()));
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

    @Override
    public void onDestroyView() {
        if(requestQueue != null)
            requestQueue.cancelAll(REQUEST_TAG);
        super.onDestroyView();
    }

    private class EventUsersAdapter extends ArrayAdapter<Profile> {
        private ArrayList<Profile> profileArrayList;
        private Context context;

        private EventUsersAdapter(ArrayList<Profile> profileArrayList, int resource, Context context) {
            super(context, resource);
            this.profileArrayList = profileArrayList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return profileArrayList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.list_item_event_user, null);
            if (convertView == null) {
                convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.list_item_event_user, null);
                holder = new Holder();
                holder.name = (TextView) convertView.findViewById(R.id.list_item_event_user_name);
                holder.image = (ImageView) convertView.findViewById(R.id.list_item_event_user_image);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
                //holder = new Holder();
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

