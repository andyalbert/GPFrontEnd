package com.project.locateme.googleMap;

import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.locateme.R;

public class AddZoneActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private DraggableCircle draggableCircle;
    private Circle circle;

    public final String CENTER_MARKER = "centerMarker";
    public final String RADIUS_MARKER = "radiusMarker";

    public static final double RADIUS_OF_EARTH_METERS = 6371009;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(draggableCircle.getCenterMarker() != null){
            outState.putParcelable(CENTER_MARKER, draggableCircle.getCenterMarker().getPosition());
            outState.putParcelable(RADIUS_MARKER, draggableCircle.getRadiusMarker().getPosition());
        }
    }

    private class DraggableCircle {
        private Marker centerMarker;
        private Marker radiusMarker;
        private LatLng centerPosition;
        private LatLng radiusPosition;
        private double radius;

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
                findViewById(R.id.activity_add_zone_ok).setVisibility(View.VISIBLE);
                findViewById(R.id.activity_add_zone_ok).setOnClickListener(AddZoneActivity.this);
            } else
                Toast.makeText(getApplicationContext(), "you only need 2 markers to create a circle", Toast.LENGTH_LONG).show();
        }
        public void updateCircle(Marker marker){
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
                    .strokeColor(Color.BLACK)
                    .strokeWidth(5)
                    .fillColor(0X553F51B5)
                    .radius(radius));
        }

        public void drawIfPossible() {
            if(centerPosition != null)
                drawCenterMarker(centerPosition);
            if(radiusPosition != null)
               drawRadiusMarker(radiusPosition);
            if(radiusPosition != null && centerPosition != null){
                radius = toRadiusMeters(centerPosition, radiusPosition);
                drawCircle();
                findViewById(R.id.activity_add_zone_ok).setVisibility(View.VISIBLE);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zone);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        draggableCircle = new DraggableCircle();

        if(getPreferences(MODE_PRIVATE).getString("checked", "").equals("") && savedInstanceState == null){
            ((CheckBox)findViewById(R.id.activity_add_zone_check_box)).setChecked(true);
            findViewById(R.id.activity_add_zone_upper_layer).setVisibility(View.VISIBLE);

            //custom font, better user experience
            Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
            ((TextView)findViewById(R.id.activity_add_zone_welcome)).setTypeface(type);

            //check if the check box is active or not
            findViewById(R.id.activity_add_zone_exit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox box = (CheckBox)findViewById(R.id.activity_add_zone_check_box);
                    if(box.isChecked())
                        getPreferences(MODE_PRIVATE).edit().putString("checked", "1").apply();
                    findViewById(R.id.activity_add_zone_upper_layer).setVisibility(View.GONE);
                }
            });
        } else if(savedInstanceState != null){
            draggableCircle.setCenterPosition((LatLng) savedInstanceState.getParcelable(CENTER_MARKER));
            draggableCircle.setRadiusPosition((LatLng) savedInstanceState.getParcelable(RADIUS_MARKER));
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        draggableCircle.drawIfPossible();
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);
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
        //copy radius, and center to the next fragment/activity
    }
}
