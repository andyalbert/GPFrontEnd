package com.project.locateme.utilities;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by khaled on 07/03/17.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    public static String token = null;
    @Override
    public void onTokenRefresh(){
        token = FirebaseInstanceId.getInstance().getToken();
        Log.i("token : " , token);
        updateTokenOnServer();
    }

    public void updateTokenOnServer(){
        //TODO : Send request to update the token in backend;
    }


}
