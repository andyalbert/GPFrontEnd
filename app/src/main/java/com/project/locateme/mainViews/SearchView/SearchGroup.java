package com.project.locateme.mainViews.SearchView;

import com.project.locateme.dataHolder.userManagement.Profile;

import java.util.ArrayList;

/**
 * Created by abdo on 2/2/17.
 */
//ExpandableListView consists of groups and each group has children , here we have one group ( friends )
public class SearchGroup
{
    private ArrayList<Profile> arr = new ArrayList<>();

    public SearchGroup()
    {

    }

    public SearchGroup(ArrayList<Profile> arr)
    {
        this.arr = arr;
    }

    public ArrayList<Profile> getArr() {
        return arr;
    }

    public void setArr(ArrayList<Profile> arr) {
        this.arr = arr;
    }
}
