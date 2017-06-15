package com.project.locateme.dataHolder.NotificationManger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.locateme.HolderActivity;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.fragments.EventFragment;
import com.project.locateme.mainViews.NotificationFragment;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author andrew
 * @since 13/6/2017
 * @version 1.1
 */

public class EventInvitation extends Notification {
    private Event event;
    private EventFragment.UserState userState;

    public EventFragment.UserState getUserState() {
        return userState;
    }

    public void setUserState(EventFragment.UserState userState) {
        this.userState = userState;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


    @Override
    public void setViewListener(NotificationFragment.ViewHolder holder, final Context context) {
        Glide.with(context).load(event.getArea().getImageURL()).into(holder.image);

        //add bold name to the text, using android native class
        String eventString = context.getString(R.string.event_invitation);
        SpannableStringBuilder str = new SpannableStringBuilder(eventString + " " + event.getName());
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), eventString.length(), eventString.length() + event.getName().length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.text.setText(str);
        holder.time.setText(General.convertTimeatampToString(getTimestamp()));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HolderActivity.class);
                intent.putExtra(Constants.HASHMAP, new HashMap(){
                    {
                        put("eventModel", event);
                        put("userStatus", userState);
                    }
                });
                intent.putExtra(context.getString(R.string.fragment_name), Constants.EVENT_FRAGMENT);
                context.startActivity(intent);
            }
        });
    }
}
