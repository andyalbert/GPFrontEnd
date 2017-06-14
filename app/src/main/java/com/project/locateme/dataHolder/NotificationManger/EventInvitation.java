package com.project.locateme.dataHolder.NotificationManger;

import android.content.Context;
import android.view.View;

import com.project.locateme.dataHolder.eventsManager.Event;

/**
 * @author andrew
 * @since 13/6/2017
 * @version 1.0
 */

public class EventInvitation extends Notification {
    private Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public View getView(Context context) {
        return null;
    }

    @Override
    public void setViewTag(View convertView) {

    }

    @Override
    public void setViewListener(View convertView) {

    }
}
