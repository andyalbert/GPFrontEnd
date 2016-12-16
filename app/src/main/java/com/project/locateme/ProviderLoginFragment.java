package com.project.locateme;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

import static java.util.Arrays.asList;

/**
 * Created by Andrew on 12/9/2016.
 */

public class ProviderLoginFragment extends Fragment {

    @BindView(R.id.login_button)
    LoginButton loginButton;
    @BindView(R.id.twitter_login_button)
    TwitterLoginButton twitterLoginButton;
    private View view;
    private CallbackManager callbackManager;
    private Profile profile;
    private Account account;
    private FillUserData fillUserData;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Activity rest" , "provider");
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode , resultCode , data);
    }
    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        fillUserData = (FillUserData) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fillUserData = (FillUserData) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //Log.i("Fab" , String.valueOf(Fabric.isInitialized()));
        view = inflater.inflate(R.layout.fragment_provider_login, container, false);
        ButterKnife.bind(this, view);
        facebookSetupAndLogin();
        twitterSetupAndLogin();
        return view;
    }

    private void facebookSetupAndLogin() {
        loginButton.setReadPermissions(asList("public_profile", "email", "user_location", "user_birthday"));
        loginButton.setFragment(this);

        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                fillUserData.startLoading();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject data, GraphResponse response) {

                                profile = new Profile();
                                account = new Account();
                                try {
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
                                    fillUserData.fill(account);
                                } catch (Exception e) {
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
                Toast.makeText(getActivity(), "You have cancelled Facebook login, please retry or choose another option", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), "Error logging into your Facebook account, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void twitterSetupAndLogin() {
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                fillUserData.startLoading();
                TwitterSession session = result.data;
                Twitter twitter = Twitter.getInstance();
                TwitterApiClient apiClient = twitter.core.getApiClient(session);
                AccountService service = apiClient.getAccountService();
                Call<User> userCall = service.verifyCredentials(true, true);
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        twitterLoginButton.setVisibility(View.GONE);
                        //// TODO: 12/11/2016 khaled, i had to comment this cuz it break the app with the loading logo running
//                        Toast.makeText(getActivity(), "Successfully logged in !", Toast.LENGTH_SHORT).show();
                        profile = new Profile();
                        account = new Account();
                        account.setId(result.data.idStr);
                        account.setType("twitter");
                        ///No Birthdate attribute
                        //profile.setBirthday(result.data.???);
                        profile.setEmail(result.data.email);
                        profile.setFirstName(result.data.name);
                        profile.setLastName(result.data.screenName);
                        profile.setHomeTown(result.data.location);
                        profile.setPictureURL(result.data.profileImageUrl);
                        account.setProfile(profile);
                        fillUserData.fill(account);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(getActivity(), "Error logging into your Twitter account, please try again", Toast.LENGTH_SHORT).show();
                    }
                });

//               try {
//                   userCall.execute();
//               } catch (IOException e) {
//                   e.printStackTrace();
//               }

            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getActivity(), "Error logging into your Twitter account, please try again", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public interface FillUserData {
        void fill(Account account);
        void startLoading();
    }

}
