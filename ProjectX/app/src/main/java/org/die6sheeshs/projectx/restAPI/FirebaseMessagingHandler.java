package org.die6sheeshs.projectx.restAPI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.fragments.OurToast;
import org.die6sheeshs.projectx.helpers.SessionManager;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class FirebaseMessagingHandler extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingHandler";
    private static final String CHANNEL_ID = "PROJECTX_NOTIFICATION";

    @Override
    public void onNewToken(String mToken) {
        super.onNewToken(mToken);
        Log.e("FirebaseMessaging", "Device-Token: " + mToken);
        // Do whatever you want with your token now
        // i.e. store it on SharedPreferences or DB
        // or directly send it to server

        //Post on rest api for changing device token in the database
        uploadFirebaseTokenToServer(mToken);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
////                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
////                handleNow();
//            }
//
//        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            displayNotification(remoteMessage.getNotification());
        }
    }

    private void displayNotification(RemoteMessage.Notification cloudNotification) {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "ProjectX Notification",
                NotificationManager.IMPORTANCE_HIGH
        );
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(cloudNotification.getTitle())
                .setContentText(cloudNotification.getBody())
                .setSmallIcon(R.drawable.ic_app_icon)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(187, notification.build());
    }

    public static void uploadFirebaseTokenToServer(String token) {
        User user = SessionManager.getInstance().getUser();
        user.setFirebaseToken(token);
        Observable<ResponseBody> response = UserPersistence.getInstance().updateUser(user);
        response.subscribeOn(Schedulers.io())
                .doOnError(error -> Log.v(TAG, error.getMessage()));
    }
}
