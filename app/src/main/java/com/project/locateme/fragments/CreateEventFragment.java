package com.project.locateme.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * @author khaled, andrew
 * @since 2/2/2017
 * @version 1.2
 */
public class CreateEventFragment extends Fragment {
    static final int DATE_DIALOG_ID = 999;
    public static String startTime;
    public static String deadline;
    private final int REQUEST_GALLERY_IMAGE = 2;
    private final int REQUEST_MAP_LOCATION = 44;
    @BindView(R.id.fragment_create_event_name)
    EditText eventName;
    @BindView(R.id.fragment_create_event_description)
    EditText eventDescription;
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
    private static boolean isFirstTime = true;
    private static Calendar firstTime;
    private static Calendar secondTime;
    private static View view;

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
        view = inflater.inflate(R.layout.fragment_create_event, container, false);
        ButterKnife.bind(this, view);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);
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
                createEvent();
            }
        });


        return view;

    }

    private void createEvent() {
        initializeModel();
        Uri uri = Uri.parse(Constants.CREATE_LOCATION).buildUpon()
                .appendQueryParameter("longitude", String.valueOf(eventLocationObject.getLongitude()))
                .appendQueryParameter("latitude", String.valueOf(eventLocationObject.getLatitude()))
                .appendQueryParameter("name", String.valueOf(eventLocationObject.getName()))
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
                //call create area
                createAreaNetworkcall();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_IMAGE) {
            if (resultCode == RESULT_OK) {
                imagePath = data.getStringExtra("path");
                Log.e("ImagePathCreate", imagePath);
                Uri imagePathUri = Uri.fromFile(new File(imagePath));
                reference.child(eventName.getText().toString());
                UploadTask uploadTask = reference.child(eventName.getText().toString()).putFile(imagePathUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") Uri downloadImage = taskSnapshot.getDownloadUrl();
                        eventArea.setImageURL(downloadImage.toString());
                    }
                });
                Log.i("Path", imagePath);
                Glide.with(getActivity()).load(imagePathUri).into(eventImage);

            }
        } else if (requestCode == REQUEST_MAP_LOCATION) {
            if (resultCode == RESULT_OK) {
                Log.e(data.getDoubleExtra("lat", 0.0) + "", "Lat");
                longitude = data.getDoubleExtra("long", 0.0);
                latitude = data.getDoubleExtra("lat", 0.0);
                radius = data.getDoubleExtra("radius", 0.0);
                Toast.makeText(getActivity(), "Location Added", Toast.LENGTH_LONG);
            }
        }
    }

    public void createAreaNetworkcall() {
        Uri uri = Uri.parse(Constants.CREATE_AREA).buildUpon()
                .appendQueryParameter("ownerid", "1")
                .appendQueryParameter("locationid", eventLocationObject.getId())
                .appendQueryParameter("redius", String.valueOf(radius))
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
                    //call create event fun
                    createEventNetworkcall();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void createEventNetworkcall() {
        Uri uri = Uri.parse(Constants.CREATE_EVENT)
                .buildUpon()
                .appendQueryParameter("name", model.getName())
                .appendQueryParameter("description", model.getDescription())
                .appendQueryParameter("radius", String.valueOf(eventArea.getRadius()))
                .appendQueryParameter("userid", sharedPreferences.getString(getString(R.string.user_id), ""))
                .appendQueryParameter("pass", sharedPreferences.getString(getString(R.string.user_password), ""))
                .appendQueryParameter("dateofevent", startTime)
                .appendQueryParameter("deadline", deadline)
                .appendQueryParameter("imageurl", imagePath)
                .appendQueryParameter("state", "true") //// TODO: 27/03/17 really ? are you kidding me -_-
                .appendQueryParameter("locationid", eventLocationObject.getId())
                .build();

        stringRequest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject returnedData = new JSONObject(response);

                    model.setId(returnedData.getString("event_id"));

                    Toast.makeText(getActivity(), "Event created Successfuly", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), HolderActivity.class);
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("eventModel", model);
                    params.put("userStatus", EventFragment.UserState.OWNER);
                    intent.putExtra(Constants.HASHMAP, params);
                    intent.putExtra(getString(R.string.fragment_name), Constants.EVENT_FRAGMENT);
                    startActivity(intent);
                    getActivity().finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "An Error occurred. Try Again Later ", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        requestQueue.add(stringRequest);
    }

    public void initializeModel() {

        //TODO : get it from the add zone
        eventLocationObject = new Location(longitude, latitude, eventName.getText().toString());
        model.setName(eventName.getText().toString());
        model.setDescription(eventDescription.getText().toString());
        // get current Date ( Date of creation)
        model.setDateOfEvent(General.convertStringToTimestamp(startTime));
        eventArea.setImageURL(imagePath);
        eventArea.setRadius(radius);
        eventArea.setLocation(eventLocationObject);
        model.setState(false);
        model.setDeadline(General.convertStringToTimestamp(deadline));
        model.setArea(eventArea);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            //edit: just disable the past days
            DatePickerDialog da = new DatePickerDialog(getActivity(), this,
                    year, month, day);
            c.add(Calendar.DATE, 1);
            Date newDate = c.getTime();
            da.getDatePicker().setMinDate(newDate.getTime());

            return da;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd'T'HH:mm:ss.SSS
            startTime = sdf.format(c.getTime());
            deadline = startTime;
            TimePickerFragment timePicker = new TimePickerFragment();
            timePicker.show(getFragmentManager(), "timePicker");
        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, false);
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);

            SimpleDateFormat sdf = new SimpleDateFormat("'T'HH:mm:ss.SSS");//yyyy-MM-dd'T'HH:mm:ss.SSS
            if(isFirstTime){
                startTime += sdf.format(c.getTime());
                firstTime = c;
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getFragmentManager(), "timePicker");
            } else{
                deadline += sdf.format(c.getTime());
                Calendar c2 = Calendar.getInstance();
                secondTime = c;

                LinearLayout layout = ButterKnife.findById(view, R.id.fragment_create_event_selection_description);
                TextView finishTime = ButterKnife.findById(view, R.id.fragment_create_event_event_finish_time);
                TextView startTime = ButterKnife.findById(view, R.id.fragment_create_event_event_start_time);
                TextView date = ButterKnife.findById(view, R.id.fragment_create_event_event_date);
                if(secondTime.compareTo(firstTime) <= 0){
                    Toast.makeText(getActivity(), "Event end time must be after start time", Toast.LENGTH_SHORT).show();
                    layout.setVisibility(View.INVISIBLE);
                } else{
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"), dateFormat = new SimpleDateFormat("yyyy:MM:dd");

                    date.setText(CreateEventFragment.startTime.substring(0, 10));
                    startTime.setText(timeFormat.format(CreateEventFragment.firstTime.getTime()));
                    finishTime.setText(timeFormat.format(CreateEventFragment.secondTime.getTime()));
                    layout.setVisibility(View.VISIBLE);
                }
            }
            isFirstTime = !isFirstTime;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_event, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create_event_save:
                createEvent();
        }
        return super.onOptionsItemSelected(item);
    }
}

