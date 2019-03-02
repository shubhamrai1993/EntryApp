package com.example.entryapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class EntryRequestMessagingService extends FirebaseMessagingService {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        SharedPrefsLocalManager.storeEntryRequestId(this, remoteMessage.getData().get("entryRequestId"));
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        Notification notification = new Notification.Builder(EntryRequestMessagingService.this)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setChannelId(NotificationChannel.DEFAULT_CHANNEL_ID)
                .build();
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1 , notification);

        Log.i(EntryRequestMessagingService.class.getName(), "Message received: " + remoteMessage.getNotification().getBody());
    }
}
