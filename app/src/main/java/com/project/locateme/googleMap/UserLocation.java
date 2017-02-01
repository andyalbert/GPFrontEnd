package com.project.locateme.googleMap;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.locateme.R;
import com.project.locateme.utilities.General;

public class UserLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lat;
    private double lon;
    private String name;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //// TODO: 31/01/17 remove that silly status bar !
        setContentView(R.layout.activity_user_location);
        lat = getIntent().getDoubleExtra("lat", 0.0);
        lon = getIntent().getDoubleExtra("lon", 0.0);
        name = getIntent().getStringExtra("name");
        imgUrl = getIntent().getStringExtra("url");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.map_marker, null);
        //// TODO: 31/01/17 uncomment this, it's only till connection with the profiles
//        Glide.with(getApplicationContext())
//                .load(imgUrl)
//                .into((ImageView)view.findViewById(R.id.map_marker_user_image)); //// TODO: 31/01/17  this may cause an error, the image may be loaded after the view has been rendered, must check this

        //// TODO: 31/01/17 remove those
        name = "Andrew Albert";
        lat = 34.2;
        lon = 36.2;
        ///////////////////////


        Bitmap bitmap = General.loadBitmapFromView(view);
        MarkerOptions marker = new MarkerOptions()
                .title(name)
                .position(new LatLng(lat, lon))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        mMap.addMarker(marker).showInfoWindow();
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 12.0f));
        //
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().draggable(true).icon(BitmapDescriptorFactory.fromBitmap(bitmap)).position(sydney).title("Marker in Sydney"));
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setCompassEnabled(true);
//        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//            @Override
//            public void onMarkerDragStart(Marker marker) {
//
//            }
//
//            @Override
//            public void onMarkerDrag(Marker marker) {
//
//            }
//
//            @Override
//            public void onMarkerDragEnd(Marker marker) {
//                Toast.makeText(getApplicationContext(), marker.getPosition().latitude + "" + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
