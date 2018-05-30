package com.hb.mydietcoach.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.hb.mydietcoach.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingReminderDialog extends Dialog {
    Context context;
    ChangeReminderListener listener;

    RadioButton rbtNone, rbtReminder;
    EditText editText;

    //Spinner
    Spinner spinner;
    String strBeforeEvent, strAfterEvent;
    String arr[];

    boolean isReminder = false;
    int minutesFromEvent = -15;

    public SettingReminderDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        if (context instanceof ChangeReminderListener)
            listener = (ChangeReminderListener) context;
    }

    public void init(boolean isReminder, int minutesFromEvent) {
        this.isReminder = isReminder;
        this.minutesFromEvent = minutesFromEvent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_setting_reminder);

        ButterKnife.bind(this);

        editText = findViewById(R.id.edtMinutes);
        rbtNone = findViewById(R.id.rbtNone);
        rbtReminder = findViewById(R.id.rbtReminder);

        //Create spinner
        spinner = findViewById(R.id.spinner);
        strBeforeEvent = context.getString(R.string.before_event);
        strAfterEvent = context.getString(R.string.after_event);
        arr = new String[]{strBeforeEvent, strAfterEvent};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(context, android.R.layout.simple_list_item_activated_1, arr);
        spinner.setAdapter(adapter);

        if (isReminder) rbtReminder.setChecked(true);
        editText.setText(String.valueOf(Math.abs(minutesFromEvent)));
        if (minutesFromEvent >= 0) {
            spinner.setSelection(1);
        }
    }

    @OnClick(R.id.tvCancel)
    void clickCancel(View view) {
        dismiss();
    }

    @OnClick(R.id.tvDone)
    void clickDone(View view) {
        String strMinutes = editText.getText().toString();
        if (TextUtils.isEmpty(strMinutes)) {
            Toast.makeText(getContext(), context.getString(R.string.input_full_info), Toast.LENGTH_SHORT).show();
            return;
        }
        int minutes = Integer.parseInt(editText.getText().toString());
        if (minutes < 0) {
            Toast.makeText(getContext(), context.getString(R.string.input_wrong_type), Toast.LENGTH_SHORT).show();
            return;
        }
        if (arr[spinner.getSelectedItemPosition()].equalsIgnoreCase(strBeforeEvent))
            minutes = -minutes;
        boolean isReminder = rbtReminder.isChecked();
        if (listener != null) {
            listener.changeReminder(isReminder, minutes);
        }
        dismiss();
    }

    public interface ChangeReminderListener {
        void changeReminder(boolean isReminder, int minutesFromEvent);
    }
}
