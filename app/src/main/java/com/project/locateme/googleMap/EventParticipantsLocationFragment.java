package com.project.locateme.googleMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.locateme.R;
import com.project.locateme.dataHolder.locationManager.Area;
import com.project.locateme.dataHolder.locationManager.Location;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author andrew
 * @version 1.0
 * @since 3/5/2017
 */

public class EventParticipantsLocationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private Unbinder unbinder;
    private View view;
    private SharedPreferences preferences;
    private RequestQueue queue;
    private String eventId;
    private Location location;
    private double radius;
    private GoogleMap map;
    private String VOLLEY_TAG = "tag";
    private ImageView userProfileImageView;

    @BindView(R.id.fragment_view_zone_map)
    MapView mapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Participants locations");
        view = inflater.inflate(R.layout.fragment_view_zone, container, false);
        unbinder = ButterKnife.bind(this, view);

        HashMap<String, Object> params = ((HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP));
        eventId = (String) params.get("eventId");
        radius = (double) params.get("radius");
        location = (Location) params.get("location");

        preferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(getActivity());

        MapsInitializer.initialize(this.getActivity());
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.addCircle(new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(radius)
                .strokeColor(Constants.STROKE_COLOR)
                .strokeWidth(Constants.STROKE_WIDTH)
                .fillColor(Constants.FILL_COLOR));

        getUsersCurrentLocation();
    }

    private void getUsersCurrentLocation() {
        Uri uri = Uri.parse(Constants.GET_EVENT_PARTICIPANTS_AND_OWNER_CURRENT_LOCATION).buildUpon()
                .appendQueryParameter("eventid", String.valueOf(eventId))
                .appendQueryParameter("userId", preferences.getString(getString(R.string.user_id), ""))
                .appendQueryParameter("pass", preferences.getString(getString(R.string.user_password), ""))
                .build();

        StringRequest request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object = array.getJSONObject(i);
                        final Profile profile = new Profile();
                        profile.setUserId(object.getInt("user_Id"));
                        profile.setFirstName(object.getString("firstName"));
                        profile.setLastName(object.getString("lastName"));
                        profile.setEmail(object.getString("email"));
                        profile.setHomeTown(object.getString("homeTown"));
                        profile.setName(object.getString("name"));
                        profile.setBirthday(object.getString("birthday"));
                        profile.setPictureURL(object.getString("pictureURL"));

                        final View myView = LayoutInflater.from(getActivity()).inflate(R.layout.map_marker, null);
                        final double lat = object.getDouble("latitude"), lon = object.getDouble("longitude");
                        userProfileImageView = (ImageView) myView.findViewById(R.id.map_marker_user_image);
                        Glide.with(getActivity())
                                .load(profile.getPictureURL())
                                .placeholder(getContext().getResources().getDrawable(R.mipmap.ic_profile_original))
                                .into((ImageView) myView.findViewById(R.id.map_marker_user_image));
//                                .into(new GlideDrawableImageViewTarget(userProfileImageView){
//                                    @Override
//                                    protected void setResource(GlideDrawable resource) {
//                                        super.setResource(resource);
//                                        Bitmap bitmap = General.loadBitmapFromView(myView);
//
//                                        map.addMarker(new MarkerOptions()
//                                                .position(new LatLng(lat, lon))
//                                                .title(profile.getName())
//                                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))).setTag(profile);
//                                    }
//                                });

                        Bitmap bitmap = General.loadBitmapFromView(myView);

                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lon))
                                .title(profile.getName())
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))).setTag(profile);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Couldn't load participants locations, please try again", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        request.setTag(VOLLEY_TAG);
        queue.add(request);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
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
        if (queue != null)
            queue.cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        unbinder.unbind();
    }
}
