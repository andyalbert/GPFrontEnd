package com.project.locateme;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.locateme.dataHolder.userManagement.Account;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.mainViews.MainUserActivity;
import com.project.locateme.updatingUserLocation.ProviderNetworkStateBroadcastReceiver;
import com.project.locateme.utilities.Constants;

import org.json.JSONObject;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Andrew
 * @since 12/12/2017
 * @version 2.0
 */

public class FillUserDataFragment extends Fragment {
    private Unbinder unbinder;
    private View view;
    private Account account;
    private Profile profile;
    private SharedPreferences preferences;
    private final String VOLLEY_REQUEST = "request";
    private RequestQueue queue;
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
    @BindView(R.id.fragment_fill_user_details_floating_button)
    FloatingActionButton floatingActionButton;
    private StorageReference reference;
    private static int IMAGE_REQUEST = 5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        reference = FirebaseStorage.getInstance().getReference();
        view = inflater.inflate(R.layout.fragment_fill_user_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        preferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(getActivity());

        account = (Account) getArguments().getSerializable("account");
        profile = account.getProfile();
        Glide.with(getActivity()).load(profile.getPictureURL()).into(profileImage);
        //// TODO: 08/05/17 fix
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
                startActivityForResult(intent , IMAGE_REQUEST);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCorrectData())
                    register();
            }
        });

        return view;
    }

    private boolean isCorrectData() {
        boolean isTrue = true;
        Pattern spacePattern = Pattern.compile("\\s*"),
                emailPattern = Pattern.compile("[a-zA-Z_0-9-\\.]+@[a-zA-Z_0-9-]+\\.[a-zA-Z_0-9-]+"),
                dataOfBirthPattern = Pattern.compile("(19[0-9]{2}|20(1[0-7]|0[0-9]))-(1[1-2]|[1-9])-(3[0-1]|[12][0-9]|[1-9])"),
                passwordPattern = Pattern.compile("(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}");
        Matcher matcher;

        matcher = spacePattern.matcher(userName.getText().toString());
        if(matcher.find() && matcher.start() == 0 && matcher.end() == userName.getText().toString().length()){
            Toast.makeText(getActivity(), "please provide a valid name", Toast.LENGTH_SHORT).show();
            isTrue = false;
        }

        matcher = spacePattern.matcher(firstName.getText().toString());
        if(matcher.find() && matcher.start() == 0 && matcher.end() == firstName.getText().toString().length()){
            Toast.makeText(getActivity(), "please provide a valid first name", Toast.LENGTH_SHORT).show();
            isTrue = false;
        }

        matcher = spacePattern.matcher(lastName.getText().toString());
        if(matcher.find() && matcher.start() == 0 && matcher.end() == lastName.getText().toString().length()){
            Toast.makeText(getActivity(), "please provide a valid last name", Toast.LENGTH_SHORT).show();
            isTrue = false;
        }

        matcher = spacePattern.matcher(homeTown.getText().toString());
        if(matcher.find() && matcher.start() == 0 && matcher.end() == homeTown.getText().toString().length()){
            Toast.makeText(getActivity(), "please provide a valid hometown", Toast.LENGTH_SHORT).show();
            isTrue = false;
        }

        matcher = emailPattern.matcher(email.getText().toString());
        if(!matcher.find() || !(matcher.start() == 0) || !(matcher.end() == email.getText().toString().length())){
            Toast.makeText(getActivity(), "please provide a valid email", Toast.LENGTH_SHORT).show();
            isTrue = false;
        }

        matcher = dataOfBirthPattern.matcher(birthday.getText().toString());
        if(!matcher.find() || !(matcher.start() == 0) || !(matcher.end() == birthday.getText().toString().length())){
            Toast.makeText(getActivity(), "please provide a valid birthday", Toast.LENGTH_SHORT).show();
            isTrue = false;
        }

        matcher = passwordPattern.matcher(password.getText().toString());
        if(!matcher.find() || !(matcher.start() == 0) || !(matcher.end() == password.getText().toString().length())){
            Toast.makeText(getActivity(), "password must be of at least 8 characters, having lowercase, uppercase and numbers", Toast.LENGTH_LONG).show();
            isTrue = false;
        }

        if(!password.getText().toString().equals(confirmedPassword.getText().toString())){
            Toast.makeText(getActivity(), "password and it's confirmation should match", Toast.LENGTH_SHORT).show();
            isTrue = false;
        }

        return isTrue;
    }

    private void register() {
        Uri uri = Uri.parse(Constants.REGISTER_ACCOUNT).buildUpon()
                .appendQueryParameter("firstname", firstName.getText().toString())
                .appendQueryParameter("lastname", lastName.getText().toString())
                .appendQueryParameter("email", email.getText().toString())
                .appendQueryParameter("hometown", homeTown.getText().toString())
                .appendQueryParameter("name", userName.getText().toString())
                .appendQueryParameter("pass", password.getText().toString())
                //// TODO: 08/05/17 fix this shit
                .appendQueryParameter("pictureURL", profile.getPictureURL())
                .appendQueryParameter("loginid", account.getId())
                .appendQueryParameter("provider", account.getType())
                //// TODO: 08/05/17 fix this
                .appendQueryParameter("birthday", profile.getBirthday())
                .build();

        StringRequest request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object = new JSONObject(response);
                    //save the user data that will be used during the application usage
                    preferences.edit().putString(getString(R.string.user_name), userName.getText().toString()).apply();
                    preferences.edit().putString(getString(R.string.user_password), password.getText().toString()).apply();
                    preferences.edit().putString(getString(R.string.user_id), String.valueOf(object.getInt("id"))).apply();

                    //enable the location updater
                    PackageManager pm = getActivity().getPackageManager();
                    ComponentName componentName = new ComponentName(getActivity(), ProviderNetworkStateBroadcastReceiver.class);
                    pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);

                    //set provider
//                            if(account.getType().equals("facebook"))
//                                 preferences.edit().putInt(getString(R.string.provider), Constants.FACEBOOK_LOGIN).apply();
//                            else
//                                preferences.edit().putInt(getString(R.string.provider), Constants.TWITTER_LOGIN).apply();
                    preferences.edit().putBoolean(getString(R.string.is_signed_in), true).apply();
                    startActivity(new Intent(getActivity(), MainUserActivity.class));
                    getActivity().finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Couldn't finish your request, please try again", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(VOLLEY_REQUEST);
        queue.add(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == getActivity().RESULT_OK){
            profile.setPictureURL(data.getStringExtra("path"));
            Log.e("ImagePathCreate", data.getStringExtra("path"));
            Uri imagePathUri = Uri.fromFile(new File(data.getStringExtra("path")));
            /*reference.child(account.getId().toString());
            UploadTask uploadTask = reference.child(account.getId().toString()).putFile(imagePathUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadImage = taskSnapshot.getDownloadUrl();
                    profile.setPictureURL(downloadImage.toString());
                }
            });
            //Log.i("Path", imagePath);
            Glide.with(getActivity()).load(imagePathUri).into(profileImage);*/
            reference.child(account.getId().toString()).putFile(imagePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadImage = taskSnapshot.getDownloadUrl();
                    profile.setPictureURL(downloadImage.toString());
                    Glide.with(getActivity()).load(profile.getPictureURL()).into(profileImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });



        }
    }

    @Override
    public void onDestroyView() {
        if(queue != null)
            queue.cancelAll(VOLLEY_REQUEST);
        super.onDestroyView();
        unbinder.unbind();
    }

}
