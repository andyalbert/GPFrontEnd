package com.project.locateme.MainViews;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.locateme.DateHolder.Event;
import com.project.locateme.ImagePickerActivity;
import com.project.locateme.R;

import butterknife.BindDrawable;
import butterknife.BindView;

public class EventActivity extends AppCompatActivity {


    @BindView(R.id.activity_event_add_image)
    Button addImageButton;
    @BindView(R.id.activity_event_description)
    TextView description;
    @BindView(R.id.activity_event_date_view)
    TextView dateTextview;

    CollapsingToolbarLayout collapsingToolbar;

    Event model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
         collapsingToolbar=
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("EventName");
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ImagePickerActivity.class);
                startActivity(intent);
            }
        });
        //initializeModel();
    }
    public void initializeModel(){
        Intent intent = getIntent();
        model = new Event();
        model = (Event) intent.getSerializableExtra("event");
        collapsingToolbar.setTitle(model.getName());
        description.setText(model.getDescription());
        dateTextview.setText(model.getLocation().getDate().toString());
    }
}
