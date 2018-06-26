package com.hb.mydietcoach.model;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class TipDetail implements RealmModel {
    public static final String MESSAGE = "message";

    private String message;

    public TipDetail() {
    }

    public TipDetail(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
