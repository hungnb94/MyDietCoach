package com.hb.mydietcoach.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.utils.Constants;

public class MyAlertDialog extends Dialog {
    String content;
    TextView textView;


    public MyAlertDialog(@NonNull Context context, String content) {
        super(context);
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_alert);

        textView = findViewById(R.id.textView);
        textView.setText(content);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, Constants.ALERT_DIALOG_LENGTH);
    }
}
