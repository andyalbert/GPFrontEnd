package com.project.locateme.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.project.locateme.ImagePickerActivity;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Event;
import com.project.locateme.utilities.DatePickerFragment;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;

public class CreateEventFragment extends Fragment {
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


    private Event model;
    private Calendar calender;
    static final int DATE_DIALOG_ID=999;
    public  static String formattedDate;
    int year , month , day;


    public CreateEventFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        model = new Event();
        calender = Calendar.getInstance();
        year = calender.get(Calendar.YEAR);
        month = calender.get(Calendar.MONTH);
        day = calender.get(Calendar.DAY_OF_MONTH);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        eventLocation = (Button)view.findViewById(R.id.fragment_create_event_location) ;
        eventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //TODO : Open Map for Location
            }
        });
        pickDate = (Button)view.findViewById(R.id.fragment_create_event_pick_date);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment picker = new DatePickerFragment();

                picker.show(getFragmentManager(), "datePicker" );
                //eventDate.setText(formattedDate);
            }
        });
        submitEvent = (Button)view.findViewById(R.id.fragment_create_event_submit);
        submitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                model.setName(eventName.getText().toString());
//                model.setDescription(eventDescription.getText().toString());
//                model.setDateOfEvent(timestamp);
                //TODO: Network Call
            }
        });
        // Inflate the layout for this fragment
        return view ;

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

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = sdf.format(c.getTime());
            ((TextView) getActivity().findViewById(R.id.fragment_create_event_date)).setText(formattedDate);
        }
    }

}
