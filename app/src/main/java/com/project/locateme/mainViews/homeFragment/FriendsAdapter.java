package com.project.locateme.mainViews.homeFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.locateme.HolderActivity;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.R;
import com.project.locateme.utilities.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Andrew
 * @since 26/1/2017
 * @version 1.0
 */

public class FriendsAdapter extends ArrayAdapter<Profile> {
    private ArrayList<Profile> profiles;
    private Context context;
    /**
     * @param context
     * @param resource
     * @param profiles
     * @param
     *
     */
    public FriendsAdapter(Context context, int resource, ArrayList<Profile> profiles) {
        super(context, resource);
        this.profiles = profiles;
        this.context = context;
    }

    @Override
    public int getCount() {
        return profiles.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProfileViewHolder holder;
        if(convertView == null){
            holder = new ProfileViewHolder();
            convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.list_item_friend, null);
            holder.name = (TextView) convertView.findViewById(R.id.list_item_friend_name);
            holder.time = (TextView) convertView.findViewById(R.id.list_item_friend_last_update_time);
            holder.image = (ImageView) convertView.findViewById(R.id.list_item_friend_image);
            convertView.setTag(holder);
        } else
            holder = (ProfileViewHolder) convertView.getTag();
        holder.name.setText(profiles.get(position).getName());
        // holder.time.setText(profiles.get(position).get); // TODO: 1/26/2017 please fix this as soon as possible
        Glide.with(context).load(profiles.get(position).getPictureURL()).into(holder.image);
        return convertView;
    }
    class ProfileViewHolder {
        public TextView name;
        public TextView time;
        public ImageView image;
    }
}
