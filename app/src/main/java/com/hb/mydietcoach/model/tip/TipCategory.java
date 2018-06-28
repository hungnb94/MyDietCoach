package com.hb.mydietcoach.model.tip;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class TipCategory extends RealmObject {
    public static final String ID = "id";
    public static final String TITLE = "title";

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
        this.advices.addAll(advices);
    }
}
