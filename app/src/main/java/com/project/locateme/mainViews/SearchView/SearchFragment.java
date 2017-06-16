package com.project.locateme.mainViews.SearchView;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.locateme.HolderActivity;
import com.project.locateme.R;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.mainViews.MainUserActivity;
import com.project.locateme.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.http.POST;

/**
 * Created by abdo on 2/2/17.
 */

public class SearchFragment extends Fragment implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener
{
    private SearchView search;
    private MyExpandableListViewAdapter listViewAdapter;
    private ExpandableListView searchList;
    private SearchGroup searchGroup = new SearchGroup();
    private StringRequest request;
    private RequestQueue queue;
    private String searchQuery;
    private HashMap<String, Object> parameters;
    private ArrayList<Profile> resultProfiles = new ArrayList<>();
    private SharedPreferences preferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(getActivity());
        return inflater.inflate(R.layout.search_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

    }
    private void initView(View view)
    {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) view.findViewById(R.id.search);
        search.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);
        search.setSubmitButtonEnabled(true);
        displayList();

        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                searchList.collapseGroup(0);
            }
        });
        searchList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                //TODO : Send the selected Profile to the User Profile Page
                Profile intentProfile = (Profile) listViewAdapter.getChild(groupPosition , childPosition);
                Log.e("childpos" , String.valueOf(childPosition));
                getUserDetailsRequest(intentProfile.getUserId());
                return true;
            }
        });
    }
    private void displayList()
    {
        loadData();
        searchList = (ExpandableListView) getActivity().findViewById(R.id.expandableList);
        listViewAdapter = new MyExpandableListViewAdapter(getActivity(),searchGroup);
        searchList.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
        listViewAdapter.filterData(searchQuery , resultProfiles);
        searchList.expandGroup(0);
        Log.e("count" , String.valueOf(listViewAdapter.getChildrenCount(0)));
    }
    private void loadData()
    {
        //TODO : Get from Backend
        parameters = (HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP);
        searchQuery =(String) parameters.get("searchTerm");
        Log.e("Here" , searchQuery);
        searchNetworkRequest(searchQuery);
//        ArrayList<Profile> friendsProfiles = new ArrayList<>(); // TODO : get Friends Profiles
//        Profile profile = new Profile();
//        profile.setFirstName("Abd El-Rahman");
//        profile.setLastName("Ahmed");
//        friendsProfiles.add(profile);
//        searchGroup.setArr(friendsProfiles);
    }

    @Override
    public boolean onClose() {
        listViewAdapter.filterData("" , new ArrayList<Profile>());
        searchList.expandGroup(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //TODO : Get from Backend
        searchNetworkRequest(query);
        listViewAdapter.filterData(query ,resultProfiles);
        listViewAdapter.notifyDataSetChanged();
        searchList.expandGroup(0);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        listViewAdapter.filterData(query , resultProfiles);
        searchList.expandGroup(0);
        return true;
    }
    public void searchNetworkRequest( final String query){
        Uri uri = Uri.parse(Constants.SEARCH_USER_BY_NAME).buildUpon()
                .appendQueryParameter("name" ,query)
                .build();
        request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                resultProfiles = new ArrayList<>();
                try {
                    JSONArray resultArr = new JSONArray(response);
                    for ( int i=0 ; i<resultArr.length() ; i++){
                        Profile profile = new Profile();
                        JSONObject iterator = resultArr.getJSONObject(i);
                        profile.setUserId(iterator.getInt("user_Id"));
                        profile.setFirstName(iterator.getString("firstName"));
                        profile.setLastName(iterator.getString("lastName"));
                        profile.setEmail(iterator.getString("email"));
                        profile.setHomeTown(iterator.getString("homeTown"));
                        profile.setName(iterator.getString("name"));
                        profile.setBirthday(iterator.getString("birthday"));
                        profile.setPictureURL(iterator.getString("pictureURL"));
                        resultProfiles.add(profile);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                searchGroup.setArr(resultProfiles);
                listViewAdapter.filterData(query ,resultProfiles);
                listViewAdapter.notifyDataSetChanged();
                searchList.expandGroup(0);
                Log.e("Size" ,String.valueOf(resultProfiles.size()) );
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);

    }
    public void getUserDetailsRequest(final int targetId){
        preferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        Uri uri = Uri.parse(Constants.GET_USER_DETAILS).buildUpon()
                .appendQueryParameter("userid1" ,String.valueOf(targetId))
                .appendQueryParameter("userid2" , preferences.getString(getString(R.string.user_id) , ""))
                .build();
        StringRequest request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Profile profile = new Profile();
                try {
                    JSONObject iterator = new JSONObject(response);
                    profile.setUserId(iterator.getInt("user_Id"));
                    profile.setFirstName(iterator.getString("firstName"));
                    profile.setLastName(iterator.getString("lastName"));
                    profile.setEmail(iterator.getString("email"));
                    profile.setHomeTown(iterator.getString("homeTown"));
                    profile.setName(iterator.getString("name"));
                    profile.setBirthday(iterator.getString("birthday"));
                    profile.setPictureURL(iterator.getString("pictureURL"));
                    int friendshipStatus = iterator.getInt("friendShipStatus");
                    switch (friendshipStatus){
                        case 1 : profile.setState(Profile.FriendShipState.FRIEND );
                        case 2 : profile.setState(Profile.FriendShipState.ADD_REQUEST);
                        case 3 : profile.setState(Profile.FriendShipState.PENDING_REQUEST );
                        case 4 : profile.setState(Profile.FriendShipState.NOT_FRIEND );
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(getActivity(), HolderActivity.class);
                intent.putExtra(getString(R.string.fragment_name), Constants.PROFILE_FRAGMENT);
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("profile" ,profile);
                Log.e("state",String.valueOf(profile.getState()));
                intent.putExtra(Constants.HASHMAP , parameters);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }
}
