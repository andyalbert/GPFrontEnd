package com.project.locateme.mainViews.homeFragment;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.locateme.dateHolder.userManagement.Profile;
import com.project.locateme.R;

import java.util.ArrayList;

/**
 * Created by Andrew on 1/26/2017.
 */

public class FriendsAdapter extends ArrayAdapter<Profile> {
    private ArrayList<Profile> profiles;
    private Context context;
    private usage use;
    public enum usage{
        SMALL_LIST, FULL_LIST
    }
    /**
     * @param context
     * @param resource
     * @param profiles
     * @param use this parameter is used to identify either it's used for the friends on the list or the full list, as the small
     *              one has an extra view (see more), while the other doesn't
     */
    public FriendsAdapter(Context context, int resource, ArrayList<Profile> profiles, usage use) {
        super(context, resource);
        this.profiles = profiles;
        this.context = context;
        this.use = use;
    }

    @Override
    public int getCount() {
        return profiles.size() + (use == usage.SMALL_LIST ? 1 : 0);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position == profiles.size() && use == usage.SMALL_LIST){
            EmptyViewHolder holder;
            if(convertView == null){
                convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.list_view_see_more, null);
                holder = new EmptyViewHolder();
                holder.seeMore = (TextView) convertView.findViewById(R.id.list_view_see_more_text);
                convertView.setTag(holder);
            } else
                holder = (EmptyViewHolder) convertView.getTag();
            holder.seeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //// TODO: 1/26/2017 this should link to the activity containing all the friends of the user
                }
            });
        } else{
            ProfileViewHolder holder;
            if(convertView == null){
                holder = new ProfileViewHolder();
                convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.list_view_friend, null);
                holder.name = (TextView) convertView.findViewById(R.id.list_view_friend_name);
                holder.time = (TextView) convertView.findViewById(R.id.list_view_friend_last_update_time);
                holder.image = (ImageView) convertView.findViewById(R.id.list_view_friend_image);
                convertView.setTag(holder);
            }
            else
                holder = (ProfileViewHolder) convertView.getTag();
            holder.name.setText(profiles.get(position).getName());
            // holder.time.setText(profiles.get(position).get); // TODO: 1/26/2017 please fix this as soon as possible
            Glide.with(context).load(profiles.get(position).getPictureURL()).into(holder.image);
        }
        return convertView;
    }
    class ProfileViewHolder {
        public TextView name;
        public TextView time;
        public ImageView image;
    }
    class EmptyViewHolder{
        public TextView seeMore;
    }
}
