package com.hb.mydietcoach.adapter.diary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.model.diary.ExerciseAssets;

import java.util.List;

public class SearchingExerciseAdapter extends ArrayAdapter<ExerciseAssets> {
    private List<ExerciseAssets> list;
    private LayoutInflater inflater;

    public SearchingExerciseAdapter(@NonNull Context context, @NonNull List<ExerciseAssets> list) {
        super(context, R.layout.item_textview, list);
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public ExerciseAssets getItem(int position) {
        return list.get(position);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
        if (view == null) view = inflater.inflate(R.layout.item_textview, viewGroup, false);

        TextView textView = view.findViewById(R.id.itemTextView);
        textView.setText(list.get(i).getName());

        return view;
    }
}
