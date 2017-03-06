package com.project.locateme.mainViews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.project.locateme.R;
import com.project.locateme.dataHolder.userManagement.Account;
import com.project.locateme.dataHolder.userManagement.Profile;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by abdo on 2/25/17.
 */

public class UserProfile extends Fragment
{
    private Account account = new Account(); // TODO : get an account from intent
    private CircleImageView profile_pic;
    private TextView username_in_profile;
    private Button FriendState;
    private TextView email_address;
    private TextView home_town_in_profile;
    private TextView birthdate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_user_profile, container, false);
        Profile profile = account.getProfile();
        profile_pic = (CircleImageView) v.findViewById(R.id.user_profile_pic);
        Glide.with(getContext()).load(profile.getPictureURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profile_pic);
        username_in_profile = (TextView) v.findViewById(R.id.username_in_profile);
        username_in_profile.setText(profile.getName());
        FriendState = (Button) v.findViewById(R.id.FriendState); // TODO : Add the action and text for the button ( Add , remove , pending )
        email_address = (TextView) v.findViewById(R.id.email_address);
        email_address.setText(profile.getEmail());

        home_town_in_profile = (TextView) v.findViewById(R.id.home_town_in_profile);
        home_town_in_profile.setText(profile.getHomeTown());

        birthdate = (TextView) v.findViewById(R.id.birthdate);
        birthdate.setText(profile.getBirthday().toString());
        return v;
    }
}
