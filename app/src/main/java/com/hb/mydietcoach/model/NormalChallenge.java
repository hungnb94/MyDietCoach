package com.hb.mydietcoach.model;

public class NormalChallenge extends Challenge{
    private int totalCount;
    private int currentPosition;

    public NormalChallenge() {
    }

    public NormalChallenge(int imageId, String title, int stars, int totalCount, int type) {
        super(imageId, title, stars, type);
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
