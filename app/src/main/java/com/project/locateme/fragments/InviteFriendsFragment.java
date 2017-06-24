package com.project.locateme.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.models.android.specific.FriendInvitationState;

import com.project.locateme.R;
import com.project.locateme.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author andrew
 * @since 10/4/2017
 * @version 1.0
 */

public class InviteFriendsFragment extends Fragment {

    public static final String ERROR_MESSAGE = "Couldn't load your friends, please try again";
    private Unbinder unbinder;
    private View view;
    private StringRequest request, invitationRequest;
    private RequestQueue queue;
    private String eventId;
    private SharedPreferences preferences;
    private final String TAG = "requestTag";
    private FriendsAdapter adapter;
    private ArrayList<FriendInvitationState> invitedStatus;
    @BindView(R.id.fragment_invite_friends_list_view)
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Invite friends");
        view = inflater.inflate(R.layout.fragment_invite_friends, container, false);
        unbinder = ButterKnife.bind(this, view);

        eventId = (String) ((HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP)).get("eventId");
        preferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);

        Uri uri = Uri.parse(Constants.GET_PARTICIPANTS).buildUpon()
                .appendQueryParameter("userid", preferences.getString(getString(R.string.user_id), ""))
                .appendQueryParameter("pass", preferences.getString(getString(R.string.user_password), ""))
                .appendQueryParameter("eventid", eventId)
                .build();
        request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                invitedStatus = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(response);
                    Profile profile;
                    JSONObject object;
                    FriendInvitationState invitationState;
                    for(int i = 0;i < array.length();i++){
                        profile = new Profile();
                        invitationState = new FriendInvitationState();
                        object = array.getJSONObject(i);

                        profile.setUserId(object.getInt("user_Id"));
                        profile.setFirstName(object.getString("firstName"));
                        profile.setLastName(object.getString("lastName"));
                        profile.setEmail(object.getString("email"));
                        profile.setHomeTown(object.getString("homeTown"));
                        profile.setName(object.getString("name"));
                        profile.setBirthday(object.getString("birthday"));
                        profile.setPictureURL(object.getString("pictureURL"));

                        invitationState.setProfile(profile);
                        switch (object.getInt("eventStatus")){
                            case 1:
                                invitationState.setState(FriendInvitationState.InvitationState.INVITED);
                                break;
                            case 2:
                                invitationState.setState(FriendInvitationState.InvitationState.PARTICIPANT);
                                break;
                            default: invitationState.setState(FriendInvitationState.InvitationState.NONE);
                        }
                        invitedStatus.add(invitationState);
                    }
                    adapter = new FriendsAdapter(getActivity(), R.layout.fragment_invite_friends, invitedStatus);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
        queue = Volley.newRequestQueue(getActivity());
        request.setTag(TAG);
        queue.add(request);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        if(queue != null)
            queue.cancelAll(TAG);
        super.onDestroyView();
        unbinder.unbind();
    }

    class FriendsAdapter extends ArrayAdapter<FriendInvitationState>{
        private Context context;
        private ArrayList<FriendInvitationState> array;

        public FriendsAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<FriendInvitationState> array) {
            super(context, resource);
            this.context = context;
            this.array = array;
        }

        @Override
        public int getCount() {
            return array.size();
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final ViewHolder holder;
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_invite_friend, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.name.setText(array.get(position).getProfile().getName());
            //// TODO: 10/04/17 uncomment when it's working  --->DONE
            Glide.with(context).load(array.get(position).getProfile().getPictureURL()).into(holder.image);
            switch (array.get(position).getState()){
                case INVITED:
                    holder.inviteFriend.setEnabled(false);
                    holder.inviteFriend.setText("Pending");
                    holder.inviteFriend.setBackgroundColor(context.getResources().getColor(R.color.pending_color));
                    break;
                case PARTICIPANT:
                    holder.inviteFriend.setEnabled(false);
                    break;
                default:
                    holder.inviteFriend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uri = Uri.parse(Constants.INVITE_TO_EVENT).buildUpon()
                                    .appendQueryParameter("userId", preferences.getString(getString(R.string.user_id), ""))
                                    .appendQueryParameter("pass", preferences.getString(getString(R.string.user_password), ""))
                                    .appendQueryParameter("friendid", String.valueOf(array.get(position).getProfile().getUserId()))
                                    .appendQueryParameter("eventid", eventId)
                                    .build();
                            invitationRequest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    array.get(position).setState(FriendInvitationState.InvitationState.INVITED);
                                    final ViewHolder viewHolder = holder;
                                    viewHolder.inviteFriend.setEnabled(false);
                                    viewHolder.inviteFriend.setText("Pending");
                                    viewHolder.inviteFriend.setBackgroundColor(context.getResources().getColor(R.color.pending_color));
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, "Couldn't invite, please try again later", Toast.LENGTH_SHORT).show();
                                }
                            });
                            invitationRequest.setTag(TAG);
                            queue.add(invitationRequest);
                        }
                    });
            }
            return convertView;
        }

        class ViewHolder{
            @BindView(R.id.list_item_invite_friend_button)
            Button inviteFriend;
            @BindView(R.id.list_item_invite_friend_image)
            CircleImageView image;
            @BindView(R.id.list_item_invite_friend_name)
            TextView name;
            public ViewHolder(View view){
                ButterKnife.bind(this, view);
            }
        }
    }
}
