package com.project.locateme.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.project.locateme.ImagePickerActivity;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by khaled on 1/28/2017.
 */

public class EventFragment extends Fragment {
    private View view;
    private Event model;
    @BindView(R.id.fragment_event_add_image)
    Button addImageButton;
    @BindView(R.id.fragment_event_description)
    TextView description;
    @BindView(R.id.fragment_event_date_view)
    TextView dateTextview;
    @BindView(R.id.fragment_event_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);

        collapsingToolbar.setTitle("EventName");
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
                startActivity(intent);
           }
        });

        //initializeModel();
        return view;
}
    public void initializeModel(){
        Intent intent = getActivity().getIntent();
        model = new Event();
        model = (Event) intent.getSerializableExtra("event");
        collapsingToolbar.setTitle(model.getName());
        description.setText(model.getDescription());
        dateTextview.setText(model.getDateOfEvent().toString());
    }
}
