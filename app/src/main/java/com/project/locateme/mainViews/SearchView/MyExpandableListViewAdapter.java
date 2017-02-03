package com.project.locateme.mainViews.SearchView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.project.locateme.R;
import com.project.locateme.dataHolder.userManagement.Profile;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by abdo on 2/2/17.
 */

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private SearchGroup searchGroup = new SearchGroup();
    private SearchGroup originalSearchGroup = new SearchGroup();

    public MyExpandableListViewAdapter(Context context, SearchGroup searchGroup) {
        this.context = context;
        this.searchGroup = searchGroup;
        this.originalSearchGroup = searchGroup;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return searchGroup.getArr().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return searchGroup;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return searchGroup.getArr().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.parent_row_searchview,null);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if(convertView==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child_row_searchview,null);
        }
        CircleImageView user_profile_pic = (CircleImageView) convertView.findViewById(R.id.user_profile_pic);
        TextView user_first_name = (TextView) convertView.findViewById(R.id.user_first_name);
        TextView user_last_name = (TextView) convertView.findViewById(R.id.user_last_name);
        user_profile_pic.setImageResource(R.mipmap.ic_profile); // TODO : Load the image profile pic from the database
        user_first_name.setText(searchGroup.getArr().get(childPosition).getFirstName()); // TODO : Load the first name from the database
        user_last_name.setText(searchGroup.getArr().get(childPosition).getLastName()); // TODO : Load the last name from the database
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    public void filterData(String query)
    {
        query = query.toLowerCase();
        if(query.isEmpty())
        {
            searchGroup = new SearchGroup();
            searchGroup = originalSearchGroup;
        }
        else
        {
            ArrayList<Profile> oldList = originalSearchGroup.getArr();
            ArrayList<Profile> newList = new ArrayList<>();
            for(Profile profile: oldList)
            {
                if((profile.getFirstName() + " " + profile.getLastName()).contains(query))
                    newList.add(profile);
            }
            searchGroup = new SearchGroup(newList);
        }
        notifyDataSetChanged();
    }
}
