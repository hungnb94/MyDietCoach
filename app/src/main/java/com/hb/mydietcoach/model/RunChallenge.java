package com.hb.mydietcoach.model;

public class RunChallenge extends Challenge {
    private double totalLength;
    private double currentPosition;
    private String unit;
    private double lengthUnit;

    public RunChallenge() {
    }

    public RunChallenge(int imageId, String title, int stars, double length, double currentPosition,
                        String unit, double lengthUnit, int type) {
        super(imageId, title, stars, type);
        this.totalLength = length;
        this.currentPosition = currentPosition;
        this.unit = unit;
        this.lengthUnit = lengthUnit;
    }

    public double getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(double length) {
        this.totalLength = length;
    }

    public double getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(double currentPosition) {
        this.currentPosition = currentPosition;
    }

    public double getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(double lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
