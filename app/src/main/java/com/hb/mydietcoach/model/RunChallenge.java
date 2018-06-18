package com.hb.mydietcoach.model;

public class RunChallenge extends Challenge {
    double length;
    double unit;

    public RunChallenge() {
    }

    public RunChallenge(int imageId, String title, int stars, double length, double unit, int type) {
        super(imageId, title, stars, type);
        this.length = length;
        this.unit = unit;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }
}
