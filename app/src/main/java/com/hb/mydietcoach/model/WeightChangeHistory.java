package com.hb.mydietcoach.model;

import io.realm.RealmObject;

public class WeightChangeHistory extends RealmObject {

    private long time;
    private float curentWeight;
    private float weightChange;

    public WeightChangeHistory() {
    }

    public WeightChangeHistory(long time, float curentWeight, float weightChange) {
        this.time = time;
        this.curentWeight = curentWeight;
        this.weightChange = weightChange;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getWeightChange() {
        return weightChange;
    }

    public void setWeightChange(float weightChange) {
        this.weightChange = weightChange;
    }

    public float getCurentWeight() {
        return curentWeight;
    }

    public void setCurentWeight(float curentWeight) {
        this.curentWeight = curentWeight;
    }
}
