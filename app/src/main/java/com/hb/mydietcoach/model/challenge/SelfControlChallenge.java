package com.hb.mydietcoach.model.challenge;

public class SelfControlChallenge extends Challenge {
    private int currentPosition;

    public SelfControlChallenge() {
    }

    public SelfControlChallenge(long id, int imageId, String title, int currentPosition, int stars, int type, long lastTime) {
        super(id, imageId, title, stars, type, lastTime);
        this.currentPosition = currentPosition;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public SelfControlChallenge setCurrentPosition(float integerPosition) {
        this.currentPosition = (int) integerPosition;
        return this;
    }
}
