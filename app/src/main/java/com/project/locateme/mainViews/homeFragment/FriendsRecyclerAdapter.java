package com.project.locateme.mainViews.homeFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.locateme.HolderActivity;
import com.project.locateme.R;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.utilities.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Andrew
 * @since 28/1/2017
 * @version 1.0
 */

public class FriendsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private ArrayList<Profile> profiles;
    private Context context;
    private usage use;
    public enum usage{
        SMALL_LIST, FULL_LIST
    }
    private final int FRIEND = 0, SEE_MORE = 1;

    public FriendsRecyclerAdapter(Context context, ArrayList<Profile> profiles, usage use){
        this.profiles = profiles;
        this.use = use;
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case FRIEND:
                View view = inflater.inflate(R.layout.list_view_friend, null); //can be parent, but cause error as it already has a parent ?!
                holder = new ProfileViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.list_view_see_more, null);//same here
                holder = new EmptyViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case FRIEND:
                ProfileViewHolder profileViewHolder = (ProfileViewHolder) holder;
                profileViewHolder.name.setText(profiles.get(position).getName());
                // holder.time.setText(profiles.get(position).get); // TODO: 1/26/2017 please fix this as soon as possible
                Glide.with(context).load(profiles.get(position).getPictureURL()).into(profileViewHolder.image);
                break;
            default:
                EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
                emptyViewHolder.seeMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, HolderActivity.class);
                        intent.putExtra(context.getString(R.string.fragment_name), Constants.ALL_FRIENDS_FRAGMENT);
                        intent.putExtra(Constants.HASHMAP, new HashMap<String, Object>());
                        context.startActivity(intent);
                    }
                });
        }
    }

    @Override
    public int getItemCount() {
        return profiles.size() + (use == usage.FULL_LIST ? 0 : 1);
    }

    @Override
    public int getItemViewType(int position) {
        if(use == usage.FULL_LIST)
            return FRIEND;
        if(position < profiles.size())
            return FRIEND;
        return SEE_MORE;
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView time;
        public ImageView image;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.list_view_friend_name);
            time = (TextView) itemView.findViewById(R.id.list_view_friend_last_update_time);
            image = (ImageView) itemView.findViewById(R.id.list_view_friend_image);
        }
    }
    class EmptyViewHolder extends RecyclerView.ViewHolder{
        public TextView seeMore;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            seeMore = (TextView) itemView.findViewById(R.id.list_view_see_more_text);
        }
    }
}
