package com.hb.mydietcoach.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import com.hb.mydietcoach.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RestartChallengeDialog extends Dialog {

    public RestartChallengeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_restart_challenge);

        setCancelable(false);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.tvOK)
    void clickOK() {
        dismiss();
    }
}
