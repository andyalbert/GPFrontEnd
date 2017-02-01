package com.project.locateme.mainViews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.project.locateme.HolderActivity;
import com.project.locateme.R;
import com.project.locateme.googleMap.AddZoneFragment;
import com.project.locateme.utilities.Constants;

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

        //// TODO: 1/28/2017 remove this, it's just for testing
        {
            Button b = (Button) view.findViewById(R.id.button2);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), HolderActivity.class);
                    intent.putExtra(getActivity().getString(R.string.fragment_name), Constants.EVENT_FRAGMENT);
                    intent.putExtra(Constants.HASHMAP, new HashMap<>());
                    startActivity(intent);
                }
            });
            Button b1 = (Button) view.findViewById(R.id.mapbutton);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), HolderActivity.class);
                    intent.putExtra(getString(R.string.fragment_name), Constants.USER_LOCATION_FRAGMENT);
                    HashMap<String, Object> parameters = new HashMap<>();
                    parameters.put("lat", 29.6);
                    parameters.put("lon", 33.95);
                    parameters.put("name", "andrew");
                    intent.putExtra(Constants.HASHMAP, parameters);
                    startActivity(intent);
                }
            });
            ((Button) view.findViewById(R.id.map_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(getActivity(), HolderActivity.class).putExtra(getString(R.string.fragment_name), Constants.SELECT_ZONE_FRAGMENT));
                }
            });
        }
        return view;
    }
}