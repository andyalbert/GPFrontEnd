package com.project.locateme.dataHolder.NotificationManger;

import android.content.Context;
import android.view.View;

import com.project.locateme.dataHolder.locationManager.Area;
import com.project.locateme.mainViews.NotificationFragment;

/**
 * @author andrew
 * @since 13/6/2017
 * @version 1.0
 */

public class LeaveArea extends Notification {
    private String personName;
    private Area area;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public void setViewListener(NotificationFragment.ViewHolder viewHolder, Context context) {

    }
}
