package com.project.locateme.mainViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
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
import com.project.locateme.dataHolder.locationManager.Area;
import com.project.locateme.dataHolder.locationManager.Location;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author andrew
 * @version 1.0
 * @since 13/3/2017
 */

public class CreatePlace extends Fragment {
    private View view;
    private FriendsAdapter adapter;
    private StringRequest friendsReqeust;
    private RequestQueue queue;
    private SharedPreferences preferences;
    private ArrayList<Profile> profiles;
    private final int MAP_REQUEST_CODE = 1;
    private final int IMAGE_REQUEST_CODE = 2;
    private Area area;
    private final String FRIENDS_VOLLEY_TAG = "friendsTag";
    private String imagePath;
    private SparseArray<Profile> selectedProfiles;
    @BindView(R.id.fragment_create_place_friends)
    RecyclerView friendsRecyclerView;
    @BindView(R.id.fragment_create_place_name)
    TextView placeName;
    @BindView(R.id.fragment_create_place_floating_button)
    FloatingActionButton finishButton;
    @BindView(R.id.fragment_create_place_checked_place)
    CheckedTextView mapSelected;
    @BindView(R.id.fragment_create_place_photo)
    CircleImageView photo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_create_place, container, false);
        ButterKnife.bind(this, view);
        queue = Volley.newRequestQueue(getActivity());
        preferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        LayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        friendsRecyclerView.setLayoutManager(manager);
        friendsRecyclerView.setAdapter(new FriendsAdapter(getActivity(), new ArrayList<Profile>()));
        loadUserFriends();
        area = new Area();

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Matcher matcher = Pattern.compile("\\s*").matcher(placeName.getText());
                //check that the entered name isn't just spaces
                matcher.find();
                    if(matcher.start() == 0 && matcher.end() == placeName.getText().length()){
                        Toast.makeText(getActivity(), "Please provide a valid name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                if(!mapSelected.isChecked()){
                    Toast.makeText(getActivity(), "Please select a location", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedProfiles = adapter.getSelectedUsers();
                if(selectedProfiles.size() == 0){
                    Toast.makeText(getActivity(), "Please select at least one account", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<Profile> selected = new ArrayList<Profile>(selectedProfiles.size());
                for(int i = 0;i < selectedProfiles.size();i++){
                    selected.add(selectedProfiles.valueAt(i));
                }
                area.setAccounts(selected);
                //// TODO: 14/03/17 set this after we check it first
                //area.setImageURL();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), ImagePickerActivity.class), IMAGE_REQUEST_CODE);
            }
        });

        mapSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HolderActivity.class);
                intent.putExtra(getString(R.string.fragment_name), Constants.SELECT_ZONE_FRAGMENT);
                intent.putExtra(Constants.HASHMAP, new HashMap<>());
                startActivityForResult(intent, MAP_REQUEST_CODE);            }
        });

        return view;
    }

    private void loadUserFriends() {
        Uri uri = Uri.parse(Constants.GET_ALL_FRIENDS).buildUpon()
                .appendQueryParameter("userid", preferences.getString(getString(R.string.user_id), ""))
                .appendQueryParameter("pass", preferences.getString(getString(R.string.user_password), ""))
                .build();
        friendsReqeust = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray array = new JSONArray(response);
                    Profile profile;
                    profiles = new ArrayList<>();
                    JSONObject object;
                    for(int i = 0;i < array.length();i++){
                        object = array.getJSONObject(i);
                        profile = new Profile();
                        profile.setFirstName(object.getString("firstName"));
                        profile.setLastName(object.getString("lastName"));
                        profile.setHomeTown(object.getString("homeTown"));
                        profile.setEmail(object.getString("email"));
                        profile.setName(object.getString("name"));
                        profile.setPictureURL(object.getString("pictureURL"));
                        profile.setUserId(object.getInt("user_Id"));
                        //// TODO: 14/03/17 fix the birthday
                        //profile.setBirthday(object.getString("birthday"));
                        profile.setState(Profile.FriendShipState.FRIEND);

                        profiles.add(profile);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                adapter = new FriendsAdapter(getActivity(), profiles);
                friendsRecyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "error loading your friends list, please try again later", Toast.LENGTH_SHORT).show();
            //    getActivity().finish();
            }
        });
        friendsReqeust.setTag(FRIENDS_VOLLEY_TAG);
        queue.add(friendsReqeust);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MAP_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                area.setRadius(data.getDoubleExtra("radius", 0.0));
                Location location = new Location();
                location.setLatitude(data.getDoubleExtra("lat", 0.0));
                location.setLongitude(data.getDoubleExtra("long", 0.0));
                area.setLocation(location);
                mapSelected.setChecked(true);
            }
        } else if(requestCode == IMAGE_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                imagePath = data.getStringExtra("path");
                Glide.with(getActivity()).load(imagePath).into(photo);
            }
        }
    }
}
