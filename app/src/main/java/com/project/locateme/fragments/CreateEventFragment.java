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
import com.project.locateme.HolderActivity;
import com.project.locateme.ImagePickerActivity;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import org.json.JSONException;
import org.json.JSONObject;

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

public class CreateEventFragment extends Fragment implements GoogleMap.OnMapClickListener {
    static final int DATE_DIALOG_ID = 999;
    public static String formattedDate;
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

    private final int REQUEST_GALLERY_IMAGE = 2;
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
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        ButterKnife.bind(this , view);
        eventLocation = (Button) view.findViewById(R.id.fragment_create_event_location);
        eventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : Open Map for Location
                startActivity(new Intent(getActivity(), HolderActivity.class).putExtra(getString(R.string.fragment_name), Constants.SELECT_ZONE_FRAGMENT));
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
                //TODO: Network Call
                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("id", sharedPreferences.getString(getString(R.string.user_id), ""));
//                    jsonObject.put("pass", sharedPreferences.getString(getString(R.string.user_password), ""));
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                Uri uri = Uri.parse(Constants.CREATE_EVENT)
                        .buildUpon().
                        appendQueryParameter("name" ,model.getName())
                        .appendQueryParameter("description" , model.getDescription())
                        .appendQueryParameter("radius" , "12")
                        .appendQueryParameter("userid" , "2")
                        .appendQueryParameter("dateofevent" , new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(new Date()))
                        .appendQueryParameter("deadline" , formattedDate)
                        .appendQueryParameter("imageurl" , imagePath)
                        .appendQueryParameter("state" , "true")
                        .appendQueryParameter("locationid" , "2")
                        .build();
                URL url = null;
                try {
                    url = new URL(uri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                URI uriVar = null;
                try {
                    uriVar = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
              Log.e("Link" , uriVar.toString());
                stringRequest = new StringRequest(Request.Method.POST, uriVar.toString(), new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject returnedData = new JSONObject(response);

                            model.setId(returnedData.getInt("event_id"));

                            Toast.makeText(getActivity() , "Event created Successfuly" , Toast.LENGTH_LONG);
                            Intent intent = new Intent(getActivity() , HolderActivity.class);
                            HashMap<String , Object> params = new HashMap<String, Object>();
                            params.put("eventModel" , model);
                            intent.putExtra(Constants.HASHMAP ,params );
                            intent.putExtra(getString(R.string.fragment_name) , Constants.EVENT_FRAGMENT);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity() , "An Error occurred. Try Again Later " , Toast.LENGTH_LONG);
                        error.printStackTrace();
                    }
                } );

                requestQueue.add(stringRequest);
            }
        });

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {

                imagePath = data.getStringExtra("path");
                Log.i("Path", imagePath);
                Glide.with(getActivity()).load(imagePath).into(eventImage);

            }
        }
    }
    public void initializeModel(){
        model.setName(eventName.getText().toString());
        model.setDescription(eventDescription.getText().toString());
        // get current Date ( Date of creation)
        String todaysDate =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(new Date());
        model.setDateOfEvent(General.convertStringToTimestamp(todaysDate));
        model.setImageURL(imagePath);
        model.setState(false);
        model.setDeadline(General.convertStringToTimestamp(formattedDate));

    }

    @Override
    public void onMapClick(LatLng latLng) {

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
