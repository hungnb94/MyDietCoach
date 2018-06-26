package com.hb.mydietcoach.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class TipCategory implements RealmModel {

    private int id;
    private String title;
    private RealmList<TipDetail> advices;

    public TipCategory(int id, String title, List<TipDetail> advices) {
        setId(id);
        setTitle(title);
        setAdvices(advices);
    }

    public TipCategory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RealmList<TipDetail> getAdvices() {
        return advices;
    }

    public void setAdvices(List<TipDetail> advices) {
        this.advices = new RealmList<>();
        advices.addAll(advices);
    }
}
