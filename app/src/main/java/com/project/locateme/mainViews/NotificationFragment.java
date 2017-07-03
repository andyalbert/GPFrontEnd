package com.project.locateme.mainViews;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.locateme.R;
import com.project.locateme.dataHolder.NotificationManger.AcceptFriend;
import com.project.locateme.dataHolder.NotificationManger.EnterArea;
import com.project.locateme.dataHolder.NotificationManger.EventDeletion;
import com.project.locateme.dataHolder.NotificationManger.EventEditing;
import com.project.locateme.dataHolder.NotificationManger.EventInvitation;
import com.project.locateme.dataHolder.NotificationManger.FriendRequest;
import com.project.locateme.dataHolder.NotificationManger.LeaveArea;
import com.project.locateme.dataHolder.NotificationManger.Notification;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.eventsManager.Suggestion;
import com.project.locateme.dataHolder.locationManager.Area;
import com.project.locateme.dataHolder.locationManager.Location;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.fragments.EventFragment;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;
import com.project.locateme.utilities.NotificationType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Andrew
 * @version 2.0
 * @since 25/1/2017
 */
public class NotificationFragment extends Fragment implements AbsListView.OnScrollListener {
    private View view;
    private Unbinder unbinder;
    private int firstNotification;
    private int lastNotification;
    private NotificationAdapter adapter;
    private final int EVENT_EDITING = 1;
    private final int EVENT_INVITATION = 2;
    private RequestQueue queue;
    private final String VOLLEY_TAG = "tag";
    private SharedPreferences preferences;
    private AtomicBoolean isCurrentlyUpdating;
    private UpdateState updateState;
    private boolean isOldAvailable;
    @BindView(R.id.fragment_notification_list_view)
    ListView listView;
    @BindView(R.id.fragment_notification_swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;


    public enum UpdateState {
        LOADING_NEW, NOT_STARTED, LOADING_OLD
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        unbinder = ButterKnife.bind(this, view);
        preferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        isCurrentlyUpdating = new AtomicBoolean(false);
        updateState = UpdateState.NOT_STARTED;
        isOldAvailable = true;

        firstNotification = -1;
        lastNotification = -1;
        queue = Volley.newRequestQueue(getActivity());
        updateState = UpdateState.NOT_STARTED;
        updateNotifications();


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateState = UpdateState.LOADING_NEW;
                updateNotifications();
            }
        });

        listView.setOnScrollListener(null);

        return view;
    }

    public void setUpdateState(UpdateState updateState) {
        this.updateState = updateState;
    }


    public void updateNotifications() {
        if (isCurrentlyUpdating.get()) {
            //     Toast.makeText(getActivity(), "An update is already underway, please wait", Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false); //in case it is the requested one
            return;
        }

        isCurrentlyUpdating.set(true);
        final int notificationId, age;
        switch (updateState) {
            case NOT_STARTED:
                notificationId = -1;
                age = 1;
                break;
            case LOADING_NEW:
                notificationId = firstNotification;
                age = 1;
                break;
            default:
                notificationId = lastNotification;
                age = -1;
        }
        Uri uri = Uri.parse(Constants.GET_NOTIFICATIONS).buildUpon()
                .appendQueryParameter("user_id", preferences.getString(getString(R.string.user_id), ""))
                .appendQueryParameter("pass", preferences.getString(getString(R.string.user_password), ""))
                .appendQueryParameter("notification_id", String.valueOf(notificationId))
                .appendQueryParameter("age", String.valueOf(age))
                .build();

        StringRequest request = new StringRequest(Request.Method.GET, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Notification> notifications = getNotifications(response);
                switch (updateState) {
                    case NOT_STARTED:
                        adapter = new NotificationAdapter(getActivity(), R.id.fragment_notification_list_view, notifications);
                        listView.setAdapter(adapter);
                        if (notifications.size() > 0) {
                            firstNotification = notifications.get(0).getId();
                            lastNotification = notifications.get(notifications.size() - 1).getId();
                            listView.setOnScrollListener(NotificationFragment.this);
                        }
                        break;
                    case LOADING_OLD: // checking if there are old ones
                        adapter.addAll(notifications);
                        adapter.notifyDataSetChanged();
                        if (notifications.size() > 0)
                            lastNotification = notifications.get(notifications.size() - 1).getId();
                        if(notifications.size() < 10)
                            isOldAvailable = false;
                        break;
                    default:
                        int firstItemFromTop = listView.getFirstVisiblePosition() + notifications.size();
//                        for (int i = notifications.size() - 1; i >= 0; i--)
//                            adapter.insert(notifications.get(i), 0);
                        adapter.addToTop(notifications);
                        adapter.notifyDataSetChanged();
                        listView.setSelectionFromTop(firstItemFromTop, 0);
                        if (notifications.size() > 0)
                            firstNotification = notifications.get(0).getId();
                }

                refreshLayout.setRefreshing(false);
                isCurrentlyUpdating.set(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Couldn't load your notifications, please try again", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
                isCurrentlyUpdating.set(false);
            }
        });
        request.setTag(VOLLEY_TAG);
        queue.add(request);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (queue != null)
            queue.cancelAll(VOLLEY_TAG);
        unbinder.unbind();
    }

    private class NotificationAdapter extends ArrayAdapter<Notification> {
        private ArrayList<Notification> notifications;
        private Context context;

        public NotificationAdapter(Context context, int resource, ArrayList<Notification> notifications) {
            super(context, resource);
            this.context = context;
            this.notifications = notifications;
        }

        @Override
        public int getCount() {
            return notifications.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.list_item_notification, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }
            notifications.get(position).setViewListener((ViewHolder) convertView.getTag(), context);

            return convertView;
        }
        void addAll(ArrayList<Notification> notifications){
            this.notifications.addAll(notifications);
        }
        void addToTop(ArrayList<Notification> notifications) {
            for(int i = notifications.size() - 1;i >= 0;i--)
                this.notifications.add(0, notifications.get(i));
        }
    }

    public class ViewHolder {
        @BindView(R.id.list_item_notification)
        public LinearLayout layout;
        @BindView(R.id.list_item_notification_image)
        public ImageView image;
        @BindView(R.id.list_item_notification_text)
        public TextView text;
        @BindView(R.id.list_item_notification_time)
        public TextView time;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private ArrayList<Notification> getNotifications(String response) {
        ArrayList<Notification> notifications = new ArrayList<>();
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray array = mainObject.getJSONArray("notifications");

            /*{owner_id, timestamp, target = {}, read}*/
            for (int i = 0;i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                JSONObject targetObject = object.getJSONObject("target");
                Notification notification;
                String type = object.getString("type");

                if (type.equals(NotificationType.ACCEPTED_FRIEND_REQUEST.toString())) {
                    notification = new AcceptFriend();
                    ((AcceptFriend) notification).setSenderProfile(profileParser(targetObject));
                } else if (type.equals(NotificationType.FRIEND_REQUEST.toString())) {
                    notification = new FriendRequest();
                    ((FriendRequest) notification).setSenderProfile(profileParser(targetObject));
                } else if (type.equals(NotificationType.AREA_ENTERED.toString())) {
                    notification = new EnterArea();
                    ((EnterArea) notification).setPersonName(targetObject.getJSONArray("users").getJSONObject(0).getString("firstName"));
                    ((EnterArea) notification).setArea(areaParser(targetObject));
                } else if (type.equals(NotificationType.AREA_LEFT.toString())) {
                    notification = new LeaveArea();
                    ((LeaveArea) notification).setPersonName(targetObject.getJSONArray("users").getJSONObject(0).getString("firstName"));
                    ((LeaveArea) notification).setArea(areaParser(targetObject));
                } else if (type.equals(NotificationType.EVENT_DELETION.toString())) {
                    notification = new EventDeletion();
                    ((EventDeletion) notification).setEventName(targetObject.getString("name"));
                } else if (type.equals(NotificationType.EVENT_EDITING.toString())) {
                    notification = new EventEditing();
                    ((EventEditing) notification).setEvent(eventParser(targetObject, notification, EVENT_EDITING));
                    ((EventEditing) notification).setSuggestion(suggestionParser(targetObject.getJSONObject("suggestion")));
                } else { // event invitation
                    notification = new EventInvitation();
                    ((EventInvitation) notification).setEvent(eventParser(targetObject, notification, EVENT_INVITATION));
                }
                notification.setId(object.getInt("id"));
                notification.setRead(object.getBoolean("read"));
                notification.setTimestamp(new Timestamp(object.getLong("timestamp") + 64800000));
                notifications.add(notification);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    private Event eventParser(JSONObject object, Notification notification, int type) {
        Event event = new Event();
        try {
            event.setId(object.getString("event_id"));
            event.setName(object.getString("name"));
            event.setDescription(object.getString("description"));
            event.setDateOfEvent(General.convertStringToTimestamp(object.getString("dateOfEvent")));
            event.setDeadline(General.convertStringToTimestamp(object.getString("deadline")));
            event.setState(object.getInt("eventState") == 2);

            Area area = new Area();
            JSONObject tempObject = object.getJSONObject("area");
            area.setId(tempObject.getString("area_id"));
            area.setRadius(tempObject.getDouble("radius"));
            //area.setImageURL(tempObject.getString("image"));

            tempObject = tempObject.getJSONObject("location");
            Location location = new Location();
            location.setId(tempObject.getString("location_id"));
            location.setLongitude(tempObject.getDouble("longitude"));
            location.setLatitude(tempObject.getDouble("latitude"));
            location.setName(tempObject.getString("name"));

            area.setLocation(location);
            event.setArea(area);

            int userState = object.getInt("eventStatus");
            if (userState == 1) {
                if (type == EVENT_EDITING)
                    ((EventEditing) notification).setUserState(EventFragment.UserState.INVITED);
                else
                    ((EventInvitation) notification).setUserState(EventFragment.UserState.INVITED);
            } else if (userState == 2) {
                if (type == EVENT_EDITING)
                    ((EventEditing) notification).setUserState(EventFragment.UserState.PARTICIPANT);
                else
                    ((EventInvitation) notification).setUserState(EventFragment.UserState.PARTICIPANT);
            } else {
//                if(type == EVENT_EDITING)
//                    ((EventEditing) notification).setUserState(EventFragment.UserState.OWNER);
//                else
//                    ((EventInvitation) notification).setUserState(EventFragment.UserState.OWNER);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return event;
    }

    private Suggestion suggestionParser(JSONObject object) {
        Suggestion suggestion = new Suggestion();
        try {
            suggestion.setDate(General.convertStringToTimestamp(object.getString("new_date")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return suggestion;
    }

    private Area areaParser(JSONObject object) {
        Area area = new Area();
        try {
            area.setId(object.getString("area_id"));
            area.setRadius(object.getDouble("radius"));

//            area.setImageURL(object.getString("image"));
            JSONObject loc = object.getJSONObject("location");
            Location location = new Location();
            location.setId(loc.getString("location_id"));
            location.setLongitude(loc.getDouble("longitude"));
            location.setLatitude(loc.getDouble("latitude"));
            location.setName(loc.getString("name"));

            area.setLocation(location);

            JSONArray usersArray = object.getJSONArray("users");
            ArrayList<Profile> users = new ArrayList<>();
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject tempObject = usersArray.getJSONObject(i);
                Profile profile = new Profile();
                profile.setUserId(tempObject.getInt("id"));
                profile.setFirstName(tempObject.getString("firstName"));
                profile.setLastName(tempObject.getString("lastName"));
                profile.setEmail(tempObject.getString("email"));
                profile.setHomeTown(tempObject.getString("homeTown"));
                profile.setName(tempObject.getString("firstName")+" "+tempObject.getString("lastName"));
                profile.setBirthday(tempObject.getString("birthday"));
                profile.setPictureURL(tempObject.getString("pictureURL"));
                users.add(profile);
            }
            area.setAccounts(users);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return area;
    }

    private Profile profileParser(JSONObject object) {
        Profile profile = new Profile();
        try {
            profile.setFirstName(object.getString("firstName"));
            profile.setLastName(object.getString("lastName"));
            profile.setEmail(object.getString("email"));
            profile.setHomeTown(object.getString("homeTown"));
            profile.setName(object.getString("firstName")+" "+object.getString("lastName"));
            profile.setBirthday(object.getString("birthday"));
            profile.setPictureURL(object.getString("pictureURL"));
            profile.setUserId(object.getInt("id"));

            String state = object.getString("friendState");
            if (state.equals("1"))
                profile.setState(Profile.FriendShipState.FRIEND);
            else if (state.equals("4"))
                profile.setState(Profile.FriendShipState.NOT_FRIEND);
            else if (state.equals("3"))
                profile.setState(Profile.FriendShipState.PENDING_REQUEST);
            else if (state.equals("2"))
                profile.setState(Profile.FriendShipState.ADD_REQUEST);
            else
                profile.setState(Profile.FriendShipState.NONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profile;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isOldAvailable && firstVisibleItem + visibleItemCount >= totalItemCount - 3) {
            updateState = UpdateState.LOADING_OLD;
            updateNotifications();
        }
    }

}
