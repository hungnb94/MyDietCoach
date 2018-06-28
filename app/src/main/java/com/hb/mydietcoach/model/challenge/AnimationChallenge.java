package com.hb.mydietcoach.model.challenge;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.utils.Constants;


public class AnimationChallenge extends NormalChallenge {

    public AnimationChallenge(long id, int imageId, String title, int stars, int totalCount, int currentPosition,
                              String unit, int type, long lastTime) {
        super(id, imageId, title, stars, totalCount, currentPosition, unit, type, lastTime);
    }

    public AnimationChallenge() {
    }

    public Animation getAnimation(Context context) {
        if (getType() == Constants.CHALLENGE_TYPE_FILL_MY_PLATE){
            return AnimationUtils.loadAnimation(context, R.anim.translate_top_back);
        }
        return null;
    }
}
