package com.project.locateme.MainViews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.locateme.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andrew on 1/25/2017.
 */
public class MessageFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
