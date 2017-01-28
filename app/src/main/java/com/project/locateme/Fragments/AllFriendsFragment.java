package com.project.locateme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project.locateme.dateHolder.userManagement.Profile;
import com.project.locateme.mainViews.homeFragment.FriendsAdapter;
import com.project.locateme.R;
import com.project.locateme.utilities.Constants;

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
    private HashMap<String, Object> parameters;
    @BindView(R.id.fragment_all_friends_list)
    ListView friendsListView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_all_friends, container);
        ButterKnife.bind(this, view);
        parameters = (HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP);
        //// TODO: 1/26/2017 add an option here to connect to web using bundle and get profile id and password

        setFriendsListView();
        return view;
    }

    private void setFriendsListView() {
        //get profiles from the server
        profileArrayAdapter = new FriendsAdapter(getActivity(),R.layout.list_view_friend, profiles, FriendsAdapter.usage.FULL_LIST);
        friendsListView.setAdapter(profileArrayAdapter);
    }
}
