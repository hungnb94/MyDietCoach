package com.hb.mydietcoach.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Challenge implements Serializable {

    private long id;
    private int imageId;
    private String title;
    private int stars;
    private int type;
    private long lastTime;

    public Challenge() {
    }

    public Challenge(long id, int imageId, String title, int stars, int type, long lastTime) {
        this.id = id;
        this.imageId = imageId;
        this.title = title;
        this.stars = stars;
        this.type = type;
        this.lastTime = lastTime;
    }

    public long getId() {
        return id;
    }

    public Challenge setId(long id) {
        this.id = id;
        return this;
    }

    public int getImageId() {
        return imageId;
    }

    public Challenge setImageId(int imageId) {
        this.imageId = imageId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Challenge setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getType() {
        return type;
    }

    public Challenge setType(int type) {
        this.type = type;
        return this;
    }

    public int getStars() {
        return stars;
    }

    public Challenge setStars(int stars) {
        this.stars = stars;
        return this;
    }

    public long getLastTime() {
        return lastTime;
    }

    public Challenge setLastTime(long lastTime) {
        this.lastTime = lastTime;
        return this;
    }

    public boolean isNewDay(){
        Calendar lastDay = new GregorianCalendar();
        lastDay.setTimeInMillis(lastTime);
        Calendar today = Calendar.getInstance();
        return (lastDay.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
                && lastDay.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && lastDay.get(Calendar.YEAR) == today.get(Calendar.YEAR));
    }
}
