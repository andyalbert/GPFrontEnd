package com.project.locateme.dataHolder.NotificationManger;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.project.locateme.mainViews.NotificationFragment;

import java.sql.Timestamp;

/**
 * @author andrew
 * @since 13/6/2017
 * @version 1.2
 */

public abstract class Notification {
    private Timestamp timestamp;
    private int id;
    private boolean isRead;

    public Notification(){
        isRead = false;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract void setViewListener(NotificationFragment.ViewHolder viewHolder, Context context);
}
