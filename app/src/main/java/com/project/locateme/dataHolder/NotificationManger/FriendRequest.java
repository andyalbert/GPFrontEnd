package com.project.locateme.dataHolder.NotificationManger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.locateme.HolderActivity;
import com.project.locateme.R;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.mainViews.NotificationFragment;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author andrew
 * @since 13/6/2017
 * @version 1.0
 */

public class FriendRequest extends Notification {
    private Profile senderProfile;

    public Profile getSenderProfile() {
        return senderProfile;
    }

    public void setSenderProfile(Profile senderProfile) {
        this.senderProfile = senderProfile;
    }


    //adapter related functions
    @Override
    public void setViewListener(NotificationFragment.ViewHolder holder, final Context context) {
        //add bold name to the text, using android native class
        SpannableStringBuilder str = new SpannableStringBuilder(senderProfile.getName() + " " + context.getString(R.string.friend_request));
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, senderProfile.getName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Glide.with(context).load(senderProfile.getPictureURL()).into(holder.image);
        holder.text.setText(str);
        holder.time.setText(General.convertTimeatampToString(getTimestamp()));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HolderActivity.class);
                intent.putExtra(Constants.HASHMAP, new HashMap(){{put("profile", senderProfile);}});
                intent.putExtra(context.getString(R.string.fragment_name), Constants.PROFILE_FRAGMENT);
                context.startActivity(intent);
            }
        });
    }

}
