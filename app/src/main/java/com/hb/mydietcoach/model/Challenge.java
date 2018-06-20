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
}
