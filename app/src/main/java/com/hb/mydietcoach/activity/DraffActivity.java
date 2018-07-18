package com.hb.mydietcoach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.utils.Constants;

public class DraffActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.some_draff);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.NOTIFICATION_CONTENT_TEXT, "Content abc");
        bundle.putInt(Constants.NOTIFICATION_TYPE, Constants.NOTIFI_TYPE_MOTIVATIONAL_PHOTO);

        Intent intent = new Intent("com.hb.mydietcoach.action");
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }
}
