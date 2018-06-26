package com.hb.mydietcoach.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class TipDetail implements RealmModel {

    @SerializedName("message")
    @Expose
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
