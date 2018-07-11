package com.hb.mydietcoach.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.hb.mydietcoach.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TargetArchiveDialog extends Dialog {
    private int nTarget, nLevel, nNextTarget;

    public TargetArchiveDialog(@NonNull Context context, int nTarget, int nLevel, int nNextTarget) {
        super(context);
        this.nTarget = nTarget;
        this.nLevel = nLevel;
        this.nNextTarget = nNextTarget;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_target_archive);

        ButterKnife.bind(this);

        TextView tvTarget = findViewById(R.id.tvTargetPoint);
        TextView tvLevel = findViewById(R.id.tvReachedLevel);
        TextView tvNextTarget = findViewById(R.id.tvNextTarget);

        tvTarget.setText(String.valueOf(nTarget));

        String sLevel = getContext().getString(R.string._reached_level) + " " + nLevel
                + " " + getContext().getString(R.string._target_);
        tvLevel.setText(sLevel);

        String sNextTarget = getContext().getString(R.string.your_next_target) + " " + nNextTarget
                + " " + getContext().getString(R.string._point);
        tvNextTarget.setText(sNextTarget);
    }

    @OnClick(R.id.tvClose)
    void clickClose() {
        dismiss();
    }
}
