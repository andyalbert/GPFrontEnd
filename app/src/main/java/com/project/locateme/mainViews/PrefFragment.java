package com.project.locateme.mainViews;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.facebook.login.LoginManager;
import com.project.locateme.MainActivity;
import com.project.locateme.R;
import com.project.locateme.updatingUserLocation.ProviderNetworkStateBroadcastReceiver;
import com.project.locateme.utilities.Constants;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * @author andrew
 * @since 20/3/2017
 * @version 1.5
 */

public class PrefFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
   // private SharedPreferences preference;

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey)  {
        addPreferencesFromResource(R.xml.preferences);
        Preference logOut = findPreference(getString(R.string.log_out_key));
        logOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
                if(preferences.getInt(getString(R.string.provider), 0) == Constants.FACEBOOK_LOGIN)
                    LoginManager.getInstance().logOut();
                else{
                    //todo handle twitter logout
                    TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                    if(session != null){
                        ClearTwitterCookies(getActivity().getApplicationContext());
                        Twitter.getSessionManager().clearActiveSession();
                        Twitter.logOut();
                    }
                }

                preferences.edit().putBoolean(getString(R.string.is_signed_in), false).apply();

                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().apply();


                getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE).edit().putInt(getString(R.string.location_update_duration), 300000).apply();
                //now disable the location updater
                PackageManager pm = getActivity().getPackageManager();
                ComponentName componentName = new ComponentName(getActivity(), ProviderNetworkStateBroadcastReceiver.class);
                Intent intent = new Intent("Initiate");
                intent.putExtra("disable", 1);
                getActivity().sendBroadcast(intent);
                pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);

                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                return false;
            }
        });
    }
    public static void ClearTwitterCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
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
