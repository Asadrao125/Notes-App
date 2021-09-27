package com.appsxone.notesapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReciever extends BroadcastReceiver {
    int h, m, r;
    String val;

    @Override
    public void onReceive(Context context, Intent intent) {

        val = intent.getStringExtra("val");
        h = intent.getIntExtra("h", 100000);
        m = intent.getIntExtra("m", 100000);
        r = intent.getIntExtra("r", 100000);

        completeNotif(context.getApplicationContext(), val);

    }

    public void completeNotif(Context context, String value) {
        NotificationHelper notificaitonHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificaitonHelper.getChannelNotification(value);
        notificaitonHelper.getManager().notify(1, nb.build());
    }
}