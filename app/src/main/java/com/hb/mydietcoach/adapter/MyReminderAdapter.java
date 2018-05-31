package com.hb.mydietcoach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.model.Reminder;
import com.hb.mydietcoach.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MyReminderAdapter extends BaseAdapter {
    Context context;
    List<Reminder> reminders;

    LayoutInflater inflater;
    SimpleDateFormat formatHour, formatDate;

    public MyReminderAdapter(Context context, List<Reminder> reminders) {
        this.context = context;
        this.reminders = reminders;
        inflater = LayoutInflater.from(context);
        formatDate = new SimpleDateFormat(Constants.DATE_FORMAT);
        formatHour = new SimpleDateFormat(Constants.FORMAT_HOUR);
    }

    @Override
    public int getCount() {
        return reminders.size();
    }

    @Override
    public Object getItem(int i) {
        return reminders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return reminders.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_my_reminder, viewGroup, false);

        TextView tvContent = view.findViewById(R.id.tvContent);
        TextView tvTime = view.findViewById(R.id.tvTime);

        Reminder reminder = reminders.get(i);
        tvContent.setText(reminder.getContent());
        Calendar calendar = reminder.getStartDate();
        String strTime = context.getString(R.string.at)
                + " " + formatHour.format(calendar.getTime())
                + ", " + formatDate.format(calendar.getTime());
        tvTime.setText(strTime);
        return view;
    }
}
