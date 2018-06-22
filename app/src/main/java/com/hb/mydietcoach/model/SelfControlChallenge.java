package com.hb.mydietcoach.model;

public class SelfControlChallenge extends Challenge {
    private int currentPosition;

    public SelfControlChallenge() {
    }

    public SelfControlChallenge(int imageId, String title, int currentPosition, int stars, int type) {
        super(imageId, title, stars, type);
        this.currentPosition = currentPosition;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
