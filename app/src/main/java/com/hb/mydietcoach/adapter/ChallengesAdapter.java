package com.hb.mydietcoach.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.model.AnimationChallenge;
import com.hb.mydietcoach.model.Challenge;
import com.hb.mydietcoach.model.NormalChallenge;
import com.hb.mydietcoach.utils.Constants;

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.MyViewHolder> {

    private final String TAG = ChallengesAdapter.class.getSimpleName();

    private final int DRINK_WATER_LENGTH = 250;

    private ItemEventListener listener;
    //    private Context context;
    private int numTotalItems;
    private int numCurrentPosition = 0;

    private LayoutInflater inflater;
    private Challenge challenge;

    private int[] arrDrinkWaters = {R.drawable.challenges_water_dark,
            R.drawable.challenge_water_b1, R.drawable.challenge_water_b2,
            R.drawable.challenge_water_b3, R.drawable.challenge_water_b4,
            R.drawable.challenge_water_b5, R.drawable.challenge_water_b6,
            R.drawable.challenge_water_b7, R.drawable.challenge_water_b8,
            R.drawable.challenge_water_b9, R.drawable.challenges_water_empty};

    private int[] arrGym = {R.drawable.challenges_gym_shape_sh,
            R.drawable.challenges_gym1, R.drawable.challenges_gym2,
            R.drawable.challenges_gym1, R.drawable.challenges_gym2,
            R.drawable.challenges_gym1, R.drawable.challenges_gym2,
            R.drawable.challenges_gym1, R.drawable.challenges_gym2,
            R.drawable.challenges_gym1, R.drawable.challenges_gym2};

    private int[] arrPushUp = {R.drawable.challenges_pushups_shape_sh,
            R.drawable.challenges_pushups01_sh, R.drawable.challenges_pushups02_sh,
            R.drawable.challenges_pushups01_sh, R.drawable.challenges_pushups02_sh,
            R.drawable.challenges_pushups01_sh, R.drawable.challenges_pushups02_sh,
            R.drawable.challenges_pushups01_sh, R.drawable.challenges_pushups02_sh,
            R.drawable.challenges_pushups01_sh, R.drawable.challenges_pushups02_sh};

    private int[] arrMyChallenge = {R.drawable.challenges_general_dark,
            R.drawable.challenges_general_animation_helper_2,
            R.drawable.challenges_general_before,
            R.drawable.challenges_general_after};

    private int[] arrFillPlate = {R.drawable.challenges_portion_sh,
            R.drawable.challenges_portion1,
            R.drawable.challenges_fill_plate_helper,
            R.drawable.challenges_portion2};

    public ChallengesAdapter(Context context, Challenge challenge, int numGlasses) {
//        this.context = context;
        this.challenge = challenge;
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
        int[] arrImage = getImageArray();

        if (position == numCurrentPosition) {
            if (arrImage != null) holder.ivGlass.setImageResource(arrImage[1]);
            holder.ivSign.setVisibility(View.GONE);
            holder.ivArrow.setVisibility(View.VISIBLE);
        } else if (position < numCurrentPosition) {
            if (arrImage != null) holder.ivGlass.setImageResource(arrImage[arrImage.length - 1]);
            holder.ivSign.setVisibility(View.VISIBLE);
            holder.ivArrow.setVisibility(View.GONE);
        } else {
            if (arrImage != null) holder.ivGlass.setImageResource(arrImage[0]);
            holder.ivSign.setVisibility(View.GONE);
            holder.ivArrow.setVisibility(View.GONE);
        }

        if (challenge instanceof AnimationChallenge) {
            if (arrImage != null) holder.ivAbove.setImageResource(arrImage[2]);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == numCurrentPosition) {
                    if (listener != null) listener.startClick();
                    numCurrentPosition++;
                    int[] arrImage = getImageArray();
                    if (challenge instanceof AnimationChallenge) {
                        startAnimationFromChallenge(holder.ivGlass, holder.ivAbove, arrImage, challenge);
                    } else if (challenge instanceof NormalChallenge) {
                        changeBackgroundLikeAnimation(holder.ivGlass, arrImage, 1);
                    }
                }
            }
        });
    }

    private int[] getImageArray() {
        int itemType = challenge.getType();
        if (itemType == Constants.CHALLENGE_TYPE_DRINK_WATER) return arrDrinkWaters;
        if (itemType == Constants.CHALLENGE_TYPE_GYM) return arrGym;
        if (itemType == Constants.CHALLENGE_TYPE_PUSH_UP) return arrPushUp;
        if (itemType == Constants.CHALLENGE_TYPE_OF_MY) return arrMyChallenge;
        if (itemType == Constants.CHALLENGE_TYPE_FILL_MY_PLATE) return arrFillPlate;
        return null;
    }

    private void startAnimationFromChallenge(final ImageView ivBelow, final ImageView ivAbove,
                                             final int[] resId, Challenge challenge) {
        if (listener != null) listener.startClick();
        Animation animation = ((AnimationChallenge) challenge).getAnimation(ivAbove.getContext());
        if (animation == null) {
            notifyDataSetChanged();
            if (listener != null) {
                listener.itemViewDone();
                if (numCurrentPosition == numTotalItems) listener.allItemDone();
            }
            return;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                notifyDataSetChanged();
                if (listener != null) {
                    listener.itemViewDone();
                    if (numCurrentPosition == numTotalItems) listener.allItemDone();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ivAbove.startAnimation(animation);
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
        return this.challenge.getType();
    }

    public void updateTotalItems(int newValue) {
        this.numTotalItems = newValue;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGlass, ivAbove, ivArrow, ivSign;

        public MyViewHolder(final View itemView) {
            super(itemView);
            ivGlass = itemView.findViewById(R.id.ivGlassWater);
            ivAbove = itemView.findViewById(R.id.ivAbove);
            ivArrow = itemView.findViewById(R.id.ivArrow);
            ivSign = itemView.findViewById(R.id.ivSign);
        }
    }

    /**
     * Change image continuous like animation
     *
     * @param ivGlass: target image view
     * @param pos:     next image position in arr
     */
    private void changeBackgroundLikeAnimation(final ImageView ivGlass, final int[] arrImage, final int pos) {
        if (arrImage == null) return;
        if (pos == arrImage.length) return;

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                ivGlass.setImageResource(arrImage[pos]);
                if (pos < arrImage.length - 1) {
                    changeBackgroundLikeAnimation(ivGlass, arrImage, pos + 1);
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
