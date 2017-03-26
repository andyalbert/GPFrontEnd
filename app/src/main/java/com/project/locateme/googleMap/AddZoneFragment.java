package com.project.locateme.googleMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.project.locateme.R;
import com.project.locateme.utilities.Constants;
;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddZoneFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener, View.OnClickListener {

    public static final int STROKE_COLOR = Color.BLACK;
    public static final int STROKE_WIDTH = 5;
    public static final int FILL_COLOR = 0X553F51B5;
    private View view;
    private GoogleMap mMap;
    private DraggableCircle draggableCircle;
    private Circle circle;

    private HashMap<String, Object> parameters;

    public final String CENTER_MARKER = "centerMarker";
    public final String RADIUS_MARKER = "radiusMarker";

    public static final double RADIUS_OF_EARTH_METERS = 6371009;

    @BindView(R.id.fragment_add_zone_map)
    MapView mapView;

    private class DraggableCircle {
        private Marker centerMarker;
        private Marker radiusMarker;
        private LatLng centerPosition;
        private LatLng radiusPosition;
        private double radius;

        public double getRadius(){
            return radius;
        }

        public void setCenterPosition(LatLng centerPosition) {
            this.centerPosition = centerPosition;
        }

        public void setRadiusPosition(LatLng radiusPosition) {
            this.radiusPosition = radiusPosition;
        }

        public Marker getCenterMarker() {
            return centerMarker;
        }

        public Marker getRadiusMarker() {
            return radiusMarker;
        }

        public void addMarker(LatLng latLng){
            if(centerMarker == null && radiusMarker == null){
                drawCenterMarker(latLng);
            } else if (radiusMarker == null){
                drawRadiusMarker(latLng);
                radius = toRadiusMeters(centerMarker.getPosition(), radiusMarker.getPosition());
                drawCircle();
                ButterKnife.findById(view, R.id.activity_add_zone_ok).setVisibility(View.VISIBLE);
                ButterKnife.findById(view, R.id.activity_add_zone_ok).setOnClickListener(AddZoneFragment.this);
            } else
                Toast.makeText(getActivity(), "you only need 2 markers to create a circle", Toast.LENGTH_LONG).show();
        }
        public void updateCircle(Marker marker){
            if(radiusMarker == null){
                return;
            }
            if(marker.equals(centerMarker)){
                circle.setCenter(marker.getPosition());
                radiusMarker.setPosition(toRadiusLatLng(marker.getPosition(), radius));
            } else {
                radius = toRadiusMeters(centerMarker.getPosition(), radiusMarker.getPosition());
                circle.setRadius(radius);
            }
        }

        private void drawCenterMarker(LatLng latLng){
            centerMarker = mMap.addMarker(new MarkerOptions()
                    .draggable(true)
                    .position(latLng)
                    .title("Center")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            centerMarker.showInfoWindow();
        }

        private void drawRadiusMarker(LatLng latLng){
            radiusMarker = mMap.addMarker(new MarkerOptions()
                    .draggable(true)
                    .position(latLng));
        }

        private void drawCircle(){
            circle = mMap.addCircle(new CircleOptions()
                    .center(centerMarker.getPosition())
                    .strokeColor(STROKE_COLOR)
                    .strokeWidth(STROKE_WIDTH)
                    .fillColor(FILL_COLOR)
                    .radius(radius));
        }

        private void drawIfPossible() {
            if(centerPosition != null)
                drawCenterMarker(centerPosition);
            if(radiusPosition != null)
                drawRadiusMarker(radiusPosition);
            if(radiusPosition != null && centerPosition != null){
                radius = toRadiusMeters(centerPosition, radiusPosition);
                drawCircle();
                ButterKnife.findById(view, R.id.activity_add_zone_ok).setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * get the radiusMarker from the centerMarker and radiusMarker markers, NOTE : from google api sample app
     * @param center
     * @param radius
     * @return
     */
    private static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);
        return result[0];
    }

    /**
     * Generate LatLng of radiusMarker marker, NOTE : from google api sample app
     */
    private static LatLng toRadiusLatLng(LatLng center, double radius) {
        double radiusAngle = Math.toDegrees(radius / RADIUS_OF_EARTH_METERS) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_add_zone, container, false);
        ButterKnife.bind(this, view);
        MapsInitializer.initialize(getActivity());

        //this won't be used here, but to be sent to the following fragment/activity
        parameters = (HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        draggableCircle = new DraggableCircle();

        if(getActivity().getPreferences(getActivity().MODE_PRIVATE).getString("checked", "").equals("") && savedInstanceState == null){
            ((CheckBox)ButterKnife.findById(view, R.id.activity_add_zone_check_box)).setChecked(true);
            ButterKnife.findById(view, R.id.activity_add_zone_upper_layer).setVisibility(View.VISIBLE);

            //custom font, better user experience
            Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");
            ((TextView)ButterKnife.findById(view, R.id.activity_add_zone_welcome)).setTypeface(type);

            //check if the check box is active or not
            ButterKnife.findById(view, R.id.activity_add_zone_exit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    CheckBox box = (CheckBox) ButterKnife.findById(view, R.id.activity_add_zone_check_box);
                    if(box.isChecked())
                        getActivity().getPreferences(getActivity().MODE_PRIVATE).edit().putString("checked", "1").apply();
                    ButterKnife.findById(view, R.id.activity_add_zone_upper_layer).setVisibility(View.GONE);
                }
            });
        } else if(savedInstanceState != null){
            draggableCircle.setCenterPosition((LatLng) savedInstanceState.getParcelable(CENTER_MARKER));
            draggableCircle.setRadiusPosition((LatLng) savedInstanceState.getParcelable(RADIUS_MARKER));
        }


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        draggableCircle.drawIfPossible(); //draw the circle, used for orientation change

        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        if(draggableCircle.getCenterMarker() != null){
            outState.putParcelable(CENTER_MARKER, draggableCircle.getCenterMarker().getPosition());
            outState.putParcelable(RADIUS_MARKER, draggableCircle.getRadiusMarker().getPosition());
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        draggableCircle.addMarker(latLng);
    }

    //// TODO: 01/02/17 remove this on mobile, it's just to come over emulator shortage
    @Override
    public void onMapClick(LatLng latLng) {
        draggableCircle.addMarker(latLng);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        draggableCircle.updateCircle(marker);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        draggableCircle.updateCircle(marker);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        draggableCircle.updateCircle(marker);
    }

    /**
     * used for handling the click event of floating point (after both radius and center have been chosen
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("lat", draggableCircle.getCenterMarker().getPosition().latitude);
        resultIntent.putExtra("long", draggableCircle.getCenterMarker().getPosition().longitude);
        resultIntent.putExtra("radius", draggableCircle.getRadius());
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
        getActivity().finish();
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
}
