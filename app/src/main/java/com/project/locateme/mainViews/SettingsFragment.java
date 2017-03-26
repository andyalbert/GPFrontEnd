package com.project.locateme.mainViews;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.google.firebase.iid.FirebaseInstanceId;
import com.project.locateme.HolderActivity;
import com.project.locateme.R;
import com.project.locateme.customViews.testActivity;
import com.project.locateme.utilities.Constants;

import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * @author Andrew
 * @version 1.0
 * @since 25/1/2017
 */
public class SettingsFragment extends Fragment {
    private View view;
    //// TODO: 20/03/17 add an option to allow the user to edit the update time

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                    intent.putExtra(getActivity().getString(R.string.fragment_name), Constants.CREATE_EVENT_FRAGMENT);
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
            view.findViewById(R.id.fragment_settings_view_friends).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), HolderActivity.class);
                    intent.putExtra(getString(R.string.fragment_name), Constants.ALL_FRIENDS_FRAGMENT);
                    intent.putExtra(Constants.HASHMAP, new HashMap<>());
                    startActivity(intent);
                }
            });
            view.findViewById(R.id.fragment_settings_create_place).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), HolderActivity.class);
                    intent.putExtra(getString(R.string.fragment_name), Constants.CREATE_PLACE_FRAGMENT);
                    intent.putExtra(Constants.HASHMAP, new HashMap<>());
                    startActivity(intent);
                }
            });
        }

        return view;
    }

    public void test(){

    }
}
