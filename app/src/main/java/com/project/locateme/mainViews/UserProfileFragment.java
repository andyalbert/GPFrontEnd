package com.project.locateme.mainViews;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.project.locateme.R;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.utilities.Constants;


import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author abdo/andrew
 * @version 2.0
 * @since 25/2/2017
 */

public class UserProfileFragment extends Fragment {
    private SharedPreferences preferences;
    private HashMap<String, Object> parameters;
//    private Account account = new Account(); // TODO : get an account from intent
    private ImageView profile_pic;
    private TextView username_in_profile;
    private Button FriendState;
    private TextView email_address;
    private TextView home_town_in_profile;
    private TextView birthdate;

    private View view;
    private LinearLayout linearLayout;
    private Profile profile;
    private StringRequest request;
    private RequestQueue requestQueue;
    private final String VOLLEY_REQUEST_TAG = "userProfileTag";
    private final String ERROR_REQUEST = "Error happens, please try again";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        preferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        parameters = (HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP);
        requestQueue = Volley.newRequestQueue(getActivity());

        profile = (Profile) parameters.get("profile");

        profile_pic = (ImageView) view.findViewById(R.id.user_profile_pic);
        Glide.with(getContext()).load(profile.getPictureURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profile_pic);
        username_in_profile = (TextView) view.findViewById(R.id.username_in_profile);
        username_in_profile.setText(profile.getName());
        FriendState = (Button) view.findViewById(R.id.FriendState);
        linearLayout = (LinearLayout) view.findViewById(R.id.fragment_user_profile_respond_linear_view);



        email_address = (TextView) view.findViewById(R.id.email_address);
        email_address.setText(profile.getEmail());

        home_town_in_profile = (TextView) view.findViewById(R.id.home_town_in_profile);
        home_town_in_profile.setText(profile.getHomeTown());

        birthdate = (TextView) view.findViewById(R.id.birthdate);
        // TODO: 17/03/17 edit this shit
        birthdate.setText(profile.getBirthday().toString());
        this.view = view;
        setUpProfileState();
        return view;
    }

    private void setUpProfileState() {
        switch (profile.getState()){
            case NONE:
                FriendState.setVisibility(View.INVISIBLE);
                break;
            case NOT_FRIEND:
                FriendState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(Constants.ADD_FRIEND).buildUpon()
                                .appendQueryParameter("userid", preferences.getString(getString(R.string.user_id), ""))
                                .appendQueryParameter("friendid", String.valueOf(profile.getUserId()))
                                .appendQueryParameter("pass", preferences.getString(getString(R.string.user_password), ""))
                                .build();

                        request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                FriendState.setText("Pending Friend Request");
                                FriendState.setBackgroundResource(R.drawable.button_shape_pending_friend_request);
                                profile.setState(Profile.FriendShipState.PENDING_REQUEST);
                                setUpProfileState();
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
                break;

            case FRIEND:
                FriendState.setBackgroundResource(R.drawable.button_shape_remove_friend);
                FriendState.setText("Remove friend");
                FriendState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(Constants.REMOVE_FRIEND).buildUpon()
                                .appendQueryParameter("userid", preferences.getString(getString(R.string.user_id), ""))
                                .appendQueryParameter("friendid", String.valueOf(profile.getUserId()))
                                .appendQueryParameter("pass", preferences.getString(getString(R.string.user_password), ""))
                                .build();

                        request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                FriendState.setBackgroundResource(R.drawable.button_shape_add_friend);
                                FriendState.setText("Add as a friend");
                                profile.setState(Profile.FriendShipState.NOT_FRIEND);
                                setUpProfileState();
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
                break;

            case ADD_REQUEST:
                FriendState.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                TextView acceptRequest = (TextView) view.
                        findViewById(R.id.fragment_user_profile_accept);
                TextView ignoreRequest = (TextView) view.findViewById(R.id.fragment_user_profile_delete);
                acceptRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(Constants.ACCEPT_REQUEST).buildUpon()
                                .appendQueryParameter("userid", preferences.getString(getString(R.string.user_id), ""))
                                .appendQueryParameter("friendid", String.valueOf(profile.getUserId()))
                                .appendQueryParameter("pass", preferences.getString(getString(R.string.user_password), ""))
                                .build();

                        request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                linearLayout.setVisibility(View.GONE);
                                FriendState.setVisibility(View.VISIBLE);
                                FriendState.setText("Remove friend");
                                FriendState.setBackgroundResource(R.drawable.button_shape_remove_friend);
                                profile.setState(Profile.FriendShipState.FRIEND);
                                setUpProfileState();
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
                        Uri uri = Uri.parse(Constants.IGNORE_REQUEST).buildUpon()
                                .appendQueryParameter("userid", preferences.getString(getString(R.string.user_id), ""))
                                .appendQueryParameter("friendid", String.valueOf(profile.getUserId()))
                                .appendQueryParameter("pass", preferences.getString(getString(R.string.user_password), ""))
                                .build();

                        request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                linearLayout.setVisibility(View.GONE);
                                FriendState.setVisibility(View.VISIBLE);
                                FriendState.setBackgroundResource(R.drawable.button_shape_add_friend);
                                profile.setState(Profile.FriendShipState.NOT_FRIEND);
                                setUpProfileState();
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
        if(requestQueue != null)
            requestQueue.cancelAll(VOLLEY_REQUEST_TAG);
        super.onDestroyView();
    }
}
