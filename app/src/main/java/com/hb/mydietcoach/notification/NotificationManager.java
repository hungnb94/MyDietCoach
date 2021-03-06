package com.hb.mydietcoach.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hb.mydietcoach.utils.Constants;

public class NotificationManager {
    private Context context;

    public NotificationManager(Context context) {
        this.context = context;
    }

    public void setAlarm(long nofificationId,
                         String strContent, int notificationType, long triggerTime) {
        Intent notifyIntent = new Intent(context, NotificationReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.NOTIFICATION_CONTENT_TEXT, strContent);
        bundle.putInt(Constants.NOTIFICATION_TYPE, notificationType);
        notifyIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, (int) nofificationId, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
    }

    public void setRepeatAlarm(long notificationId,
                               String strContent, int notificationType, long triggerTime, long repeatMiliseconds) {
        Intent notifyIntent = new Intent(context, NotificationReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.NOTIFICATION_CONTENT_TEXT, strContent);
        bundle.putInt(Constants.NOTIFICATION_TYPE, notificationType);
        notifyIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, (int) notificationId, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, repeatMiliseconds, pendingIntent);
        }
    }


    public void cancelAlarm(long notificationId) {
        Intent notifyIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, (int) notificationId, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) alarmManager.cancel(pendingIntent);
    }

//    /**
//     * Set alarm for motivational photo reminder
//     *
//     * @param strContent:        Content of notification
//     * @param triggerTime:       Trigger time
//     * @param repeatMiliseconds: Repeat time in mili second
//     */
//    public void setRepeatAlarmMotivational(String strContent, long triggerTime, long repeatMiliseconds) {
//        Intent notifyIntent = new Intent(context, NotificationReceiver.class);
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.NOTIFICATION_CONTENT_TEXT, strContent);
//        bundle.putInt(Constants.NOTIFICATION_TYPE, Constants.NOTIFI_TYPE_MOTIVATIONAL_PHOTO);
//        notifyIntent.putExtras(bundle);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast
//                (context, Constants.RC_MOTIVATIONAL_PHOTO, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//        if (alarmManager != null) {
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, repeatMiliseconds, pendingIntent);
//        }
//    }

}
