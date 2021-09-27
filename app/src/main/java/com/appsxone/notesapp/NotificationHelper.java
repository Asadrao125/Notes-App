package com.appsxone.notesapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.appsxone.notesapp.activities.HomeActivity;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    @SuppressLint("ResourceAsColor")
    public NotificationCompat.Builder getChannelNotification(String morningng) {
        Intent activityIntent = new Intent(getApplicationContext(), HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(morningng)
                .setSmallIcon(R.drawable.logo)
                .setDefaults(Notification.DEFAULT_ALL)
                .setColor(R.color.sky_blue)
                .setContentIntent(contentIntent);
    }
}