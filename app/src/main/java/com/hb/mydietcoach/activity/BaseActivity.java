package com.hb.mydietcoach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.hb.mydietcoach.R;

public class BaseActivity extends AppCompatActivity {
    private final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleUncaughtException();
    }

    private void handleUncaughtException() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.e(TAG, "Uncaught Exception");
                Log.e(TAG, "Thread: " + paramThread.getName());
                Log.e(TAG, "Throwable: " + paramThrowable.getMessage());
                paramThrowable.printStackTrace();

                Toast.makeText(BaseActivity.this, R.string.uncaught_exception, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BaseActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }
}
