package com.project.locateme.dataHolder.NotificationManger;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.bumptech.glide.Glide;
import com.project.locateme.HolderActivity;
import com.project.locateme.R;
import com.project.locateme.dataHolder.locationManager.Area;
import com.project.locateme.mainViews.NotificationFragment;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import java.util.HashMap;

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
    public void setViewListener(NotificationFragment.ViewHolder viewHolder, final Context context) {
        viewHolder.time.setText(General.convertTimeatampToString(getTimestamp()));
        Glide.with(context).load(area.getImageURL()).into(viewHolder.image);
        SpannableStringBuilder str = new SpannableStringBuilder(personName + " " + context.getString(R.string.enter_area));
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, personName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.text.setText(str);

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HolderActivity.class);
                intent.putExtra(Constants.HASHMAP, new HashMap() {{
                    put("area", area);
                }});
                intent.putExtra(context.getString(R.string.fragment_name), Constants.VIEW_ZONE_FRAGMENT);
                context.startActivity(intent);
            }
        });
    }
}
