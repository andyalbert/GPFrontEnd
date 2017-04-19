package com.project.locateme.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
<<<<<<< refs/remotes/origin/e_n
import android.content.SharedPreferences;
=======
>>>>>>> local
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
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
<<<<<<< refs/remotes/origin/e_n
=======
import com.project.locateme.dataHolder.eventsManager.Suggestion;
>>>>>>> local
import com.project.locateme.dataHolder.userManagement.Account;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
<<<<<<< refs/remotes/origin/e_n
=======

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
>>>>>>> local
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author khaled, andrew
 * @version 1.2
 * @since 28/1/2017
 */

public class EventFragment extends Fragment {
    private static View view;
    private static boolean isFirstTime = true;
    private static Timestamp dateOfEvent;
    private static String deadline = new String();
    private static Calendar firstTime;
    private static Calendar secondTime;
    private static String startTime = new String();
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
<<<<<<< refs/remotes/origin/e_n
    @BindView(R.id.fragment_event_notification_action)
    LinearLayout notificationActionLinearLayout;
    private View view;
    private Event event;
=======
    private Event model;
>>>>>>> local
    private EventUsersAdapter eventUsersAdapter;
    private ArrayList<Profile> eventUsersArray;
    private HashMap<String, Object> params;
    private boolean isOwner = false;
<<<<<<< refs/remotes/origin/e_n
    private StringRequest stringRequest, deleteReqest, ignoreRequest, acceptRequest;
    private RequestQueue requestQueue ;
    private SharedPreferences preferences;
    private final String REQUEST_TAG = "tag";
    private UserState userState;
    public enum UserState {
        OWNER, PARTICIPANT, INVITED
    }
=======
    private StringRequest stringRequest;
    private static RequestQueue requestQueue;
    private static Suggestion suggestion;

>>>>>>> local
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);
        preferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(getActivity());
        initializeEvent();
        //initializeUsersListItems();
<<<<<<< refs/remotes/origin/e_n
        chatEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> params = new HashMap<String , Object>();
                params.put("eventName", event.getName());
                params.put("deadline", event.getDeadline());
=======
        acceptEvent.setVisibility(View.GONE);
        declineEvent.setVisibility(View.GONE);
        deleteEvent.setVisibility(View.GONE);
        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : network call
                //StringRequest deleteRequest = new StringRequest(Request.Method.POST , )
                //if successful
                Uri deleteuri = Uri.parse(Constants.DELETE_EVENT).
                        buildUpon().appendQueryParameter("eventid" , model.getId())
                        .build();
                StringRequest request = new StringRequest(Request.Method.POST, deleteuri.toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject result = null;
                                    result = new JSONObject(response);
                                    if (result.getString("operation").equals("Done")) {
                                        Toast.makeText(getActivity(), "Event Deleted Successfuly", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "An error occurred please try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch(JSONException e){
                                    e.printStackTrace();
                                }
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });

                //TODO :Direct to Homepage ?
            }
        });
        chatEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("eventName", model.getName());
>>>>>>> local
                Intent intent = new Intent(getActivity(), HolderActivity.class);
                intent.putExtra(getString(R.string.fragment_name), Constants.Event_CHAT_FRAGMENT);
                intent.putExtra(Constants.HASHMAP, params);
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
<<<<<<< refs/remotes/origin/e_n
                if(userState == UserState.OWNER)
=======
                if (isOwner) {
>>>>>>> local
                    inviteActionButton.setVisibility(View.VISIBLE);
                    //Open Suggestions Page
                    editActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), HolderActivity.class);
                            HashMap<String, Object> params = new HashMap<String, Object>();
                            params.put("eventId", "1");
                            intent.putExtra(Constants.HASHMAP, params);
                            intent.putExtra(getString(R.string.fragment_name), Constants.VIEW_SUGGESTIONS);
                            startActivity(intent);
                        }
                    });
                } else {
                    //Add Modification
                    editActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            suggestModification();
                        }
                    });
                }
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
<<<<<<< refs/remotes/origin/e_n
        inviteActionButton.setOnClickListener(new View.OnClickListener() {
=======
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

                    //acceptEvent.setVisibility(View.VISIBLE);
                    //declineEvent.setVisibility(View.VISIBLE);
                    deleteEvent.setVisibility(View.VISIBLE);
                }
                //initializeUsersListItems();
            }
        }, new Response.ErrorListener() {
>>>>>>> local
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
    public static void suggestionNetworkCall(){
        suggestion = new Suggestion();
        suggestion.setState(false);
        suggestion.setDate(General.convertStringToTimestamp(deadline));
                            Uri uri = Uri.parse(Constants.ADD_SUGGESTION).buildUpon()
                                    .appendQueryParameter("userid", "3")//ToDo: replace with real id
                                    .appendQueryParameter("eventid", "1")
                                    .appendQueryParameter("date", suggestion.getDate().toString())
                                    .build();
                            StringRequest suggestionRequest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject res = null;
                                    String result = null;
                                    try {
                                        res = new JSONObject(response);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        result = res.getString("operation");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("result" , response);
                                    if (result.equalsIgnoreCase("Done")) {
                                        //get around the static access for the getActivity();
                                        Toast.makeText(view.getContext(), "Suggestion has been added !", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(view.getContext(), "An error happened try again Later", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
        requestQueue.add(suggestionRequest);

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
<<<<<<< refs/remotes/origin/e_n
                .appendQueryParameter("eventid" , String.valueOf(event.getId())).build();
=======
                .appendQueryParameter("eventid", String.valueOf(model.getId())).build();
>>>>>>> local

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
                Log.e("jos", users.toString());
                for (int i = 0; i < users.length(); i++) {
                    Profile tempObject = new Profile();
                    Account tempAccount = new Account();
                    JSONObject iterator = null;
                    try {
                        iterator = (JSONObject) users.get(i);
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
                Log.e("eventUsers", String.valueOf(eventUsersArray.size()));
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

<<<<<<< refs/remotes/origin/e_n
    @Override
    public void onDestroyView() {
        if(requestQueue != null)
            requestQueue.cancelAll(REQUEST_TAG);
        super.onDestroyView();
    }

    private class EventUsersAdapter extends ArrayAdapter<Profile> {
=======
    public void suggestModification() {
        Log.e("dialog" , "here");
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            //edit: just disable the past days
            DatePickerDialog da = new DatePickerDialog(getActivity(), this,
                    year, month, day);
            c.add(Calendar.DATE, 1);
            Date newDate = c.getTime();
            da.getDatePicker().setMinDate(newDate.getTime());
            da.setTitle("Suggest Modification");
            return da;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd'T'HH:mm:ss.SSS
            startTime = sdf.format(c.getTime());
            deadline = startTime;
            EventFragment.TimePickerFragment timePicker = new EventFragment.TimePickerFragment();
            timePicker.show(getFragmentManager(), "timePicker");
        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, false);
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);

            SimpleDateFormat sdf = new SimpleDateFormat("'T'HH:mm:ss.SSS");//yyyy-MM-dd'T'HH:mm:ss.SSS
            if (isFirstTime) {
                startTime += sdf.format(c.getTime());
                firstTime = c;
                EventFragment.TimePickerFragment timePickerFragment = new EventFragment.TimePickerFragment();
                timePickerFragment.show(getFragmentManager(), "timePicker");
            } else {
                deadline += sdf.format(c.getTime());
                Calendar c2 = Calendar.getInstance();
                secondTime = c;

                if (secondTime.compareTo(firstTime) <= 0) {
                    Toast.makeText(getActivity(), "Event end time must be after start time", Toast.LENGTH_SHORT).show();
                } else {
                    // SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"), dateFormat = new SimpleDateFormat("yyyy:MM:dd");

                }
                suggestionNetworkCall();
            }
            isFirstTime = !isFirstTime;
        }
    }

    class EventUsersAdapter extends ArrayAdapter<Profile> {
>>>>>>> local
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
            //convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.list_item_event_user, null);
            if (convertView == null) {
                convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.list_item_event_user, parent, false);
                holder = new Holder();
                holder.name = (TextView) convertView.findViewById(R.id.list_item_event_user_name);
                holder.image = (ImageView) convertView.findViewById(R.id.list_item_event_user_image);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
<<<<<<< refs/remotes/origin/e_n
                //holder = new Holder();
                holder.name.setText(profileArrayList.get(position).getName());
                Glide.with(context).load(profileArrayList.get(position).getPictureURL()).into(holder.image);
          }
=======

            }
            holder.name.setText(profileArrayList.get(position).getName());
            Glide.with(context).load(profileArrayList.get(position).getPictureURL()).into(holder.image);
>>>>>>> local
            return convertView;
        }

        class Holder {
            public TextView name;
            public ImageView image;
        }

    }

}

