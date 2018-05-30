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
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(strContent);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.drawer_icon_with_shadow);
        builder.setLargeIcon(bitmap);
        builder.setSmallIcon(R.drawable.add_exercise_icon);
        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }

}
