package com.hb.mydietcoach.model;

public class NormalChallenge extends Challenge{
    private int totalCount;
    private int currentPosition;
    private String unit;

    public NormalChallenge() {
    }

    public NormalChallenge(long id, int imageId, String title, int stars, int totalCount, int currentPosition,
                           String unit, int type, long lastTime) {
        super(id, imageId, title, stars, type, lastTime);
        setTotalCount(totalCount);
        setCurrentPosition(currentPosition);
        setUnit(unit);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public NormalChallenge setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public NormalChallenge setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public NormalChallenge setUnit(String unit) {
        this.unit = unit;
        return this;
    }
}
