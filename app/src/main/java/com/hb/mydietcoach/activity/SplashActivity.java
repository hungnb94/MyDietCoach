package com.hb.mydietcoach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        PreferenceManager pre = new PreferenceManager(this);
        final boolean isFirstTimeRun = pre.getBoolean(PreferenceManager.IS_FIRST_TIME_LAUNCH, true);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (isFirstTimeRun){
                    intent = new Intent(SplashActivity.this, TermConditionActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable, Constants.SPLASH_LENGTH);
    }

}
