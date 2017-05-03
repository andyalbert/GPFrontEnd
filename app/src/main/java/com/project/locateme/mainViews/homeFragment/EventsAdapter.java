package com.project.locateme.mainViews.homeFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.locateme.HolderActivity;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.R;
import com.project.locateme.fragments.EventFragment;
import com.project.locateme.utilities.Constants;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Andrew
 * @since 25/1/2017
 * @version 2.1
 *
 * this class is responsible of rendering the events views that appear on the home fragment in the main tabs, and in the all events
 * fragment
 */

public class EventsAdapter extends ArrayAdapter<Event> {
    private Context context;
    private ArrayList<Pair<Event, EventFragment.UserState>> events;
    private boolean moreExists;
    private EventList eventList;
    private int VIEW_COUNTS;

    @Override
    public int getViewTypeCount() {
        return VIEW_COUNTS;
    }

    @Override
    public int getItemViewType(int position) {
        return eventList == EventList.SMALL && position == events.size() ? 1 : 0;
    }

    public enum EventList{
        SMALL, LARGE
    }

    public EventsAdapter(Context context, int resource, ArrayList<Pair<Event, EventFragment.UserState>> events, boolean moreExists, EventList eventList){
        super(context, resource);
        this.context = context;
        this.events = events;
        this.moreExists = moreExists;
        this.eventList = eventList;
        if(moreExists)
            VIEW_COUNTS = 2;
        else
            VIEW_COUNTS = 1;
    }

    public void setData(ArrayList<Pair<Event, EventFragment.UserState>> events){
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size() + (moreExists ? 1 : 0);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            if(eventList == EventList.SMALL && position == events.size()){
                convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.list_item_see_more, null);
                MoreViewHolder holder = new MoreViewHolder(convertView);
                convertView.setTag(holder);
                setListener(holder);
            } else{
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_event, parent, false);
                ViewHolder holder = new ViewHolder(convertView);
                convertView.setTag(holder);
                setEventDetailsAndListener(holder, position, convertView);
            }
        } else if(eventList == EventList.SMALL && position == events.size()){
            setListener((MoreViewHolder) convertView.getTag());
        } else {
            setEventDetailsAndListener((ViewHolder) convertView.getTag(), position, convertView);
        }
        return convertView;
    }

    private void setEventDetailsAndListener(ViewHolder holder, final int position, View view) {
        holder.date.setText(events.get(position).first.getDateOfEvent().toString());
        holder.name.setText(events.get(position).first.getName());
        //if(events.get(position).first.getDeadline().compareTo())
           // view.setBackgroundColor(context.getResources().getColor(R.color.white));
        if(events.get(position).second.equals(EventFragment.UserState.OWNER))
            holder.name.append("[Owner]");
        if(events.get(position).first.getArea().getImageURL() != null) //first, check if it exists
            Glide.with(context).load(events.get(position).first.getArea().getImageURL()).into(holder.image);
        holder.location.setText(events.get(position).first.getArea().getLocation().getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HolderActivity.class);
                intent.putExtra(context.getString(R.string.fragment_name), Constants.EVENT_FRAGMENT);
                intent.putExtra(Constants.HASHMAP, new HashMap<String, Object>(){
                    {
                        put("eventModel", events.get(position).first);
                        put("userStatus", events.get(position).second);
                    }
                });
                context.startActivity(intent);
            }
        });
    }

    private void setListener(MoreViewHolder holder) {
        holder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HolderActivity.class);
                intent.putExtra(context.getString(R.string.fragment_name), Constants.ALL_EVENTS_FRAGMENT);
                intent.putExtra(Constants.HASHMAP, new HashMap<>());
                context.startActivity(intent);
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
