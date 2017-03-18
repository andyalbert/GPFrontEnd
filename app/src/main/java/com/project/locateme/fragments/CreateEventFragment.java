package com.project.locateme.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.locateme.HolderActivity;
import com.project.locateme.ImagePickerActivity;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.dataHolder.locationManager.Area;
import com.project.locateme.dataHolder.locationManager.Location;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class CreateEventFragment extends Fragment {
    static final int DATE_DIALOG_ID = 999;
    public static String formattedDate;
    private final int REQUEST_GALLERY_IMAGE = 2;
    private final int REQUEST_MAP_LOCATION = 44;
    @BindView(R.id.fragment_create_event_name)
    EditText eventName;
    @BindView(R.id.fragment_create_event_description)
    EditText eventDescription;
    @BindView(R.id.fragment_create_event_date)
    TextView eventDate;
    @BindView(R.id.fragment_create_event_location)
    Button eventLocation;
    @BindView(R.id.fragment_create_event_submit)
    Button submitEvent;
    @BindView(R.id.fragment_create_event_pick_date)
    Button pickDate;
    @BindView(R.id.fragment_create_event_image)
    ImageView eventImage;
    int year, month, day;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private SharedPreferences sharedPreferences;
    private Event model;
    private Calendar calender;
    private String imagePath;
    private Area eventArea;
    private double longitude;
    private double latitude;
    private double radius;
    private Location eventLocationObject;
    private StorageReference reference;

    public CreateEventFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        model = new Event();
        calender = Calendar.getInstance();
        year = calender.get(Calendar.YEAR);
        month = calender.get(Calendar.MONTH);
        day = calender.get(Calendar.DAY_OF_MONTH);
        requestQueue = Volley.newRequestQueue(getActivity());
        reference = FirebaseStorage.getInstance().getReference();
        eventArea = new Area();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        ButterKnife.bind(this, view);
        eventLocation = (Button) view.findViewById(R.id.fragment_create_event_location);
        eventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : Open Map for Location
                startActivityForResult(new Intent(getActivity(), HolderActivity.class).putExtra(getString(R.string.fragment_name), Constants.SELECT_ZONE_FRAGMENT),
                        REQUEST_MAP_LOCATION);
            }
        });
        pickDate = (Button) view.findViewById(R.id.fragment_create_event_pick_date);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
            }
        });
        eventImage = (ImageView) view.findViewById(R.id.fragment_create_event_image);
        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : Open gallery for image select and save in model
                startActivityForResult(new Intent(getActivity(), ImagePickerActivity.class), 2);

            }
        });
        submitEvent = (Button) view.findViewById(R.id.fragment_create_event_submit);
        submitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeModel();
                Uri uri = null;
                uri = Uri.parse(Constants.CREATE_LOCATION).buildUpon()
                .appendQueryParameter("longitude" , String.valueOf(eventLocationObject.getLongitude())).appendQueryParameter("latitude" , String.valueOf(eventLocationObject.getLatitude()))
                .appendQueryParameter("name" , String.valueOf(eventLocationObject.getName()))
                .build();
                stringRequest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            eventLocationObject.setId(jsonObject.getString("location_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
                        return params;
                    }
                };
                requestQueue.add(stringRequest);

                uri = Uri.parse(Constants.CREATE_AREA).buildUpon()
                        .appendQueryParameter("ownerid" , "1")
                        .appendQueryParameter("locationid" , eventLocationObject.getId())
                        .appendQueryParameter("redius" , String.valueOf(radius))
                        .build();
                stringRequest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            eventArea.setId(json.getString("area_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
                uri = Uri.parse(Constants.CREATE_EVENT)
                        .buildUpon().
                                appendQueryParameter("name", model.getName())
                        .appendQueryParameter("description", model.getDescription())
                        .appendQueryParameter("radius", String.valueOf(eventArea.getRadius()))
                        .appendQueryParameter("userid", "2")
                        .appendQueryParameter("dateofevent", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(new Date()))
                        .appendQueryParameter("deadline", formattedDate)
                        .appendQueryParameter("imageurl", imagePath)
                        .appendQueryParameter("state", "true")
                        .appendQueryParameter("locationid", eventLocationObject.getId())
                        .build();
                stringRequest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject returnedData = new JSONObject(response);

                            model.setId(returnedData.getString("event_id"));

                            Toast.makeText(getActivity(), "Event created Successfuly", Toast.LENGTH_LONG);
                            Intent intent = new Intent(getActivity(), HolderActivity.class);
                            HashMap<String, Object> params = new HashMap<String, Object>();
                            params.put("eventModel", model);
                            intent.putExtra(Constants.HASHMAP, params);
                            intent.putExtra(getString(R.string.fragment_name), Constants.EVENT_FRAGMENT);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "An Error occurred. Try Again Later ", Toast.LENGTH_LONG);
                        error.printStackTrace();
                    }
                }){
                        @Override
                        public Map<String , String> getParams() throws AuthFailureError{
                            return new HashMap<String, String>();
                        }
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }
        });

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_IMAGE) {
            if (resultCode == RESULT_OK) {
                imagePath = data.getStringExtra("path");
                Log.e("ImagePathCreate", imagePath);
                Uri imagePathUri = Uri.fromFile(new File(imagePath));
                reference.child(eventName.getText().toString()).putFile(imagePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") Uri cc = taskSnapshot.getDownloadUrl();
                        imagePath = cc.toString();

                    }
                });
                Log.i("Path", imagePath);
                Glide.with(getActivity()).load(imagePathUri).into(eventImage);

            }
        } else if (requestCode == REQUEST_MAP_LOCATION) {
            if (resultCode == RESULT_OK) {
                HashMap<String, Double> result = (HashMap<String, Double>) data.getSerializableExtra("result");
                Log.e(result.get("lat").toString(), "Lat");
                longitude = result.get("long");
                latitude = result.get("lat");
                radius = result.get("radius");
                Toast.makeText(getActivity(), "Location Added", Toast.LENGTH_LONG);
            }
        }
    }

    public void initializeModel() {

        //TODO : get it from the add zone
        eventLocationObject = new Location(longitude, latitude, eventName.toString());
        model.setName(eventName.getText().toString());
        model.setDescription(eventDescription.getText().toString());
        // get current Date ( Date of creation)
        String todaysDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(new Date());
        model.setDateOfEvent(General.convertStringToTimestamp(todaysDate));
        eventArea.setImageURL(imagePath);
        eventArea.setRadius(radius);
        eventArea.setLocation(eventLocationObject);
        model.setState(false);
        model.setDeadline(General.convertStringToTimestamp(formattedDate));
        model.setArea(eventArea);

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            formattedDate = sdf.format(c.getTime());
            ((TextView) getActivity().findViewById(R.id.fragment_create_event_date)).setText(formattedDate);
        }
    }

}
