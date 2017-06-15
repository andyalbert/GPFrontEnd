package com.project.locateme.dataHolder.NotificationManger;

import android.content.Context;
import android.view.View;

import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.eventsManager.Suggestion;
import com.project.locateme.mainViews.NotificationFragment;

/**
 * @author andrew
 * @since 13/6/2017
 * @version 1.0
 */

public class EventEditing extends Notification {
    private Suggestion suggestion;
    private Event event;

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
    public void setViewListener(NotificationFragment.ViewHolder viewHolder, Context context) {

    }
}
