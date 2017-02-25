package com.project.locateme.mainViews.homeFragment;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.locationManager.Area;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author andrew
 * @version 1.0
 * @since 8/2/2017
 */

public class ZonesAdapter extends ArrayAdapter<Event> {
    private ArrayList<Area> areas;
    private Context context;
    public ZonesAdapter(Context context, int resource, ArrayList<Area> areas) {
        super(context, resource);
        this.areas = areas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return areas.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.list_item_zone, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();
      //  holder.zoneName.setText(areas.get(position).get);

        return convertView;
    }
    class ViewHolder{
        @BindView(R.id.list_item_zone_assigned_number)
        TextView assignedNumber;
        @BindView(R.id.list_item_zone_name)
        TextView zoneName;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
