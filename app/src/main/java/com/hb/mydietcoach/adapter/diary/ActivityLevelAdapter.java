package com.hb.mydietcoach.adapter.diary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hb.mydietcoach.R;

public class ActivityLevelAdapter extends BaseAdapter {
    Context context;

    private String[] titles, details;

    public ActivityLevelAdapter(Context context, String[] titles, String[] details) {
        this.context = context;
        this.titles = titles;
        this.details = details;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        view = inflater.inflate(R.layout.item_activity_level, viewGroup, false);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvDetail = view.findViewById(R.id.tvDetail);

        tvTitle.setText(titles[i]);
        tvDetail.setText(details[i]);

        return view;
    }
}
