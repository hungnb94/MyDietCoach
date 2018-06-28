package com.hb.mydietcoach.model;

import io.realm.RealmObject;

public class WeightChangeHistory extends RealmObject {

    private long time;
    private float weightChange;

    public WeightChangeHistory() {
    }

    public WeightChangeHistory(long time, float weightChange) {
        this.time = time;
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

}
