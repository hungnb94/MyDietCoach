package com.hb.mydietcoach.notification;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.MainActivity;
import com.hb.mydietcoach.activity.diary.DiaryActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.activity.reminder.ReminderActivity;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.Constants;

import java.util.Calendar;

public class NotificationIntentService extends IntentService {
    private static final String TAG = NotificationIntentService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 1;

    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (isNowSleepingHour()) return;

        Notification.Builder builder = new Notification.Builder(this);
        //Set title
        builder.setContentTitle(getString(R.string.app_name));

        Bundle bundle = intent.getExtras();
        //Set content
        String strContent = bundle.getString(Constants.NOTIFICATION_CONTENT_TEXT);
        builder.setContentText(strContent);

        //Set large icon
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.drawer_icon_with_shadow);
        builder.setLargeIcon(bitmap);

        //Set notify intent base on type
        int nType = bundle.getInt(Constants.NOTIFICATION_TYPE, 0);
        Intent notifyIntent = setNotifyIntent(builder, nType);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //To be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);

        setSoundVibrate(builder);

        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }

    private Intent setNotifyIntent(Notification.Builder builder, int nType) {
        Intent notifyIntent;
        switch (nType) {
            case Constants.NOTIFI_TYPE_REMINDER:
                builder.setSmallIcon(R.drawable.ic_reminders);
                notifyIntent = new Intent(this, ReminderActivity.class);
                break;
            case Constants.NOTIFI_TYPE_MOTIVATIONAL_PHOTO:
                builder.setSmallIcon(R.drawable.ic_photo);
                notifyIntent = new Intent(this, PhotosActivity.class);
                break;
            case Constants.NOTIFI_TYPE_EXERCISE:
                builder.setSmallIcon(R.drawable.ic_diary);
                notifyIntent = new Intent(this, DiaryActivity.class);
                break;
            default:
                builder.setSmallIcon(R.drawable.ic_home_drawer);
                notifyIntent = new Intent(this, MainActivity.class);
                break;
        }
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        return notifyIntent;
    }

    private boolean isNowSleepingHour() {
        PreferenceManager pre = new PreferenceManager(getApplicationContext());
        int nStartHour = pre.getInt(PreferenceManager.SETTING_START_SLEEPING_HOUR, 0);
        int nEndHour = pre.getInt(PreferenceManager.SETTING_END_SLEEPING_HOUR, 0);
        int nStartMinute = pre.getInt(PreferenceManager.SETTING_START_SLEEPING_MINUTE, 0);
        int nEndMinute = pre.getInt(PreferenceManager.SETTING_END_SLEEPING_MINUTE, 0);

        return isSleepingHour(nStartHour, nStartMinute, nEndHour, nEndMinute);
    }

    public static boolean isSleepingHour(int nStartHour, int nStartMinute, int nEndHour, int nEndMinute) {
        int nStartTime = nStartHour * 60 + nStartMinute;
        int nEndTime = nEndHour * 60 + nEndMinute;

        Calendar calendar = Calendar.getInstance();
        int nTimeNow = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);

        if (nStartTime <= nEndTime) {
            return (nTimeNow >= nStartTime && nTimeNow <= nEndTime);
        } else {
            return (nTimeNow >= nStartTime || nTimeNow <= nEndTime);
        }
    }

    private void setSoundVibrate(Notification.Builder builder) {
        PreferenceManager pre = new PreferenceManager(getApplicationContext());
        boolean isPlaySound = pre.getBoolean(PreferenceManager.SETTING_IS_PLAY_SOUND, false);
        boolean isMakeVibrate = pre.getBoolean(PreferenceManager.SETTING_IS_MAKE_VIBRATE, false);
        if (isMakeVibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.VIBRATE)
                        == PackageManager.PERMISSION_GRANTED) {
                    builder.setVibrate(new long[]{1000, 1000});
                }
            }
        } else {
            builder.setVibrate(new long[]{1000, 1000});
        }

        if (isPlaySound) {
            String soundUri = pre.getString(PreferenceManager.SETTING_URI_NOTIFICATION_SOUND, null);
            if (soundUri != null) {
                try {
                    builder.setSound(Uri.parse(soundUri));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                builder.setSound(Uri.parse("android.resource://"
                        + getApplicationContext().getPackageName() + "/" + R.raw.nice_alarm));
            }
        }
    }
}