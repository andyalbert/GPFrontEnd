package com.project.locateme.mainViews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;
import com.project.locateme.HolderActivity;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.locationManager.Area;
import com.project.locateme.dataHolder.locationManager.Location;
import com.project.locateme.utilities.Constants;

import java.sql.Timestamp;
import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * @author Andrew
 * @since 25/1/2017
 * @version 1.0
 */
public class SettingsFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        Log.e("tokk" ,FirebaseInstanceId.getInstance().getToken());

        //// TODO: 1/28/2017 remove this, it's just for testing
        {
            Button b = (Button) view.findViewById(R.id.button2);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), HolderActivity.class);
                    Location loc = new Location(1.2 , 2.2 , "AAA");
                    loc.setId("1");
                    Area area = new Area();
                    area.setId("1");
                    area.setRadius(222.50);
                    area.setImageURL("https://firebasestorage.googleapis.com/v0/b/locateme-f9c41.appspot.com/o/final%20edit?alt=media&token=83ce548c-2193-43cb-9955-60272cac3080");
                    area.setLocation(loc);
                    Event event = new Event();
                    event.setArea(area);
                    event.setId("2");
                    event.setDescription("A5 5ara");
                    event.setState(true);
                    event.setDateOfEvent(new Timestamp(1244512154));
                    event.setDeadline(new Timestamp(544546));
                    HashMap<String , Object> params = new HashMap<String, Object>();
                    params.put("eventModel" , event);
                    intent.putExtra(getActivity().getString(R.string.fragment_name), Constants.EVENT_FRAGMENT);
                    intent.putExtra(Constants.HASHMAP, params);
                    startActivity(intent);
                }
            });
        }
        return view;
    }
}
