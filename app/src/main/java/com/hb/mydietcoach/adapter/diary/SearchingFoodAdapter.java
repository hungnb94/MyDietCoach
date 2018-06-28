package com.hb.mydietcoach.adapter.diary;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.model.diary.FoodAssets;

import java.util.List;

public class SearchingFoodAdapter extends ArrayAdapter<FoodAssets> {
    Context context;
    List<FoodAssets> list;
    LayoutInflater layoutInflater;

    public SearchingFoodAdapter(Context context, List<FoodAssets> list) {
        super(context, R.layout.item_food, list);
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Nullable
    @Override
    public FoodAssets getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.item_food, viewGroup, false);
        TextView textView  = view.findViewById(R.id.textView);

        FoodAssets food = this.list.get(position);
        textView.setText(food.getFn());

        return view;
    }
}
