package com.project.locateme;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.project.locateme.dateHolder.userManagement.Account;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Andrew on 12/8/2016.
 */

public class LoginActivity extends AppCompatActivity implements ProviderLoginFragment.FillUserData {
//    private static final String TWITTER_KEY = "Qcwv4vyvEMZTjwWQSjpLhJsGU";
//    private static final String TWITTER_SECRET = " KlopfY41uRZaMU5a1b025qS4W8qoVjCokkMcbUkyew2UHld2Oe";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //facebook sdk setup
        FacebookSdk.sdkInitialize(getApplicationContext());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_key), getString(R.string.twitter_secret));
        Fabric.with(this, new Twitter(authConfig));

        Log.i("Fab" , String.valueOf(Fabric.isInitialized()));
        setContentView(R.layout.activity_login);
        if(savedInstanceState == null)
            getFragmentManager().beginTransaction().replace(R.id.activity_login, new ProviderLoginFragment() ,"ProviderFragment").
                    commit();
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
        // // TODO: 12/11/2016 try to handle the crashing on changing orientation when loading for first time
        if(getFragmentManager().findFragmentByTag("FillUserData") == null)
            getFragmentManager().beginTransaction().replace(R.id.activity_login, dataFragment, "FillUserData").commitAllowingStateLoss();
    }

    @Override
    public void startLoading() {
        getFragmentManager().beginTransaction().replace(R.id.activity_login, new LoadingFragment()).commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getFragmentManager().findFragmentByTag("ProviderFragment");
        if (fragment != null) { //todo what do you mean by calling onactiviyresult explicitly ???
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
