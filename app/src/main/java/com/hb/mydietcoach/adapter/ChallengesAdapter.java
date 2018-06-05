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
import com.hb.mydietcoach.utils.Constants;

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.MyViewHolder> {

    private final String TAG = ChallengesAdapter.class.getSimpleName();

    private final int DRINK_WATER_LENGTH = 150;

    private ItemEventListener listener;
    //    private Context context;
    private int numTotalItems;
    private int numCurrentPosition = 0;

    private LayoutInflater inflater;
    private int itemType = Constants.CHALLENGE_TYPE_DRINK_WATER;

    public ChallengesAdapter(Context context, int numGlasses) {
//        this.context = context;
        this.numTotalItems = numGlasses;

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
        if (position == numCurrentPosition) {
            holder.ivGlass.setImageResource(R.drawable.challenge_water_full);
            holder.ivArrow.setVisibility(View.VISIBLE);
        } else if (position < numCurrentPosition) {
            holder.ivGlass.setImageResource(R.drawable.challenges_water_empty);
            holder.ivSign.setVisibility(View.VISIBLE);
            holder.ivArrow.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == numCurrentPosition) {
                    if (listener != null) listener.startClick();
                    numCurrentPosition++;
                    changeBackgroundLikeDrinkWater(holder.ivGlass, 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return numTotalItems;
    }

    public void backToPreviousItem() {
        numCurrentPosition--;
        notifyDataSetChanged();
    }

    public int getCurentPosition() {
        return numCurrentPosition;
    }

    public void setNumCurrentPosition(int numGlassesLeft) {
        numCurrentPosition = numGlassesLeft;
    }

    public int getType() {
        return this.itemType;
    }

    public void updateTotalItems(int newValue) {
        this.numTotalItems = newValue;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGlass, ivArrow, ivSign;

        public MyViewHolder(final View itemView) {
            super(itemView);
            ivGlass = itemView.findViewById(R.id.ivGlassWater);
            ivArrow = itemView.findViewById(R.id.ivArrow);
            ivSign = itemView.findViewById(R.id.ivSign);
        }
    }

    int[] imageArray = {R.drawable.challenge_water_b1, R.drawable.challenge_water_b2,
            R.drawable.challenge_water_b3, R.drawable.challenge_water_b4,
            R.drawable.challenge_water_b5, R.drawable.challenge_water_b6,
            R.drawable.challenge_water_b7, R.drawable.challenge_water_b8,
            R.drawable.challenge_water_b9, R.drawable.challenges_water_empty};

    private void changeBackgroundLikeDrinkWater(final ImageView ivGlass, final int pos) {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                ivGlass.setImageResource(imageArray[pos]);
                if (pos < imageArray.length - 1) {
                    changeBackgroundLikeDrinkWater(ivGlass, pos + 1);
                } else {
                    if (listener != null) {
                        listener.itemViewDone();
                        if (isAllItemDone()) listener.allItemDone();
                    }
                    notifyDataSetChanged();
                }
            }
        };
        handler.postDelayed(runnable, DRINK_WATER_LENGTH);
    }

    public void setOnItemEventListener(ItemEventListener itemEventListener) {
        this.listener = itemEventListener;
    }

    public boolean isAllItemDone() {
        return numTotalItems == numCurrentPosition;
    }

    public interface ItemEventListener {
        void startClick();

        void itemViewDone();

        void allItemDone();
    }
}
