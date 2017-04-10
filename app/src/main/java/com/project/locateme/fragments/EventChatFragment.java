package com.project.locateme.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.MessageModel;
import com.project.locateme.utilities.Constants;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventChatFragment extends Fragment {
    FirebaseListAdapter<MessageModel> myAdapter;
    ListView listOfMessages;
    @BindView(R.id.fragment_event_chat_text_edit)
    EditText messageText;
    @BindView(R.id.fragment_event_chat_send)
    FloatingActionButton sendMessage;
    private View view;
    private DatabaseReference mDatabase;
    private String eventName;
    private SharedPreferences sharedPreferences;
    private String userId;

    public EventChatFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_event_chat, container, false);
        ButterKnife.bind(this, view);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(getString(R.string.user_id), "3");
        mDatabase = FirebaseDatabase.getInstance()
                .getReference();
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View internalView) {
                //TODO : get username from shared Pref
                EditText input = (EditText) view.findViewById(R.id.fragment_event_chat_text_edit);
                mDatabase.child(eventName)
                        .child("chat")
                        .push()
                        .setValue(
                                new MessageModel(
                                        messageText.getText().toString(),
                                        "User Name", userId)
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HashMap<String, Object> parameters = (HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP);
        eventName = (String)parameters.get("eventName");
    }


}
