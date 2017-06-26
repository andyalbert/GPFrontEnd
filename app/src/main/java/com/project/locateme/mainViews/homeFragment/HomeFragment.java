package com.project.locateme.mainViews.homeFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.project.locateme.R;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Andrew
 * @since 25/1/2017
 * @version 2.0
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerClickListener {
    private Unbinder unbinder;
    private View view;
    private final String VOLLEY_TAG = "tag";
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private GoogleMap map;
    private final int LOCATION_UPDATE_REQUEST = 1;
    @BindView(R.id.map)
    MapView mapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        MapsInitializer.initialize(getActivity());
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(getActivity());

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            }

        //used on the app launch, to update the user location
        Location location = getLocation();
        if(location != null)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
        else
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sharedPreferences.getFloat("lat", 30.045915f), sharedPreferences.getFloat("long", 31.2220958f)), 16.0f)); // get the last, default is cairo tower
        //// TODO: 24/02/17 ask the user when signing in to get his position


        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        googleMap.setOnCameraIdleListener(this);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMarkerClickListener(this);
    }



    @Override
    public boolean onMyLocationButtonClick() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        return false;
    }


    @Override
    public void onCameraIdle() {
        updateMarkers();
       // Log.e("test", map.getProjection().getVisibleRegion().toString());
//        Toast.makeText(getActivity(), map.getProjection().getVisibleRegion().farLeft.latitude + "  " +
//        map.getProjection().getVisibleRegion().farRight.latitude, Toast.LENGTH_SHORT).show();
    }

    public void updateMarkers() {
        requestQueue.cancelAll(VOLLEY_TAG); //stop any previous requests that is still online
        VisibleRegion region = map.getProjection().getVisibleRegion();
        Uri uri = Uri.parse(Constants.GET_USERS_ON_MAP_REGION).buildUpon()
                .appendQueryParameter("lat1", String.valueOf(region.farLeft.latitude))
                .appendQueryParameter("lat2", String.valueOf(region.nearRight.latitude))
                .appendQueryParameter("lon1", String.valueOf(region.farLeft.longitude))
                .appendQueryParameter("lon2", String.valueOf(region.nearRight.longitude))
                .appendQueryParameter("userid", sharedPreferences.getString(getString(R.string.user_id), ""))
                .appendQueryParameter("pass", sharedPreferences.getString(getString(R.string.user_password), ""))
                .build();

        StringRequest request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                map.clear();
                try{
                    JSONArray array = new JSONArray(response);
                    JSONObject object;
                    Bitmap bitmap;

                    View myView = LayoutInflater.from(getActivity()).inflate(R.layout.map_marker, null);
                    for(int i = 0;i < array.length();i++){
                        object = array.getJSONObject(i);
                        Profile profile = new Profile();
                        profile.setUserId(object.getInt("user_Id"));
                        profile.setFirstName(object.getString("firstName"));
                        profile.setLastName(object.getString("lastName"));
                        profile.setEmail(object.getString("email"));
                        profile.setHomeTown(object.getString("homeTown"));
                        profile.setName(object.getString("name"));
                        profile.setBirthday(object.getString("birthday"));
                        profile.setPictureURL(object.getString("pictureURL"));

                        //// TODO: 25/04/17 uncomment when the shit pictures is done
                    Glide.with(getActivity().getApplicationContext())
                            .load(profile.getPictureURL())
                            .into((ImageView)ButterKnife.findById(myView, R.id.map_marker_user_image)); //// TODO: 31/01/17  this may cause an error, the image may be loaded after the view has been rendered, must check this

                        bitmap = General.loadBitmapFromView(myView);
                        map.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                .position(new LatLng(object.getDouble("latitude"), object.getDouble("longitude"))))
                                .setTag(profile);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "an error has occurred during updating map, please try again", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(VOLLEY_TAG);
        requestQueue.add(request);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //// TODO: 25/02/17 uncomment when the account is done
//        String id = (String) marker.getTag();
//        Intent intent = new Intent(getActivity(), UserProfileFragment.class);
//        intent.putExtra(getString(R.string.fragment_name, Constants.));
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("id", id);
//        intent.putExtra(Constants.HASHMAP, params);
//        startActivity(intent);
        marker.setTitle(((Profile)marker.getTag()).getName());
        return false;
    }

    private Location getLocation() {
        LocationManager mLocationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            if(mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER))
                return mLocationManager.getLastKnownLocation("gps");
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED)
            if(mLocationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER))
                return mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_UPDATE_REQUEST);
        return null;//// TODO: 24/02/17 change

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_UPDATE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                }
            }
        }
    } //todo this is useless till now

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        if(requestQueue != null)
            requestQueue.stop();
        super.onDestroy();
        unbinder.unbind();
    }

}
