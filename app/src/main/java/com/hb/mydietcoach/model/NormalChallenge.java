package com.hb.mydietcoach.model;

public class NormalChallenge extends Challenge{
    private int totalCount;
    private int currentPosition;
    private String unit;

    public NormalChallenge() {
    }

    public NormalChallenge(int imageId, String title, int stars, int totalCount, int currentPosition,
                           String unit, int type) {
        super(imageId, title, stars, type);
        setTotalCount(totalCount);
        setCurrentPosition(currentPosition);
        setUnit(unit);
    }

    public NormalChallenge(Challenge challenge) {
        super(challenge.getImageId(), challenge.getTitle(), challenge.getStars(), challenge.getType());
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
