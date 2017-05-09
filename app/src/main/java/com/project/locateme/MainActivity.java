package com.project.locateme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.project.locateme.mainViews.MainUserActivity;
import com.project.locateme.utilities.FirebaseInstanceIDService;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

/**
 * @author Andrew
 * @since 9/12/2016
 * @version 1.0
 */


public class MainActivity extends AppCompatActivity {

    private static final String TWITTER_KEY = "Qcwv4vyvEMZTjwWQSjpLhJsGU";
    private static final String TWITTER_SECRET = " KlopfY41uRZaMU5a1b025qS4W8qoVjCokkMcbUkyew2UHld2Oe";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        //// TODO: 08/05/17 uncomment this
        //Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);
        if(preferences.getBoolean(getString(R.string.is_signed_in), false))
            startActivity(new Intent(this, MainUserActivity.class));
        else
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        this.finish();
    }
}
