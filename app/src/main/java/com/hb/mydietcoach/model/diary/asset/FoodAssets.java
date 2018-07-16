package com.hb.mydietcoach.model.diary.asset;

import io.realm.RealmObject;

public class FoodAssets extends RealmObject {

    public static final String FN = "fn";

    private Srvs srvs;

    private String fn;

    private String ft;

    private String fid;

    private String bn;

    public void setSrvs(Srvs srvs) {
        this.srvs = srvs;
    }

    public Srvs getSrvs() {
        return this.srvs;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getFn() {
        return this.fn;
    }

    public void setFt(String ft) {
        this.ft = ft;
    }

    public String getFt() {
        return this.ft;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFid() {
        return this.fid;
    }

    public void setBn(String bn) {
        this.bn = bn;
    }

    public String getBn() {
        return this.bn;
    }

    @Override
    public String toString() {
        return getFn();
    }

}
