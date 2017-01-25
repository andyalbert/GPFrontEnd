package com.project.locateme.MainViews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.project.locateme.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andrew on 1/25/2017.
 */

public class MainUserActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        ButterKnife.bind(this);

        MainViewsAdapter mainViewsAdapter = new MainViewsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainViewsAdapter);

        tabLayout.setupWithViewPager(viewPager);
        setViewPagerTabs();
    }

    private void setViewPagerTabs() {

        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_home);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_message);
        tabLayout.getTabAt(2).setIcon(R.mipmap.ic_notification);
        tabLayout.getTabAt(3).setIcon(R.mipmap.ic_settings);
    }
}
