package com.project.locateme;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.project.locateme.Utilities.Constants;
import com.project.locateme.Utilities.FragmentsFactory;

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
            getFragmentManager().beginTransaction().replace(R.id.activity_holder_main_view, fragment);
    }
}
