package org.die6sheeshs.projectx.restAPI;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingHandler extends FirebaseMessagingService {
    @Override
    public void onNewToken(String mToken) {
        super.onNewToken(mToken);
        Log.w("Info", "Device-Token: " + mToken);
        // Do whatever you want with your token now
        // i.e. store it on SharedPreferences or DB
        // or directly send it to server

        // TODO: Post on rest api for changing device token in the database

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    public static String getToken() {
        final String[] token = {""};
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("Error", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                token[0] = task.getResult();
            }
        });
        return token[0];
    }
}
