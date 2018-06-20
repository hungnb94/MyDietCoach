package com.hb.mydietcoach.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hb.mydietcoach.utils.Constants;

public class NotificationManager {
    Context context;

    public NotificationManager(Context context) {
        this.context = context;
    }

    public void setAlarm(long nofificationId, boolean isMotivationalNotification,
                         String strContent, long triggerTime) {
        Intent notifyIntent = new Intent(context, NotificationReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.NOTIFICATION_CONTENT_TEXT, strContent);
        bundle.putBoolean(Constants.IS_MOTIVATIONAL_NOTIFICATION, isMotivationalNotification);
        notifyIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, (int) nofificationId, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }

    public void setRepeatAlarm(long notificationId, boolean isMotivationalNotification,
                               String strContent, long triggerTime, long repeatMiliseconds) {
        Intent notifyIntent = new Intent(context, NotificationReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.NOTIFICATION_CONTENT_TEXT, strContent);
        bundle.putBoolean(Constants.IS_MOTIVATIONAL_NOTIFICATION, isMotivationalNotification);
        notifyIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, (int) notificationId, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, repeatMiliseconds, pendingIntent);
    }

    public void cancelAlarm(long notificationId) {
        Intent notifyIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, (int) notificationId, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }

}
