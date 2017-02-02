package com.project.locateme.customViews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.project.locateme.R;

/**
 * Created by andrew on 02/02/17.
 */

public class testActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_back_pressed, R.anim.exit_back_pressed);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_transition);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.test);
        animation.setFillAfter(true);
        animation.setDuration(300);
        findViewById(R.id.test_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
            }
        });
    }
}
