package com.project.locateme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.FragmentsFactory;

/**
 * @author Andrew
 * @since 26/1/2017
 * @version 1.0
 *
 * this Activity's main responsibility is to act just as a holder for any other fragment that are called in situations
 * where it can't use the current Activity as it's holder, ie. from the Activity that holds the tabs, etc...
 */

public class HolderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);
        String fragmentName = getIntent().getExtras().getString(getResources().getString(R.string.fragment_name));
        Fragment fragment = FragmentsFactory.getFragmentForActivityHolder(fragmentName);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.HASHMAP, getIntent().getExtras().getSerializable(Constants.HASHMAP));
        fragment.setArguments(bundle);

        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_holder_main_view, fragment);
    }
}
