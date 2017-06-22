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

import butterknife.BindView;
import butterknife.ButterKnife;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ProfileViewHolder holder;
        if(convertView == null){
            convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.list_item_friend, null);
            holder = new ProfileViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ProfileViewHolder) convertView.getTag();
        holder.name.setText(profiles.get(position).getName());
        //// TODO: 10/03/17 enable when images are ready on server
        Glide.with(context).load(profiles.get(position).getPictureURL()).into(holder.image);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HolderActivity.class);
                intent.putExtra(context.getString(R.string.fragment_name), Constants.PROFILE_FRAGMENT);
                intent.putExtra(Constants.HASHMAP, new HashMap(){{put("profile", profiles.get(position));}});
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ProfileViewHolder {
        @BindView(R.id.list_item_friend_name)
        TextView name;
        @BindView(R.id.list_item_friend_image)
        ImageView image;
        public ProfileViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
