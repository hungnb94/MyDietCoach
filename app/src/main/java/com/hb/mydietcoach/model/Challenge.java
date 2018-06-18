package com.hb.mydietcoach.model;

import java.io.Serializable;


public class Challenge implements Serializable {

    private int imageId;
    private String title;
    private int stars;
    private int type;

    public Challenge() {
    }

    public Challenge(int imageId, String title, int stars, int type) {
        this.imageId = imageId;
        this.title = title;
        this.stars = stars;
        this.type = type;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
