package com.project.locateme.mainViews.SearchView;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.project.locateme.R;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.mainViews.MainUserActivity;

import java.util.ArrayList;

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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view);
        super.onViewCreated(view, savedInstanceState);
    }
    private void initView(View view)
    {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) view.findViewById(R.id.search);
        search.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);
        displayList();
        searchList.expandGroup(0);
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                searchList.collapseGroup(0);
            }
        });
    }
    private void displayList()
    {
        loadData();
        searchList = (ExpandableListView) getActivity().findViewById(R.id.expandableList);
        listViewAdapter = new MyExpandableListViewAdapter(getActivity(),searchGroup);
        searchList.setAdapter(listViewAdapter);
    }
    private void loadData()
    {
        ArrayList<Profile> friendsProfiles = new ArrayList<>(); // TODO : get Friends Profiles
        Profile profile = new Profile();
        profile.setFirstName("Abd El-Rahman");
        profile.setLastName("Ahmed");
        friendsProfiles.add(profile);
        searchGroup.setArr(friendsProfiles);
    }

    @Override
    public boolean onClose() {
        listViewAdapter.filterData("");
        searchList.expandGroup(0);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        listViewAdapter.filterData(query);
        searchList.expandGroup(0);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        listViewAdapter.filterData(query);
        searchList.expandGroup(0);
        return false;
    }
}
