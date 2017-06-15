package com.project.locateme.dataHolder.NotificationManger;

import android.content.Context;
import android.view.View;

import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.mainViews.NotificationFragment;

/**
 * @author andrew
 * @since 13/6/2017
 * @version 1.0
 */

public class EventArrival extends Notification {
    private String personName;
    private Event event;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void setViewListener(NotificationFragment.ViewHolder viewHolder, Context context) {

    }
}
