package com.hb.mydietcoach.model;

import com.hb.mydietcoach.utils.Constants;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;


public abstract class Challenge implements Serializable {

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

    public String getTypeString() {
        if (getType() == Constants.CHALLENGE_TYPE_DRINK_WATER) return "CHALLENGE_TYPE_DRINK_WATER";
        if (getType() == Constants.CHALLENGE_TYPE_GYM) return "CHALLENGE_TYPE_GYM";
        if (getType() == Constants.CHALLENGE_TYPE_FILL_MY_PLATE)
            return "CHALLENGE_TYPE_FILL_MY_PLATE";
        if (getType() == Constants.CHALLENGE_TYPE_AVOID_JUNK_FOOD)
            return "CHALLENGE_TYPE_AVOID_JUNK_FOOD";
        if (getType() == Constants.CHALLENGE_TYPE_AVOID_SUGARY_DRINK)
            return "CHALLENGE_TYPE_AVOID_SUGARY_DRINK";
        if (getType() == Constants.CHALLENGE_TYPE_AVOID_SNACKING)
            return "CHALLENGE_TYPE_AVOID_SNACKING";
        if (getType() == Constants.CHALLENGE_TYPE_OF_MY) return "CHALLENGE_TYPE_OF_MY";
        if (getType() == Constants.CHALLENGE_TYPE_WALK_A_MILE) return "CHALLENGE_TYPE_WALK_A_MILE";
        return null;
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
        if (lastTime == 0) return false;
        Calendar lastDay = new GregorianCalendar();
        lastDay.setTimeInMillis(lastTime);
        Calendar today = Calendar.getInstance();
        return lastDay.get(Calendar.DAY_OF_YEAR) != today.get(Calendar.DAY_OF_YEAR)
                || lastDay.get(Calendar.MONTH) != today.get(Calendar.MONTH)
                || lastDay.get(Calendar.YEAR) != today.get(Calendar.YEAR);
    }

    public abstract Challenge setCurrentPosition(float currentPosition);
}
