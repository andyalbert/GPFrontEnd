package com.project.locateme;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.locateme.DateHolder.Account;
import com.project.locateme.DateHolder.Profile;

/**
 * Created by Andrew on 12/9/2016.
 */

public class FillUserDataFragment extends Fragment {
    private View view;
    private Account account;
    private Profile profile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_fill_user_details, container, false);
        account = (Account) getArguments().getSerializable("account");
        profile = account.getProfile();
        //Log.i("profile" , profile.getEmail()+profile.getFirstName()+profile.getHomeTown()+profile.getPictureURL());

        return view;
    }
}
