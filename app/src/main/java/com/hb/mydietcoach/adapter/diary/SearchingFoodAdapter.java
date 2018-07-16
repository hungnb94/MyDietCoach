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
import com.hb.mydietcoach.model.diary.asset.FoodAssets;

import java.util.List;

public class SearchingFoodAdapter extends ArrayAdapter<FoodAssets> {
    private List<FoodAssets> list;
    private LayoutInflater layoutInflater;

    public SearchingFoodAdapter(Context context, List<FoodAssets> list) {
        super(context, R.layout.item_food, list);
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public FoodAssets getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.item_food, viewGroup, false);
        TextView textView = view.findViewById(R.id.textView);

        FoodAssets food = this.list.get(position);
        textView.setText(food.getFn());

        view.setTag(getItem(position));

        return view;
    }
}
