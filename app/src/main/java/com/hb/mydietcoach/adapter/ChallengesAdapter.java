package com.hb.mydietcoach.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hb.mydietcoach.R;

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.MyViewHolder> {

    private final String TAG = ChallengesAdapter.class.getSimpleName();

    private final int DRINK_WATER_LENGTH = 150;

    //    private Context context;
    private int numGlasses;
    private int currentPosition = 0;

    private LayoutInflater inflater;

    public ChallengesAdapter(Context context, int numGlasses) {
//        this.context = context;
        this.numGlasses = numGlasses;

        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_glass_water, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if (position == currentPosition) {
            holder.ivGlass.setImageResource(R.drawable.challenge_water_full);
            holder.ivArrow.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == currentPosition) {
                    currentPosition++;
                    changeBackgroundLikeDrinkWater(holder.ivGlass, holder.ivArrow, 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return numGlasses;
    }

    public void setItemsSize(int size) {
        this.numGlasses = size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGlass, ivArrow;

        public MyViewHolder(final View itemView) {
            super(itemView);
            ivGlass = itemView.findViewById(R.id.ivGlassWater);
            ivArrow = itemView.findViewById(R.id.ivArrow);
        }
    }

    int[] imageArray = {R.drawable.challenge_water_b1, R.drawable.challenge_water_b2,
            R.drawable.challenge_water_b3, R.drawable.challenge_water_b4,
            R.drawable.challenge_water_b5, R.drawable.challenge_water_b6,
            R.drawable.challenge_water_b7, R.drawable.challenge_water_b8,
            R.drawable.challenge_water_b9, R.drawable.challenges_water_empty,
            R.drawable.challenges_water_dark};

    private void changeBackgroundLikeDrinkWater(final ImageView ivGlass, final ImageView ivArrow, final int pos) {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                ivGlass.setImageResource(imageArray[pos]);
                if (pos < imageArray.length - 1) {
                    changeBackgroundLikeDrinkWater(ivGlass, ivArrow, pos + 1);
                } else {
                    ivArrow.setVisibility(View.INVISIBLE);
                    notifyDataSetChanged();
                }
            }
        };
        handler.postDelayed(runnable, DRINK_WATER_LENGTH);
    }

}
