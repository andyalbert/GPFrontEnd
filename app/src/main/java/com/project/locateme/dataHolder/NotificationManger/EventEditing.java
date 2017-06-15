package com.project.locateme.dataHolder.NotificationManger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.internal.zzand;
import com.project.locateme.HolderActivity;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.eventsManager.Suggestion;
import com.project.locateme.fragments.EventFragment;
import com.project.locateme.mainViews.NotificationFragment;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import java.util.HashMap;

/**
 * @author andrew
 * @since 13/6/2017
 * @version 1.1
 */

public class EventEditing extends Notification {
    private Suggestion suggestion;
    private Event event;
    private EventFragment.UserState userState;

    public EventFragment.UserState getUserState() {
        return userState;
    }

    public void setUserState(EventFragment.UserState userState) {
        this.userState = userState;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
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
        holder.time.setText(General.convertTimeatampToString(getTimestamp()));

        String eventString = context.getString(R.string.event_editing);
        SpannableStringBuilder str = new SpannableStringBuilder(event.getName() + eventString + General.convertTimeatampToString(suggestion.getDate()));
        str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, event.getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), event.getName().length() + eventString.length(), event.getName().length() + eventString.length() + General.convertTimeatampToString(suggestion.getDate()).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.text.setText(str);

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
