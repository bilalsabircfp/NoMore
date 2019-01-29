package com.bilal.dzone.fiverr_security_app.notification;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by DzonE on 21-Oct-17.
 */

public class ReceiveNotification extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e("Tag", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Log.e("Tag", "Message data payload: " + remoteMessage.getData());


            //get data from notification on foreground
//            String a1,a2,a3;
//            a1 = remoteMessage.getData().get("name");
//            a2 = remoteMessage.getData().get("lat");
//            a3 = remoteMessage.getData().get("lon");
//            Intent intent = new Intent("com.example.dzone.blood_donation_FCM-MESSAGE");
//            intent.putExtra("a", a1);
//            intent.putExtra("b", a2);
//            intent.putExtra("c", a3);
//            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
//            broadcastManager.sendBroadcast(intent);
//
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.e("Tag", "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        } else {
//            Log.e("Tag", "No Data");
//
        }


    }


    }


