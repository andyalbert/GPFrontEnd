package com.project.locateme.mainViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.locateme.R;
import com.project.locateme.dataHolder.userManagement.Profile;

import java.net.ConnectException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author andrew
 * @version 1.0
 * @since 13/3/2017
 */

public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Profile> profiles;
    private SparseArray<Profile> selectedUsers;

    public FriendsAdapter(Context context, ArrayList<Profile> profiles) {
        this.profiles = profiles;
        this.context = context;
        selectedUsers = new SparseArray<>();
    }

    public SparseArray<Profile> getSelectedUsers(){
        return selectedUsers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_place_friend, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        //// TODO: 14/03/17 uncomment this when the shitty image error is done
       // Glide.with(context).load(profiles.get(position).getPictureURL()).into(viewHolder.friendImage);
        viewHolder.friendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedUsers.get(profiles.get(viewHolder.getAdapterPosition()).getUserId()) == null){ //key not found, so add it
                    viewHolder.selectedImageView.setVisibility(View.VISIBLE);
                    selectedUsers.put(profiles.get(viewHolder.getAdapterPosition()).getUserId(), profiles.get(viewHolder.getAdapterPosition()));
                } else {
                    viewHolder.selectedImageView.setVisibility(View.INVISIBLE);
                   selectedUsers.remove(profiles.get(viewHolder.getAdapterPosition()).getUserId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.list_item_place_friend_image)
        CircleImageView friendImage;
        @BindView(R.id.list_item_place_friend_selected)
        CircleImageView selectedImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
