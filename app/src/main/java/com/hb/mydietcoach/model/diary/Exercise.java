package com.hb.mydietcoach.model.diary;

import java.io.Serializable;

public class Exercise implements IItemDiary, Serializable {
    private long id;
    private String name, calories, time;
    private boolean isReminder;
    private int minutesFromEvent;

    public Exercise() {
    }

    public Exercise(long id, String name, String calories, String time, boolean isReminder, int minutesFromEvent) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.time = time;
        this.isReminder = isReminder;
        this.minutesFromEvent = minutesFromEvent;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isReminder() {
        return isReminder;
    }

    public void setReminder(boolean reminder) {
        isReminder = reminder;
    }

    public int getMinutesFromEvent() {
        return minutesFromEvent;
    }

    public void setMinutesFromEvent(int minutesFromEvent) {
        this.minutesFromEvent = minutesFromEvent;
    }
}
