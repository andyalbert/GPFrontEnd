package com.project.locateme.MainViews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

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

        setViewPagerTabs();
    }

    private void setViewPagerTabs() {
        MainViewsAdapter mainViewsAdapter = new MainViewsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainViewsAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_home);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_message);
        tabLayout.getTabAt(2).setIcon(R.mipmap.ic_notification);
        tabLayout.getTabAt(3).setIcon(R.mipmap.ic_settings);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(getApplicationContext(), "selected", Toast.LENGTH_SHORT).show();
                if(tab.getPosition() == 1)//// TODO: 1/25/2017 to be done later, after fixing the mipmap overload
                    tab.setIcon(R.mipmap.ic_message_selected);
//                switch(tab.getPosition()){
//                    case 0: tab.setIcon(R.d);
//                        break;
//                    case 1: tab.setIcon();
//                        break;
//                    case 2: tab.setIcon();
//                        break;
//                    case 3: tab.setIcon();
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 1)
                    tab.setIcon(R.mipmap.ic_message);
                Toast.makeText(getApplicationContext(), "unselected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
