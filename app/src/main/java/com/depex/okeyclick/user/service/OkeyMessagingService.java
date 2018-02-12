package com.depex.okeyclick.user.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by we on 2/9/2018.
 */

public class OkeyMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String address=remoteMessage.getFrom();
        Log.i("remoteMessage","From : " +address);
        Log.i("remoteMessage", "Message Data Payload : "+remoteMessage.getData());

    }
}
