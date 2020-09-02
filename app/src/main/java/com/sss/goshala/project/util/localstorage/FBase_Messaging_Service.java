package com.sss.goshala.project.util.localstorage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sss.goshala.project.R;

import java.util.Date;
import java.util.Objects;

public class FBase_Messaging_Service extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String Title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String Message = remoteMessage.getNotification().getBody();

        sendNotification(Title, Message);

    }

    private void sendNotification(String Title, String Message) {


        Uri Sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.default_notification_channel_id),
                    "Extranettest", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(Title);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getResources().getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.sp)
                .setContentTitle(Title)
                .setContentText(Message)
                .setAutoCancel(true)
                .setSound(Sound)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setLights(Color.YELLOW, 1000, 300);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        assert notificationManager != null;
        notificationManager.notify(m, builder.build());
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i("tk",s);
    }
}