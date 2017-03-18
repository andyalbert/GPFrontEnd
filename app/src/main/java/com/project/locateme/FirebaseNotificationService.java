package com.project.locateme;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.locateme.utilities.General;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by khaled on 08/03/17.
 */

public class FirebaseNotificationService extends FirebaseMessagingService {
    private final String pushNotification = "pushNotification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        if (remoteMessage == null)
            return;
        if (remoteMessage.getNotification() != null) {
            Log.e("Notification Body: " , remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }
        if(remoteMessage.getData().size() >0 ){
            try {
                handleData(new JSONObject(remoteMessage.getData().toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void handleNotification(String message){
        if(!General.isAppInBackground(getApplicationContext())){
            //Check if app is in background or not to Handle the Noti
            Intent pushNotification = new Intent();
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            try {
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            //Let the firabase do the work
        }

    }
    public void handleData(JSONObject notificationData){
        //TODO : This differs from one notification to another
        try {
            JSONObject data = notificationData.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
