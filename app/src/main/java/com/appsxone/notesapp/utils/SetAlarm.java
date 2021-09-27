package com.appsxone.notesapp.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.appsxone.notesapp.AlarmReciever;

import java.util.Calendar;

public class SetAlarm {
    private void setAlarm(Activity activity, int hour, int mins, int requestCode, String morningOrEvening) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, mins);
        c.set(Calendar.SECOND, 0);

        AlarmManager am = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity, AlarmReciever.class);
        intent.putExtra("val", morningOrEvening);
        intent.putExtra("h", hour);
        intent.putExtra("m", mins);
        intent.putExtra("r", requestCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, requestCode, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        if (am != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            } else if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT <= 20) {
                am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            } else if (Build.VERSION.SDK_INT > 20) {
                am.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }
        }
    }
}
