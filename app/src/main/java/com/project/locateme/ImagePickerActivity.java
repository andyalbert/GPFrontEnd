package com.project.locateme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * @author Andrew
 * @since 16/12/2016
 * @version 1.0
 */

public class ImagePickerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        getWindow().setLayout((int)(metrics.widthPixels * 0.6), (int)(metrics.heightPixels * 0.2));

    }
}
