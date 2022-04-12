package org.die6sheeshs.projectx.restAPI;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingHandler extends FirebaseMessagingService {
    @Override
    public void onNewToken(String mToken) {
        super.onNewToken(mToken);
        System.out.println("Device-Token: " + mToken);
        // Do whatever you want with your token now
        // i.e. store it on SharedPreferences or DB
        // or directly send it to server

        // TODO: Post on rest api for changing device token in the database
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
