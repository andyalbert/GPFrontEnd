package com.project.locateme.mainViews;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;

import com.project.locateme.R;
import com.project.locateme.updatingUserLocation.ProviderNetworkStateBroadcastReceiver;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

/**
 * @author andrew
 * @since 20/3/2017
 * @version 1.1
 */

public class PrefFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences preferences;

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey)  {
        addPreferencesFromResource(R.xml.preferences);
    //    preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(getString(R.string.incognito_key))){
            boolean value = sharedPreferences.getBoolean(s, false);

            PackageManager pm = getActivity().getPackageManager();
            ComponentName componentName = new ComponentName(getActivity(), ProviderNetworkStateBroadcastReceiver.class);
            if (value){
                Intent intent = new Intent("Initiate");
                intent.putExtra("disable", 1);
                getActivity().sendBroadcast(intent);
                pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
            else{
                pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                getActivity().sendBroadcast(new Intent("Initiate"));
            }

        } else if(s.equals(getString(R.string.update_frequency_key))){
            int newDuration = Integer.parseInt(sharedPreferences.getString(s, "5")) * 1000 * 60 /*for milli seconds*/;
            //update the user preference
            getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE).edit().putInt(getString(R.string.location_update_duration), newDuration).commit();
            getActivity().sendBroadcast(new Intent("Initiate"));

        } else if(s.equals(getString(R.string.do_not_disturb_key))){
            boolean value = sharedPreferences.getBoolean(s, false);
            if(value){//enable don't disturb
                //// TODO: 21/03/17 disable/enable the firebase updates
            } else{

            }
        } else if(s.equals(getString(R.string.user_profile_key))){
            //// TODO: 21/03/17 load user profile, ?allow him to edit ??
        }
    }
}
