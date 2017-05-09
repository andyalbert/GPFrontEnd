package com.project.locateme;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.project.locateme.dataHolder.userManagement.Account;
import com.project.locateme.utilities.Constants;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * @author Andrew
 * @since 8/12/2017
 * @version 1.5
 */

public class LoginActivity extends AppCompatActivity implements ProviderLoginFragment.FillUserData {
//    private static final String TWITTER_KEY = "Qcwv4vyvEMZTjwWQSjpLhJsGU";
//    private static final String TWITTER_SECRET = " KlopfY41uRZaMU5a1b025qS4W8qoVjCokkMcbUkyew2UHld2Oe";
    private Fragment loadingFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //facebook sdk setup
        FacebookSdk.sdkInitialize(getApplicationContext());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_key), getString(R.string.twitter_secret));
        //// TODO: 08/05/17 uncomment this
        //Fabric.with(this, new Twitter(authConfig));
        Log.i("Fab" , String.valueOf(Fabric.isInitialized()));
        setContentView(R.layout.activity_login);
        if(savedInstanceState == null){
            getFragmentManager().beginTransaction().add(R.id.activity_login, new ProviderLoginFragment() ,"ProviderFragment").
                    commit();
            getFragmentManager().beginTransaction().addToBackStack(null).commit();
        }
    }

    /**
     * should be called once any successful login using any provider
     *
     * @since 12/3/2016
     */

    @Override
    public void fill(Account account) {
        FillUserDataFragment dataFragment = new FillUserDataFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("account", account);
        dataFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().remove(loadingFragment).commit();
        if(getFragmentManager().findFragmentByTag("FillUserData") == null)
            getFragmentManager().beginTransaction().replace(R.id.activity_login, dataFragment, "FillUserData").commitAllowingStateLoss();
    }

    @Override
    public void startLoading() {
        loadingFragment = new LoadingFragment();
        getFragmentManager().beginTransaction().add(R.id.activity_login, loadingFragment, "loading").commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getFragmentManager().findFragmentByTag("ProviderFragment");
        if (fragment != null) { //todo what do you mean by calling onactiviyresult explicitly ???
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);
//        if(providerLogin){
//            if(preferences.getInt(getString(R.string.provider), -1) == Constants.FACEBOOK_LOGIN)
//                LoginManager.getInstance().logOut();
//            else if(preferences.getInt((getString(R.string.provider)), -1) == Constants.TWITTER_LOGIN) {
//                //// TODO: 08/05/17 handle twitter logout
//            }
//        }
//        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);
        boolean isLogined = preferences.getBoolean(getString(R.string.is_signed_in), false);
        if(!isLogined){
            if(preferences.getInt(getString(R.string.provider), -1) == Constants.FACEBOOK_LOGIN)
                LoginManager.getInstance().logOut();
            else if(preferences.getInt((getString(R.string.provider)), -1) == Constants.TWITTER_LOGIN) {
                //// TODO: 08/05/17 handle twitter logout
            }
        }
        finishAffinity();
    }
}
