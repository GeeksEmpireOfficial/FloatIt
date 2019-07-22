package net.geekstools.floatshort.PRO;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class CloudNotificationHandler extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (BuildConfig.DEBUG) {
            Log.d(">>> ", "From: " + remoteMessage.getFrom());
            Log.d(">>> ", "Notification Message Body: " + remoteMessage.getNotification().getBody());
        }
    }
}
