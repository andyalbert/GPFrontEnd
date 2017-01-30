package com.project.locateme.fragments.allFriendsFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.mainViews.homeFragment.FriendsAdapter;
import com.project.locateme.R;
import com.project.locateme.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Andrew
 * @since 26/1/2017
 * @version 1.0
 */

public class AllFriendsFragment extends Fragment {
    private View view;
//    private ArrayList<Profile> profiles;
//    private ArrayAdapter<Profile> profileArrayAdapter;
//    private RequestQueue requestQueue;
//    @BindView(R.id.fragment_all_friends_list)
//    ListView friendsListView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.list_item_remove_friend, container, false);
        ButterKnife.bind(this, view);
        //parameters = (HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP);, not needed any more
        //loadFriendsProfiles();

        return view;
    }

//    private void loadFriendsProfiles() {
//        JSONObject json = new JSONObject();
//        SharedPreferences pref = getActivity().getSharedPreferences((getString(R.string.shared_preferences_name)), Context.MODE_PRIVATE);
//        try {
//            json.put("id", pref.getString(getString(R.string.user_id), ""));
//            json.put("pass", pref.getString(getString(R.string.user_password), ""));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.GET_ALL_FRIENDS, json, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                //todo deal with data, careful as it's json object not array
//                setFriendsListView();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        jsonObjectRequest.setTag("all_friends");
//        requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(jsonObjectRequest);
//    }
//
//    @Override
//    public void onDestroyView() {
//        if(requestQueue != null)
//            requestQueue.cancelAll("all_friends");
//        super.onDestroyView();
//    }
//
//    private void setFriendsListView() {
//        profileArrayAdapter = new FriendsAdapter(getActivity(),R.layout.list_item_friend, profiles, FriendsAdapter.usage.FULL_LIST);
//        friendsListView.setAdapter(profileArrayAdapter);
//    }
}
