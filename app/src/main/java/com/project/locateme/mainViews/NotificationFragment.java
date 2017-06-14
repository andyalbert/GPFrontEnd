package com.project.locateme.mainViews;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.project.locateme.R;
import com.project.locateme.dataHolder.NotificationManger.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew
 * @since 25/1/2017
 * @version 1.0
 */
public class NotificationFragment extends Fragment {
    private View view;
    private Unbinder unbinder;
    @BindView(R.id.fragment_notification_list_view)
    ListView listView;
    private NotificationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        unbinder = ButterKnife.bind(this, view);

        loadNotifications();

        return view;
    }

    private void loadNotifications() {


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount >= totalItemCount - 3){

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class NotificationAdapter extends ArrayAdapter<Notification>{
        private ArrayList<Notification> notifications; //// TODO: 13/06/17 this may be changed to more efficient data structure
        private Context context;

        public NotificationAdapter(Context context, int resource, ArrayList<Notification> notifications) {
            super(context, resource);
            this.context = context;
            this.notifications = notifications;
        }

        @Override
        public int getCount() {
            return notifications.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = notifications.get(position).getView(context);
                notifications.get(position).setViewTag(convertView);
            }
            notifications.get(position).setViewListener(convertView);

            return convertView;
        }
    }
}
