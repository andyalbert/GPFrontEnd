package com.project.locateme.fragments;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.project.locateme.R;
import com.project.locateme.dataHolder.eventsManager.Suggestion;
import com.project.locateme.dataHolder.userManagement.Profile;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.POST;
import retrofit2.http.Url;

public class ViewSuggestions extends Fragment {

    RequestQueue requestQueue ;
    Button acceptButton;
    Button declineButton;
    TextView suggestionDate;
    TextView suggestionUsername;
    private View view;
    private ListView suggestionsList;
    private ArrayList<Suggestion> suggestions;
    private ArrayList<Profile> profiles;
    private String eventId;
    private ListView suggestionListView;
    private SuggestionAdapter suggestionAdapter;
    private ArrayList<Suggestion> suggestionList;

    public ViewSuggestions() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_suggestions, container, false);
        ButterKnife.bind(this, view);
        HashMap<String, Object> parameters = (HashMap<String, Object>) getArguments().getSerializable(Constants.HASHMAP);
        requestQueue = Volley.newRequestQueue(getActivity());
        eventId = (String) parameters.get("eventId");
        suggestionList = new ArrayList<>();
        suggestionAdapter = new SuggestionAdapter(getActivity(), R.layout.fragment_view_suggestions, suggestionList);
        suggestionListView = (ListView) view.findViewById(R.id.fragment_suggestions_listview);
        suggestionListView.setAdapter(suggestionAdapter);
        getSuggestions();


        return view;
    }

    public void acceptSuggestion(String userId, String eventId , String suggId , final int index ){
        Uri uri = Uri.parse(Constants.ACCEPT_SUGGESTION).buildUpon()
                .appendQueryParameter("userid" , "1")//TODO : replace with userID : Admin of event(Shared Pref)
                .appendQueryParameter("eventid" , eventId)
                .appendQueryParameter("suggestionid" , suggId)
                .build();
        StringRequest request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    Log.e("accept" , response);
                    if (result.getString("operation").equalsIgnoreCase("Done")) {

                         suggestionList.remove(index);
                         suggestionAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity() , "Suggestion Accepted" , Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getActivity() , "An error occured , please try again later" , Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }
    public void declineSuggestion(String userId, String eventId , String suggId , final int index){
        Uri uri = Uri.parse(Constants.DECLINE_SUGGESTION).buildUpon()
                .appendQueryParameter("userid" , "1")//TODO : replace with userID : Admin of event(Shared Pref)
                .appendQueryParameter("eventid" , eventId)
                .appendQueryParameter("suggestionid" , suggId)
                .build();
        StringRequest request = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    Log.e("decline" , response);
                    if (result.getString("operation").equalsIgnoreCase("Done")) {

                        suggestionList.remove(index);
                        suggestionAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity() , "Suggestion Declined" , Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getActivity() , "An error occured , please try again later" , Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }
    public ArrayList<Suggestion> getSuggestions() {
        final ArrayList<Suggestion> list = new ArrayList<>();
        //TODO : get suggestions from backend
        // suggestion object : User name , Date
        Uri getSuggestionsUri = Uri.parse(Constants.GET_ALL_SUGGESTION).buildUpon()
                .appendQueryParameter("eventid", String.valueOf(eventId))
                .build();
        StringRequest request = new StringRequest(Request.Method.POST, getSuggestionsUri.toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                //todo :Parse Json
                JSONArray array = null;
                JSONObject arrayItem = null, profileJson = null;
                Suggestion suggestionItem;
                Log.e("res" , response);
                Profile profileItem;
                try {
                    array = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                arrayItem = new JSONObject();
                for (int i = 0; i < array.length(); i++) {
                    suggestionItem = new Suggestion();
                    profileItem = new Profile();
                    try {
                        arrayItem = array.getJSONObject(i);
                        suggestionItem.setId(arrayItem.getString("suggestion_id"));
                        suggestionItem.setDate(General.convertStringToTimestamp(arrayItem.getString("time")));
                        suggestionItem.setUserName(arrayItem.getJSONObject("profile").getString("firstName") +
                                arrayItem.getJSONObject("profile").getString("lastName"));
                        suggestionItem.setState(false);
                        suggestionItem.setUserId(arrayItem.getJSONObject("profile").getString("user_Id"));
                        list.add(suggestionItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                suggestionList.addAll(list);
                suggestionAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);

//        for(int i=0 ; i<5 ; i++){
//            Suggestion s = new Suggestion();
//            s.setState(false);
//            s.setDate(new Timestamp(new Long(45454534)));
//            //s.setUserId(5);
//            s.setUserName("Ahmed k");
//            list.add(s);
//        }

        return list;
    }

    class SuggestionAdapter extends ArrayAdapter<Suggestion> {
        Holder holder;
        private ArrayList<Suggestion> suggestions;
        private Context context;
        private int resource;

        public SuggestionAdapter(Context context, int resource, ArrayList<Suggestion> suggestions) {
            super(context, resource, suggestions);
            this.context = context;
            this.suggestions = suggestions;
            this.resource = resource;
        }

        @Override
        public int getCount() {
            return suggestions.size();
        }

        @Override
        public Suggestion getItem(int i) {
            return suggestions.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final int  index = i;
            if (view == null) {
                view = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_suggestion_list_item, viewGroup, false);
                holder = new Holder();
                holder.accept = (Button) view.findViewById(R.id.fragment_suggestion_accept);
                holder.decline = (Button) view.findViewById(R.id.fragment_suggestion_decline);
                holder.userName = (TextView) view.findViewById(R.id.fragment_suggestion_username);
                holder.date = (TextView) view.findViewById(R.id.fragment_suggestion_date);
                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       acceptSuggestion(suggestions.get(index).getUserId() ,eventId , suggestions.get(index).getId() , index );
                    }
                });
                holder.decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        declineSuggestion(suggestions.get(index).getUserId() ,eventId , suggestions.get(index).getId() , index);
                    }
                });

                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            holder.date.setText(suggestions.get(i).getDate().toString());
            holder.userName.setText(suggestions.get(i).getUserName());
            return view;
        }

        class Holder {
            TextView date;
            TextView userName;
            Button accept;
            Button decline;

        }
    }

}
