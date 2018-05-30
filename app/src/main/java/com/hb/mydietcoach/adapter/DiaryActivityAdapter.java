package com.hb.mydietcoach.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.model.Exercise;
import com.hb.mydietcoach.model.Food;
import com.hb.mydietcoach.model.IItemDiary;
import com.hb.mydietcoach.utils.Constants;

import java.util.List;

public class DiaryActivityAdapter extends BaseAdapter {
    private final String TAG = DiaryActivityAdapter.class.getSimpleName();

    Context context;
    List<IItemDiary> list;
    LayoutInflater layoutInflater;
    Animation animRotation;

    public DiaryActivityAdapter(Context context, List<IItemDiary> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        animRotation = AnimationUtils.loadAnimation(context, R.anim.rotate_180);
        animRotation.setDuration(Constants.ANIMATION_LENGTH);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getViewTypeCount() {
        // menu type count
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        // current menu type
        return position;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_diary_activity, null);
            holder = new ViewHolder();
            holder.tvTime = view.findViewById(R.id.tvTime);
            holder.ivItemType = view.findViewById(R.id.ivItemType);
            holder.tvItemName = view.findViewById(R.id.tvItemName);
            holder.tvCaloriesAmount = view.findViewById(R.id.tvCaloriesAmount);
            holder.tvWeight = view.findViewById(R.id.tvWeight);
            holder.ivMore = view.findViewById(R.id.ivMore);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Object object = this.list.get(position);
        if (object instanceof Food) {
            Food food = (Food) object;
            int pos = food.getTime().indexOf(" ");
            if (pos + 1 < food.getTime().length()) {
                holder.tvTime.setText(food.getTime().substring(pos + 1));
            }
            holder.ivItemType.setImageResource(R.drawable.quick_meal_hint_apple);
            holder.tvItemName.setText(food.getName());
            holder.tvCaloriesAmount.setText(food.getCalories());
            holder.tvWeight.setText(food.getWeight());
            holder.tvWeight.setVisibility(View.INVISIBLE);

            final Runnable animRunnable = new Runnable() {
                @Override
                public void run() {
                    if (holder.tvWeight.getVisibility() == View.VISIBLE) {
                        holder.tvWeight.setVisibility(View.INVISIBLE);
                        holder.ivMore.setRotation(0);
                    } else {
                        holder.tvWeight.setVisibility(View.VISIBLE);
                        holder.ivMore.setRotation(180);
                    }
                }
            };
            holder.ivMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.ivMore.startAnimation(animRotation);
                    new Handler().postDelayed(animRunnable, Constants.ANIMATION_LENGTH);
                }
            });

        } else if (object instanceof Exercise) {
            Exercise exercise = (Exercise) object;
            int pos = exercise.getTime().indexOf(" ");
            if (pos + 1 < exercise.getTime().length()) {
                holder.tvTime.setText(exercise.getTime().substring(pos + 1));
            }
            holder.tvTime.setText(exercise.getTime());
            holder.ivItemType.setImageResource(R.drawable.ic_small_aerobics);
            holder.tvItemName.setText(exercise.getName());
            holder.tvCaloriesAmount.setText(exercise.getCalories());
            holder.tvWeight.setVisibility(View.INVISIBLE);
            holder.ivMore.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    static class ViewHolder {
        TextView tvTime;
        ImageView ivItemType;
        TextView tvItemName, tvCaloriesAmount, tvWeight;
        ImageView ivMore;
    }

}
