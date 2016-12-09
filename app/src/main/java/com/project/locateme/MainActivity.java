package com.project.locateme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //the following two lines will allow facebook to trigger how many times have the app been installed (it's mainly for analytical usage)
     //   FacebookSdk.sdkInitialize(getApplicationContext());
      //  AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.activity_main);

        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}
