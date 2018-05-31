package com.hb.mydietcoach.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.RadioGroup;

import com.hb.mydietcoach.R;

public class RepeatTimeDialog extends Dialog {
    RadioGroup radioGroup;

    public RepeatTimeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_repeat_time);
        radioGroup = findViewById(R.id.radioGroup);
    }
}
