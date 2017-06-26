package com.project.locateme.googleMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.locateme.HolderActivity;
import com.project.locateme.R;
import com.project.locateme.dataHolder.locationManager.Area;
import com.project.locateme.dataHolder.locationManager.Location;
import com.project.locateme.dataHolder.userManagement.Account;
import com.project.locateme.dataHolder.userManagement.History;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.models.android.specific.UserLocation;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @since 2/2/2017
 * @version 1.0
 * @author andrew
 */

public class ViewZoneFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private Unbinder unbinder;
    private GoogleMap map;
    private View view;
    private HashMap<String, Object> parameters;
    private Area area;
    private StringRequest request;
    private RequestQueue queue;
    private SharedPreferences preferences;
    private ArrayList<UserLocation> usersLocation;

    @BindView(R.id.fragment_view_zone_map)
    MapView mapView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_view_zone, container, false);
        unbinder = ButterKnife.bind(this, view);

        parameters = (HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP);
        preferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        area = (Area) parameters.get("area");

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(area.getLocation().getName());

        queue = Volley.newRequestQueue(getActivity());

        MapsInitializer.initialize(this.getActivity());
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    private void getUsersCurrentLocation() {
        Uri uri = Uri.parse(Constants.GET_ZONE_FRIENDS_CURRENT_LOCATION).buildUpon()
                .appendQueryParameter("userid", preferences.getString(getString(R.string.user_id), ""))
                .appendQueryParameter("pass", preferences.getString(getString(R.string.user_password), ""))
                .appendQueryParameter("areaid", area.getId())
                .build();

        request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                usersLocation = new ArrayList<>();
                UserLocation currentUser;
                Location location;
                JSONObject currentObject;
                try{
                    JSONArray array = new JSONObject(response).getJSONArray("object");

                    for(int i = 0;array != null && i < array.length();i++){
                        currentObject = array.getJSONObject(i);
                        currentUser = new UserLocation();

                        currentUser.setUserId(currentObject.getInt("id"));
                        location = new Location();
                        location.setLongitude(currentObject.getDouble("longitude"));
                        location.setLatitude(currentObject.getDouble("latitude"));

                        currentUser.setLocation(location);
                        usersLocation.add(currentUser);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                //now Update the users you have with their locations
                for(int j = 0;j < area.getAccounts().size();j++)
                    match(area.getAccounts().get(j));
                drawAndFocus();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "error loading your friends locations, please try again", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    /**
     * this method is responsible for matching the profile with their current locations
     * @param profile
     */
    private void match(Profile profile) {
        int id = profile.getUserId();
        int lower, upper, mid = 0;
        lower = 0;
        upper = usersLocation.size() - 1;
        while(lower <= upper){
            mid = (lower + upper) / 2;
            if(usersLocation.get(mid).getUserId() == id)
                break;
            else if (usersLocation.get(mid).getUserId() > id)
                upper = mid - 1;
            else
                lower = mid + 1;
        }
        History history = new History();
        history.setLocation(usersLocation.get(mid).getLocation());
        history.setTimestamp(usersLocation.get(mid).getLastLocationUpdate());
        profile.setHistory(history);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);
        getUsersCurrentLocation();
    }

    private void drawAndFocus() {
        //first, draw the area itself
        LatLng circleLatLong = new LatLng(area.getLocation().getLatitude(), area.getLocation().getLongitude());
        map.addCircle(new CircleOptions()
                        .strokeColor(Constants.STROKE_COLOR)
                        .strokeWidth(Constants.STROKE_WIDTH)
                        .fillColor(Constants.FILL_COLOR)
                        .radius(area.getRadius())
                        .center(circleLatLong));

        //second, draw the users locations
        View myView = LayoutInflater.from(getActivity()).inflate(R.layout.map_marker, null);
        Profile profile;
        for(int i = 0;i < area.getAccounts().size();i++){
            profile = area.getAccounts().get(i);
            LatLng latLng = new LatLng(profile.getHistory().getLocation().getLatitude(), profile.getHistory().getLocation().getLongitude());
            Bitmap bitmap = General.loadBitmapFromView(myView);
            //// TODO: 10/03/17 uncomment when image is ready
            Glide.with(getActivity())
                    .load(profile.getPictureURL())
                    .into((ImageView)myView.findViewById(R.id.map_marker_user_image));
            map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
//                            .title(profile.getHistory().getTimestamp().toString()))//// TODO: 08/03/17 the timestamp should be more rational
                    ).setTag(profile);
        }

        //finally, set the map for the user
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(circleLatLong, 12.0f)); //// TODO: 08/03/17 may be changed to match the circle zone, it there is time
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setRotateGesturesEnabled(false);
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
    }

    /**
     * opens the user account
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        //silly, so will be replaced with just the name
//        Intent intent = new Intent(getActivity(), HolderActivity.class);
//        intent.putExtra(getString(R.string.fragment_name), Constants.PROFILE_FRAGMENT);
//        intent.putExtra(Constants.HASHMAP, new HashMap(){{put("profile", (Profile)marker.getTag());}});
//        startActivity(intent);
        marker.setTitle(((Profile)marker.getTag()).getName());
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        unbinder.unbind();
    }
}
