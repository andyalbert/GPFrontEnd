package com.project.locateme.fragments.allFriendsFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.mainViews.homeFragment.FriendsAdapter;
import com.project.locateme.R;
import com.project.locateme.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Andrew
 * @since 26/1/2017
 * @version 1.0
 */

public class AllFriendsFragment extends Fragment {
    private View view;
    private ArrayList<Profile> profiles;
    private ArrayAdapter<Profile> profileArrayAdapter;
    private RequestQueue requestQueue;
    private SharedPreferences pref;
    private StringRequest request;
    private final String VOLLEY_TAG = "allFriendsList";
    @BindView(R.id.fragment_all_friends_list)
    ListView friendsListView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_all_friends, container, false);
        ButterKnife.bind(this, view);
        pref = getActivity().getSharedPreferences((getString(R.string.shared_preferences_name)), Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(getActivity());
        loadFriendsProfiles();

        return view;
    }

    private void loadFriendsProfiles() {
        Uri uri = Uri.parse(Constants.GET_ALL_FRIENDS).buildUpon()
                .appendQueryParameter("userid", pref.getString(getString(R.string.user_id), ""))
                .appendQueryParameter("pass", pref.getString(getString(R.string.user_password), ""))
                .build();
        profiles = new ArrayList<>();

        request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray array = new JSONArray(response);
                    JSONObject object;
                    Profile profile;
                    for(int i = 0; i < array.length();i++){
                        profile = new Profile();
                        object = array.getJSONObject(i);

//                        profile.setBirthday(object.getString("birthday"));
                        profile.setState(Profile.FriendShipState.FRIEND);
                        profile.setName(object.getString("name"));
                        profile.setEmail(object.getString("email"));
                        profile.setPictureURL(object.getString("pictureURL"));
                        profile.setUserId(object.getInt("user_Id"));
                        profile.setHomeTown(object.getString("homeTown"));
                        profile.setFirstName(object.getString("firstName"));
                        profile.setLastName(object.getString("lastName"));
                        profiles.add(profile);
                    }
                    setFriendsListView();
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error loading, please try again later", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(VOLLEY_TAG);
        requestQueue.add(request);
    }

    @Override
    public void onDestroyView() {
        if(requestQueue != null)
            requestQueue.cancelAll(VOLLEY_TAG);
        super.onDestroyView();
    }

    private void setFriendsListView() {
        profileArrayAdapter = new FriendsAdapter(getActivity(),R.layout.list_item_friend, profiles);
        friendsListView.setAdapter(profileArrayAdapter);
    }
}
