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
import com.project.locateme.HolderActivity;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Andrew
 * @since 25/1/2017
 * @version 1.1
 *
 * this class is responsible of rendering the events views that appear on the home fragment in the main tabs
 */

public class EventsAdapter extends ArrayAdapter<Event> {
    private Context context;
    private ArrayList<Event> events;
    private boolean moreExists;
    private EventList eventList;
    public enum EventList{
        SMALL, LARGE
    }

    public EventsAdapter(Context context, int resource, ArrayList<Event> events, boolean moreExists, EventList eventList){
        super(context, resource);
        this.context = context;
        this.events = events;
        this.moreExists = moreExists;
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return events.size() + (moreExists ? 1 : 0);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            if(eventList == EventList.SMALL && position == 5){
                convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.list_item_see_more, null);
                MoreViewHolder holder = new MoreViewHolder(convertView);
                convertView.setTag(holder);
                setListener(holder);
            } else{
                convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.list_item_event, null);
                ViewHolder holder = new ViewHolder(convertView);
                convertView.setTag(holder);
                setEventDetails(holder, position);
            }
        } else if(eventList == EventList.SMALL && position == 5){
            setListener((MoreViewHolder) convertView.getTag());
        } else {
            setEventDetails((ViewHolder) convertView.getTag(), position);
        }
        return convertView;
    }

    private void setEventDetails(ViewHolder holder, int position) {
        holder.date.setText(events.get(position).getDateOfEvent().toString());
        holder.name.setText(events.get(position).getName());
        if(events.get(position).getArea().getImageURL() != null) //first, check if it exists
            Glide.with(context).load(events.get(position).getArea().getImageURL()).into(holder.image);
        holder.location.setText(events.get(position).getArea().getLocation().getName());
    }

    private void setListener(MoreViewHolder holder) {
        holder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //// TODO: 27/02/17 go to the fragment will all the events
            }
        });
    }

    class ViewHolder{
        @BindView(R.id.list_item_event_time)
        TextView date;
        @BindView(R.id.list_item_event_location)
        public TextView location;
        @BindView(R.id.list_item_event_name)
        public TextView name;
        @BindView(R.id.list_item_event_image)
        public ImageView image;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
    class MoreViewHolder {
        @BindView(R.id.list_item_see_more_text)
        TextView seeMore;
        public MoreViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
