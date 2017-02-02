package com.project.locateme.googleMap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.locateme.R;
import com.project.locateme.utilities.Constants;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @since 2/2/2017
 * @version 1.0
 * @author andrew
 */

public class ViewZoneFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap map;
    private View view;
    private HashMap<String, Object> parameters;

    public static final int STROKE_COLOR = Color.BLACK;
    public static final int STROKE_WIDTH = 5;
    public static final int FILL_COLOR = 0X553F51B5;

    @BindView(R.id.fragment_view_zone_map)
    MapView mapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_view_zone, container, false);
        ButterKnife.bind(this, view);

        parameters = (HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP);

        MapsInitializer.initialize(this.getActivity());
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
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
    public void onPause() {
        super.onPause();
        mapView.onPause();
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
        mapView.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        drawAndFocus();
    }

    private void drawAndFocus() {
        LatLng latLng = new LatLng((double) parameters.get("lat"), (double) parameters.get("long"));
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title((String) parameters.get("title"))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        map.addCircle(new CircleOptions()
                .strokeWidth(STROKE_WIDTH)
                .strokeColor(STROKE_COLOR)
                .fillColor(FILL_COLOR)
                .center(latLng)
                .radius((double) parameters.get("radius")));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setCompassEnabled(true);
    }
}
