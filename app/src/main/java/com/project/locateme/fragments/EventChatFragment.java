package com.project.locateme.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.MessageModel;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventChatFragment extends Fragment {
    FirebaseListAdapter<MessageModel> myAdapter;
    ListView listOfMessages;
    @BindView(R.id.fragment_event_chat_text_edit)
    EditText messageText;
    @BindView(R.id.fragment_event_chat_send)
    FloatingActionButton sendMessage;
    private Unbinder unbinder;
    private View view;
    private DatabaseReference mDatabase;
    private String eventName;
    private SharedPreferences sharedPreferences;
    private String userId;
    private StringRequest request;
    private RequestQueue queue;
    private Calendar currentTime;
    private Calendar eventDeadLine;
    private boolean isRegistered;
    private final String VOLLEY_TAG = "tag";
    private int trialTimes = 0;
    private final int MAX_CONNECTION_TRIALS = 3;
    private BroadcastReceiver minutePassedReceiver;

    public EventChatFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Chat");
        view = inflater.inflate(R.layout.fragment_event_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        isRegistered = false;
        sendMessage.setEnabled(false);

        queue = Volley.newRequestQueue(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        getServerRealTime();
        userId = sharedPreferences.getString(getString(R.string.user_id), "");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View internalView) {
                EditText input = (EditText) view.findViewById(R.id.fragment_event_chat_text_edit);
                mDatabase.child(eventName)
                        .child("chat")
                        .push()
                        .setValue(
                                new MessageModel(
                                        messageText.getText().toString(),
                                        sharedPreferences.getString(getString(R.string.user_name), ""), userId)
                        );

                // Clear the input
                input.setText("");
            }
        });

        listOfMessages = (ListView) view.findViewById(R.id.fragment_event_chat_list_of_messages);
        myAdapter = new FirebaseListAdapter<MessageModel>(getActivity(), MessageModel.class, R.layout.chat_message_item,
                FirebaseDatabase.getInstance().getReference().child(eventName).child("chat")) {
            @Override
            protected void populateView(View v, MessageModel model, int position) {

                TextView messageText = (TextView) v.findViewById(R.id.chat_message_text);
                TextView messageTime = (TextView) v.findViewById(R.id.chat_message_time);

                if(!model.getUserId().equals(userId)){
                    ((TextView) v.findViewById(R.id.chat_message_user)).setText(model.getMessageUser());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(getResources().getDimension(R.dimen.chat_box_size)), LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.START;
                    params.leftMargin = (int) (getResources().getDimension(R.dimen.margin));
                    v.findViewById(R.id.chat_message_internal_layout).setLayoutParams(params);
                }
                else
                    v.findViewById(R.id.chat_message_user).setVisibility(View.INVISIBLE);
                // Set Message text
                messageText.setText(model.getMessageText());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }


        };
        listOfMessages.setAdapter(myAdapter);
        return view;
    }

    /**
     * this method is responsible for getting the server time, then upon success, it will start a receiver that will update the time
     * each time a minute pass, so the messages will be disabled once the event time is off
     */
    private void getServerRealTime() {
        Uri uri = Uri.parse(Constants.GET_SERVER_REAL_TIME).buildUpon()
                .appendQueryParameter("userID", sharedPreferences.getString(getString(R.string.user_id), ""))
                .appendQueryParameter("pass", sharedPreferences.getString(getString(R.string.user_password), ""))
                .build();

        request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String timeString = ((String) object.getString("Time")).replace(" ", "T");
                    Timestamp time = General.convertStringToTimestamp(timeString);
                    currentTime = Calendar.getInstance();
                    currentTime.setTimeInMillis(time.getTime());
                    if(currentTime.compareTo(eventDeadLine) >= 0){

                        return;
                    }
                    minutePassedReceiver = getReceiver();
                    getActivity().registerReceiver(minutePassedReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
                    isRegistered = true;
                    sendMessage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));
                    sendMessage.setEnabled(true);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(trialTimes++ == MAX_CONNECTION_TRIALS){
                    Toast.makeText(getActivity().getApplicationContext(), "Couldn't get server time, please check your connection and try again", Toast
                    .LENGTH_LONG).show();
                    getActivity().finish();

                }
                getServerRealTime();
            }
        });
        request.setTag(VOLLEY_TAG);
        queue.add(request);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HashMap<String, Object> parameters = (HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP);
        eventName = (String)parameters.get("eventName");
        eventDeadLine = Calendar.getInstance();
        eventDeadLine.setTimeInMillis(((Timestamp) parameters.get("deadline")).getTime());
    }

    private BroadcastReceiver getReceiver(){
         return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                    currentTime.add(Calendar.MINUTE, 1);
                    if(currentTime.compareTo(eventDeadLine) >= 0){
                        sendMessage.setEnabled(false);
                        sendMessage.setBackgroundTintList(getResources().getColorStateList(R.color.pending_color));
                        isRegistered = false;
                        getActivity().unregisterReceiver(minutePassedReceiver);
                        minutePassedReceiver = null;
                    }
                }
            }
        };
    }

    @Override
    public void onDestroyView() {
        if(queue != null)
            queue.cancelAll(VOLLEY_TAG);
        if(isRegistered)
            getActivity().unregisterReceiver(minutePassedReceiver);
        super.onDestroyView();
        unbinder.unbind();
    }
}
