package com.hb.mydietcoach.model;

public class NormalChallenge extends Challenge {
    private int totalCount;
    private int currentPosition;

    public NormalChallenge() {
    }

    public NormalChallenge(int imageId, String title, int stars, int totalCount, int currentPosition, int type) {
        super(imageId, title, stars, type);
        this.totalCount = totalCount;
        this.currentPosition = currentPosition;
    }
}
