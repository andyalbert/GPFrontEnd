package com.project.locateme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.project.locateme.DateHolder.Account;
import com.project.locateme.DateHolder.Profile;

import org.json.JSONObject;

import static java.util.Arrays.asList;

/**
 * Created by Andrew on 12/8/2016.
 */

public class LoginActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Profile profile;
    private Account account;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //facebook sdk setup
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        loginButton = (LoginButton) findViewById(R.id.login_button);

        facebookSetupAndLogin();
    }

    /**
     * should be called once any successful login using any provider
     *
     * @since 12/3/2016
     */
    private void fillUserDate(){

    }

    private void facebookSetupAndLogin() {
        loginButton.setReadPermissions(asList("public_profile", "email", "user_location", "user_birthday"));
        // If using in a fragment
        //loginButton.setFragment(this);

        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject data, GraphResponse response) {
                                profile = new Profile();
                                account = new Account();
                                try{
                                    account.setId(data.getString("id"));
                                    account.setType("facebook");
                                    account.setProfile(profile);

                                    profile.setName(data.getString("name"));
                                    profile.setFirstName(data.getString("first_name"));
                                    profile.setLastName(data.getString("last_name"));
                                    profile.setEmail(data.getString("email"));
                                    profile.setBirthday(data.getString("birthday"));
                                    profile.setHomeTown(data.getJSONObject("location").getString("name"));
                                    profile.setPictureURL(data.getJSONObject("picture").getJSONObject("data").getString("url"));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,email,location,birthday,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "You have cancelled Facebook login, please retry or choose another option", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "Error logging into your Facebook account, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
