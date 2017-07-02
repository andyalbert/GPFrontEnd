package com.project.locateme.dataHolder.NotificationManger;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.android.volley.toolbox.StringRequest;
import com.project.locateme.R;
import com.project.locateme.mainViews.NotificationFragment;
import com.project.locateme.utilities.Constants;

/**
 * @author andrew
 * @since 30/6/2017
 * @version 1.0
 */

public class EventDeletion extends Notification {
    private String eventName;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public void setViewListener(NotificationFragment.ViewHolder viewHolder, Context context) {
        SpannableStringBuilder str = new SpannableStringBuilder(eventName + " has been deleted");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, eventName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        viewHolder.text.setText(str);
        viewHolder.time.setText(getTimestamp().toString());
        viewHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.image_event_large));

    }
}
