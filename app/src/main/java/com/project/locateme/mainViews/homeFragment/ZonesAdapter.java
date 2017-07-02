package com.project.locateme.mainViews.homeFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.locateme.HolderActivity;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.locationManager.Area;
import com.project.locateme.utilities.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author andrew
 * @version 1.2
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.list_item_zone, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();
        holder.zoneName.setText(areas.get(position).getLocation().getName());
        if(!areas.get(position).getImageURL().equals("null"))
            Glide.with(context).load(areas.get(position).getImageURL()).into(holder.image);
        else
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.image_place));
        String assigned = String.valueOf(areas.get(position).getAccounts().size());
        holder.assignedNumber.setText(assigned);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HolderActivity.class);
                intent.putExtra(context.getString(R.string.fragment_name), Constants.VIEW_ZONE_FRAGMENT);
                intent.putExtra(Constants.HASHMAP, new HashMap(){{put("area", areas.get(position));}});
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void addAll(ArrayList<Area> areas) {
        this.areas.addAll(areas);
    }

    class ViewHolder{
        @BindView(R.id.list_item_zone_assigned_number)
        TextView assignedNumber;
        @BindView(R.id.list_item_zone_name)
        TextView zoneName;
        @BindView(R.id.list_item_zone_image)
        ImageView image;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
