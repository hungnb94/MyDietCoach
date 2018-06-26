package com.hb.mydietcoach.adapter.tip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.model.TipCategory;

import io.realm.RealmResults;

public class SpinnerTipCategoryAdapter extends BaseAdapter {
    //    Context context;
    RealmResults<TipCategory> list;
    LayoutInflater inflater;

    public SpinnerTipCategoryAdapter(Context context, RealmResults<TipCategory> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public TipCategory getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getId();
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_textview, viewGroup, false);
        TextView textView = view.findViewById(R.id.itemTextView);
        textView.setText(getItem(i).getTitle());
        return view;
    }

}
