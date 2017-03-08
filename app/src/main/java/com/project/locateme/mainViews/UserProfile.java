package com.project.locateme.mainViews;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Freezable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.project.locateme.R;
import com.project.locateme.dataHolder.userManagement.Account;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by abdo on 2/25/17.
 */

public class UserProfile extends Fragment
{
    private Profile profile;
    private SharedPreferences preferences;
    private HashMap<String, Object> parameters;
//    private Account account = new Account(); // TODO : get an account from intent
    private CircleImageView profile_pic;
    private TextView username_in_profile;
    private Button FriendState;
    private TextView email_address;
    private TextView home_town_in_profile;
    private TextView birthdate;
    private LinearLayout linearLayout;
    private JsonObjectRequest request;
    private RequestQueue requestQueue;
    private final String VOLLEY_REQUEST_TAG = "userProfileTag";
    private final String ERROR_REQUEST = "Error happens, please try again";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_user_profile, container, false);
        preferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        parameters = (HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP);
        requestQueue = Volley.newRequestQueue(getActivity());

        final Profile profile = (Profile) parameters.get("profile");

        profile_pic = (CircleImageView) v.findViewById(R.id.user_profile_pic);
        Glide.with(getContext()).load(profile.getPictureURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profile_pic);
        username_in_profile = (TextView) v.findViewById(R.id.username_in_profile);
        username_in_profile.setText(profile.getName());
        FriendState = (Button) v.findViewById(R.id.FriendState);
        linearLayout = (LinearLayout) v.findViewById(R.id.fragment_places_zone_linear_layout);

        setUpProfileState(v, profile);

        email_address = (TextView) v.findViewById(R.id.email_address);
        email_address.setText(profile.getEmail());

        home_town_in_profile = (TextView) v.findViewById(R.id.home_town_in_profile);
        home_town_in_profile.setText(profile.getHomeTown());

        birthdate = (TextView) v.findViewById(R.id.birthdate);
        birthdate.setText(profile.getBirthday().toString());
        return v;
    }

    private void setUpProfileState(View v, final Profile profile) {
        switch (profile.getState()){
            case NOT_FRIEND:
                FriendState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject object = new JSONObject();
                        try {
                            object.put("userid", preferences.getString(getString(R.string.user_id), ""));
                            object.put("friendid", preferences.getString(getString(R.string.user_password), ""));
                            object.put("pass", preferences.getString(getString(R.string.user_password), ""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        request = new JsonObjectRequest(Request.Method.POST, Constants.ADD_FRIEND, object, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                FriendState.setText("Pending Friend Request");
                                FriendState.setBackgroundResource(R.drawable.button_shape_pending_friend_request);
                                profile.setState(Profile.FriendShipState.PENDING_REQUEST);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), ERROR_REQUEST, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                request.setTag(VOLLEY_REQUEST_TAG);
                requestQueue.add(request);
                break;
            case FRIEND:
                FriendState.setBackgroundResource(R.drawable.button_shape_remove_friend);

                FriendState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject object = new JSONObject();
                        try {
                            //// TODO: 08/03/17 check this with backend
//                    object.put("userid", preferences.getString(getString(R.string.user_id), ""));
//                    object.put("friendid", preferences.getString(getString(R.string.user_password), ""));
                            object.put("pass", preferences.getString(getString(R.string.user_password), ""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        request = new JsonObjectRequest(Request.Method.POST, Constants.REMOVE_FRIEND, object, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                FriendState.setBackgroundResource(R.drawable.button_shape_add_friend);
                                FriendState.setText("Add as a friend");
                                profile.setState(Profile.FriendShipState.NOT_FRIEND);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), ERROR_REQUEST, Toast.LENGTH_SHORT).show();
                            }
                        });
                        request.setTag(VOLLEY_REQUEST_TAG);
                        requestQueue.add(request);
                    }
                });
                FriendState.setText("Remove friend");
                break;
            case PENDING_REQUEST:
                FriendState.setBackgroundResource(R.drawable.button_shape_pending_friend_request);
                FriendState.setText("Pending Friend Request");
                FriendState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "You can't take any further action without the user approve/disapprove your request", Toast.LENGTH_LONG).show();
                    }
                });
            case ADD_REQUEST:
                FriendState.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                TextView acceptRequest = (TextView) v.findViewById(R.id.fragment_user_profile_accept);
                TextView ignoreRequest = (TextView) v.findViewById(R.id.fragment_user_profile_delete);
                acceptRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject object = new JSONObject();
                        //// TODO: 08/03/17 fill json object
                        request = new JsonObjectRequest(Request.Method.POST, Constants.ACCEPT_REQUEST, object, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                linearLayout.setVisibility(View.GONE);
                                FriendState.setVisibility(View.VISIBLE);
                                FriendState.setText("Remove friend");
                                FriendState.setBackgroundResource(R.drawable.button_shape_remove_friend);
                                profile.setState(Profile.FriendShipState.FRIEND);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), ERROR_REQUEST, Toast.LENGTH_SHORT).show();
                            }
                        });
                        request.setTag(VOLLEY_REQUEST_TAG);
                        requestQueue.add(request);
                    }
                });
                ignoreRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject object = new JSONObject();
                        //// TODO: 08/03/17 fill the json request
                        request = new JsonObjectRequest(Request.Method.POST, Constants.IGNORE_REQUEST, object, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                linearLayout.setVisibility(View.GONE);
                                FriendState.setVisibility(View.VISIBLE);
                                FriendState.setBackgroundResource(R.drawable.button_shape_add_friend);
                                profile.setState(Profile.FriendShipState.NOT_FRIEND);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), ERROR_REQUEST, Toast.LENGTH_SHORT).show();
                            }
                        });
                        request.setTag(VOLLEY_REQUEST_TAG);
                        requestQueue.add(request);
                    }
                });
        }
    }

    @Override
    public void onDestroyView() {
        requestQueue.cancelAll(VOLLEY_REQUEST_TAG);
        super.onDestroyView();
    }
}
