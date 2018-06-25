package com.hb.mydietcoach.model;

public class RunChallenge extends Challenge {
    private double totalLength;
    private double currentPosition;
    private String unit;
    private double lengthUnit;

    public RunChallenge() {
    }

    public RunChallenge(long id, int imageId, String title, int stars, double length, double currentPosition,
                        String unit, double lengthUnit, int type, long lastTime) {
        super(id, imageId, title, stars, type, lastTime);
        this.totalLength = length;
        this.currentPosition = currentPosition;
        this.unit = unit;
        this.lengthUnit = lengthUnit;
    }

    public double getTotalLength() {
        return totalLength;
    }

    public RunChallenge setTotalLength(double length) {
        this.totalLength = length;
        return this;
    }

    public double getCurrentPosition() {
        return currentPosition;
    }

    public RunChallenge setCurrentPosition(float currentPosition) {
        this.currentPosition = currentPosition;
        return this;
    }

    public double getLengthUnit() {
        return lengthUnit;
    }

    public RunChallenge setLengthUnit(double lengthUnit) {
        this.lengthUnit = lengthUnit;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public RunChallenge setUnit(String unit) {
        this.unit = unit;
        return this;
    }
}
