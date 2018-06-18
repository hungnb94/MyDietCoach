package com.hb.mydietcoach.model;

import android.view.animation.Animation;


public class AnimationChallenge extends NormalChallenge{
    private Animation animation;

    public AnimationChallenge(int imageId, String title, int stars, int totalCount, Animation animation, int type) {
        super(imageId, title, stars, totalCount, type);
        this.animation = animation;
    }

    public AnimationChallenge() {
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }
}
