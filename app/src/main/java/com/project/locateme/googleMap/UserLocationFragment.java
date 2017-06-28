package com.project.locateme.googleMap;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.locateme.R;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Andrew
 * @version 1.1
 * @since 1/2/2017
= */

public class UserLocationFragment extends Fragment implements OnMapReadyCallback {
    private Unbinder unbinder;
    private View view;
    private GoogleMap mMap;
    private double lat;
    private double lon;
    private String name;
    private String imgUrl;

    @BindView(R.id.fragment_user_location_map)
    MapView mapView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_user_location, container, false);
        unbinder = ButterKnife.bind(this, view);

        HashMap<String, Object> parameters = (HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP);
        lat = (double) parameters.get("lat");
        lon = (double) parameters.get("lon");
        name = (String) parameters.get("name");
        imgUrl = (String) parameters.get("url");

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(name + " position");

        MapsInitializer.initialize(this.getActivity());
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        View myView = LayoutInflater.from(getActivity()).inflate(R.layout.map_marker, null);
        //// TODO: 31/01/17 uncomment this, it's only till connection with the profiles
//        Glide.with(getApplicationContext())
//                .load(imgUrl)
//                .into((ImageView)myView.findViewById(R.id.map_marker_user_image));

        Bitmap bitmap = General.loadBitmapFromView(myView);
        MarkerOptions marker = new MarkerOptions()
                .title(name)
                .position(new LatLng(lat, lon))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        mMap.addMarker(marker).showInfoWindow();
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 12.0f));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        unbinder.unbind();
    }
}
