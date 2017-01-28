package com.project.locateme;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.project.locateme.dataHolder.userManagement.Account;
import com.project.locateme.dataHolder.userManagement.Profile;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Andrew
 * @since 12/12/2017
 * @version 1.0
 */

public class FillUserDataFragment extends Fragment {
    private View view;
    private Account account;
    private Profile profile;
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.first_name)
    EditText firstName;
    @BindView(R.id.last_name)
    EditText lastName;
    @BindView(R.id.birthday)
    EditText birthday;
    @BindView(R.id.home_town)
    EditText homeTown;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.confirmed_password)
    EditText confirmedPassword;
    @BindView(R.id.email)
    EditText email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_fill_user_details, container, false);
        ButterKnife.bind(this, view);
        account = (Account) getArguments().getSerializable("account");
        profile = account.getProfile();
        Glide.with(getActivity()).load(profile.getPictureURL()).into(profileImage);
//        profileImage.setImageBitmap();
        userName.setText(profile.getName());
        lastName.setText(profile.getLastName());
        firstName.setText(profile.getFirstName());
//        birthday.setText(General.calendarToString(profile.getBirthday()));
        homeTown.setText(profile.getHomeTown());
        email.setText(profile.getEmail());
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
                startActivity(intent);
            }
        });

        //Log.i("profile" , profile.getEmail()+profile.getFirstName()+profile.getHomeTown()+profile.getPictureURL());

        return view;
    }
}
