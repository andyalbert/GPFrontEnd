package com.project.locateme.mainViews.homeFragment;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.R;

import java.util.ArrayList;

/**
 * @author Andrew
 * @since 25/1/2017
 * @version 1.0
 *
 * this class is responsible of rendering the events views that appear on the home fragment in the main tabs
 */

public class EventsAdapter extends ArrayAdapter<Event> {
    private Context context;
    private ArrayList<Event> events;
    public EventsAdapter(Context context, int resource, ArrayList<Event> events) {
        super(context, resource);
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.list_item_event, null);
            holder = new ViewHolder();

            holder.date = (TextView)convertView.findViewById(R.id.list_item_event_time);
            holder.location = (TextView)convertView.findViewById(R.id.list_item_event_location);
            holder.name = (TextView)convertView.findViewById(R.id.list_item_event_name);
            holder.image = (ImageView)convertView.findViewById(R.id.list_item_event_image);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.date.setText(events.get(position).getDateOfEvent().toString());
        holder.name.setText(events.get(position).getName());
        //TODO : Update Models
//        holder.location.setText(events.get(position).getLocation().getName());
//        if(events.get(position).getImageURL() != null) //first, check if it exists
//            Glide.with(context).load(events.get(position).getImageURL()).into(holder.image);
        return convertView;
    }
    class ViewHolder{
        public TextView date;
        public TextView location;
        public TextView name;
        public ImageView image;
    }
}
