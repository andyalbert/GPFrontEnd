package com.project.locateme.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

    public EventChatFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_event_chat, container, false);
        ButterKnife.bind(this, view);
        mDatabase = FirebaseDatabase.getInstance()
                .getReference();
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : get username from shared Pref
                EditText input = (EditText) view.findViewById(R.id.fragment_event_chat_text_edit);
                mDatabase.child(eventName)
                        .child("chat")
                        .push()
                        .setValue(
                                new MessageModel(
                                        messageText.getText().toString(),
                                        "User Name")
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
                TextView messageUser = (TextView) v.findViewById(R.id.chat_message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.chat_message_time);

                // Set Message text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

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
