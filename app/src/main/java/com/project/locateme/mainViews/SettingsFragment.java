package com.project.locateme.mainViews;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.project.locateme.HolderActivity;
import com.project.locateme.R;
import com.project.locateme.customViews.testActivity;
import com.project.locateme.googleMap.AddZoneFragment;
import com.project.locateme.updatingUserLocation.GPSAndInternetStateChangeReceiver;
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
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                    double lat = 29.6, lon = 33.95;
                    HashMap<String, Object> parameters = new HashMap<>();
                    parameters.put("lat", lat);
                    parameters.put("lon", lon);
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
            view.findViewById(R.id.test_anim).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), testActivity.class));
                  //  getActivity().overridePendingTransition(R.anim.enter_back_pressed, R.anim.exit_back_pressed);
                }
            });
            view.findViewById(R.id.view_zone_fragment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), HolderActivity.class);
                    intent.putExtra(getString(R.string.fragment_name), Constants.VIEW_ZONE_FRAGMENT);
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    double lat = 12.2, lon = 34, rad = 5256;
                    params.put("lat", lat);
                    params.put("long", lon);
                    params.put("title", "hi");
                    params.put("radius", rad);

                    intent.putExtra(Constants.HASHMAP, params);
                    startActivity(intent);
                }
            });
        }

        //// TODO: 01/02/17 this is just temporary, it must be placed well and somewhere else
        ((SwitchCompat) view.findViewById(R.id.fragment_settings_switch_incognito)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PackageManager pm  = getActivity().getPackageManager();
                ComponentName componentName = new ComponentName(getActivity(), GPSAndInternetStateChangeReceiver.class);
                if(b)
                    pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                else
                    pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,                                 PackageManager.DONT_KILL_APP);
            }
        });
        return view;
    }
}
