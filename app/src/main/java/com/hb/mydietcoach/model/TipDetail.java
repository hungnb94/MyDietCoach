package com.hb.mydietcoach.model;

import io.realm.RealmObject;

public class TipDetail extends RealmObject {
    public static final String MESSAGE = "message";
    public static final String PIORITY = "piority";

    private String message;
    private int piority;

    public TipDetail() {
    }

    public TipDetail(String message, int piority) {
        this.message = message;
        this.piority = piority;
    }

    public int getPiority() {
        return piority;
    }

    public void setPiority(int piority) {
        this.piority = piority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
