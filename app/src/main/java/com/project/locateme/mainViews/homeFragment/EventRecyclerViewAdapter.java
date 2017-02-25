package com.project.locateme.mainViews.homeFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;

import java.util.ArrayList;

/**
 * @author Andrew
 * @version 1.0
 * @since 28/1/2017
 */

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Event> events;

    public EventRecyclerViewAdapter(Context context, ArrayList<Event> events){
        this.events = events;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.date.setText(events.get(position).getDateOfEvent().toString());
        viewHolder.name.setText(events.get(position).getName());
     //   viewHolder.location.setText(events.get(position).getLocation().getName());
        if(events.get(position).getImageURL() != null) //first, check if it exists
            Glide.with(context).load(events.get(position).getImageURL()).into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView date;
        public TextView location;
        public TextView name;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView)itemView.findViewById(R.id.list_item_event_time);
            location = (TextView)itemView.findViewById(R.id.list_item_event_location);
            name = (TextView)itemView.findViewById(R.id.list_item_event_name);
            image = (ImageView)itemView.findViewById(R.id.list_item_event_image);
        }
    }
}
