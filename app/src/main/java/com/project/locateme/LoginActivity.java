package com.project.locateme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.project.locateme.DateHolder.Account;
import com.project.locateme.DateHolder.Profile;

/**
 * Created by Andrew on 12/8/2016.
 */

public class LoginActivity extends AppCompatActivity implements ProviderLoginFragment.FillUserData {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //facebook sdk setup
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        if(savedInstanceState == null)
            getFragmentManager().beginTransaction().replace(R.id.activity_login, new ProviderLoginFragment()).commit();
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
        getFragmentManager().beginTransaction().replace(R.id.activity_login, dataFragment).commit();
    }
}
