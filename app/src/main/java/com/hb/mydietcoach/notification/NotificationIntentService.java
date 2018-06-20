package com.hb.mydietcoach.notification;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.MainActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.utils.Constants;

public class NotificationIntentService extends IntentService {
    private static final int NOTIFICATION_ID = 1;

    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String strContent = bundle.getString(Constants.NOTIFICATION_CONTENT_TEXT);
        boolean isMotivationalNotification = bundle.getBoolean(Constants.IS_MOTIVATIONAL_NOTIFICATION, false);
        //Set repeat
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(strContent);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.drawer_icon_with_shadow);
        builder.setLargeIcon(bitmap);
        Intent notifyIntent;
        if (isMotivationalNotification) {
            builder.setSmallIcon(R.drawable.photos_no_images);
            notifyIntent = new Intent(this, PhotosActivity.class);
        } else {
            builder.setSmallIcon(R.drawable.add_exercise_icon);
            notifyIntent = new Intent(this, MainActivity.class);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }

}
