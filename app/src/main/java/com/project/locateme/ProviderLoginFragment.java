package com.project.locateme;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.project.locateme.dataHolder.userManagement.Account;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.mainViews.MainUserActivity;
import com.project.locateme.updatingUserLocation.ProviderNetworkStateBroadcastReceiver;
import com.project.locateme.utilities.Constants;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

import static java.util.Arrays.asList;

/**
 * @author Andrew
 * @since 12/9/2017
 * @version 2.0
 */

public class ProviderLoginFragment extends Fragment {
    private Unbinder unbinder;
    private View view;
    private CallbackManager callbackManager;
    private Profile profile;
    private Account account;
    private FillUserData fillUserData;
    private SharedPreferences preferences;
    private RequestQueue queue;
    private final String VOLLEY_REQUEST = "request";
    @BindView(R.id.login_button)
    LoginButton loginButton;
    @BindView(R.id.twitter_login_button)
    TwitterLoginButton twitterLoginButton;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Activity rest" , "provider");
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //// TODO: 08/05/17 uncomment this
//        twitterLoginButton.onActivityResult(requestCode , resultCode , data);
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
        unbinder = ButterKnife.bind(this, view);
        queue = Volley.newRequestQueue(getActivity());
        preferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
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
                preferences.edit().putInt(getString(R.string.provider), Constants.FACEBOOK_LOGIN).apply();
                fillUserData.startLoading();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject data, GraphResponse response) {
                                try{
                                    checkRegistration(data, data.getString("id"));
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
                Toast.makeText(getActivity(), "You have cancelled Facebook login, please retry or choose another option", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), "Error logging into your Facebook account, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkRegistration(final Object object, String id) {
        Uri uri = Uri.parse(Constants.CHECK_REGISTRATION).buildUpon()
                .appendQueryParameter("loginid", id)
                .appendQueryParameter("provider", preferences.getInt(getString(R.string.provider), -1) == Constants.FACEBOOK_LOGIN ? "facebook" : "twitter")
                .build();

        StringRequest request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean isRegistered = false;
                JSONObject jsonObject = null;
                try{
                    jsonObject = new JSONObject(response);
                    isRegistered = jsonObject.getInt("isRegisterd") == 2;
                } catch (Exception e){
                    e.printStackTrace();
                }

                if(isRegistered){
                    try{
                        preferences.edit().putString(getString(R.string.user_name), jsonObject.getString("name")).apply();
                        preferences.edit().putString(getString(R.string.user_password), jsonObject.getString("pass")).apply();
                        preferences.edit().putString(getString(R.string.user_id), String.valueOf(jsonObject.getInt("id"))).apply();

                        //enable the location updater
                        PackageManager pm = getActivity().getPackageManager();
                        ComponentName componentName = new ComponentName(getActivity(), ProviderNetworkStateBroadcastReceiver.class);
                        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP);

                        preferences.edit().putBoolean(getString(R.string.is_signed_in), true).apply();

                        ProviderLoginFragment.this.getActivity().finish();
                        startActivity(new Intent(ProviderLoginFragment.this.getActivity(), MainUserActivity.class));

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    if(preferences.getInt(getString(R.string.provider), -1) == Constants.FACEBOOK_LOGIN){
                        profile = new Profile();
                        account = new Account();
                        try {
                            JSONObject data = (JSONObject) object;
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else{
                        User user = (User) object;
                        profile = new Profile();
                        account = new Account();
                        account.setId(user.idStr);
                        account.setType("twitter");
                        ///No Birthdate attribute //// TODO: 09/05/17 fill
                        // Twitter API doesnt return a birthday Attr
                        //profile.setBirthday(result.data.???);
                        profile.setEmail(user.email);
                        profile.setFirstName(user.name.split(" ")[0]);
                        profile.setLastName(user.name.split(" ")[1]);
                        profile.setHomeTown(user.location);
                        profile.setPictureURL(user.profileImageUrl);
                        account.setProfile(profile);
                    }
                    fillUserData.fill(account);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Couldn't complete registration, please try again", Toast.LENGTH_SHORT).show();
                if(preferences.getInt(getString(R.string.provider), -1) == Constants.FACEBOOK_LOGIN)
                    LoginManager.getInstance().logOut();
                else if(preferences.getInt(getString(R.string.provider), -1) == Constants.TWITTER_LOGIN){
                    //// TODO: 08/05/17 handle twitter
                    Twitter.getSessionManager().clearActiveSession();
                    Twitter.logOut();
                }
                getActivity().finish();
            }
        });

        request.setTag(VOLLEY_REQUEST);
        queue.add(request);
    }

    private void twitterSetupAndLogin() {
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                preferences.edit().putInt(getString(R.string.provider), Constants.TWITTER_LOGIN).apply();
                fillUserData.startLoading();
                TwitterSession session = result.data;
                Twitter twitter = Twitter.getInstance();
                TwitterApiClient apiClient = twitter.core.getApiClient(session);
                AccountService service = apiClient.getAccountService();
                Call<User> userCall = service.verifyCredentials(true, true);
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        //// TODO: 08/05/17 what is the use of the following line ? , Kh : is it not clear ?
                        twitterLoginButton.setVisibility(View.GONE);
                        //// TODO: 12/11/2016 khaled, i had to comment this cuz it break the app with the loading logo running , Kh: Its ok
//                        Toast.makeText(getActivity(), "Successfully logged in !", Toast.LENGTH_SHORT).show();
                        checkRegistration(result.data, result.data.idStr);
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

    @Override
    public void onDestroyView() {
        if(queue != null)
            queue.cancelAll(VOLLEY_REQUEST);
        super.onDestroyView();
        unbinder.unbind();
    }
}
