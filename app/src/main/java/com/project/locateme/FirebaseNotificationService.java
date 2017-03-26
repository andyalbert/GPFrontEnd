package com.project.locateme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.games.internal.api.StatsImpl;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.locateme.utilities.Constants;
import com.project.locateme.utilities.General;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by khaled on 08/03/17.
 */

public class FirebaseNotificationService extends FirebaseMessagingService {
    private final String pushNotification = "pushNotification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        if (remoteMessage == null)
            return;
//        if (remoteMessage.getNotification() != null) {
//            Log.e("Notification Body: " , remoteMessage.getNotification().getBody());
//            handleNotification(remoteMessage.getNotification());
//        }
        if(remoteMessage.getData().size() >0 ){

                handleNotification(remoteMessage.getData());

        }
    }
    public void handleNotification(Map<String , String> data){
        //if(!General.isAppInBackground(getApplicationContext())){
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            Intent intent = new Intent(this, HolderActivity.class);
            intent.putExtra("fragment name" , data.get("activity"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(data.get("title"))
                    .setContentText(data.get("text"))
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(icon)
                    .setSmallIcon(R.mipmap.ic_launcher);
            notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());



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
