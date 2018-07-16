package com.hb.mydietcoach.model.diary.asset;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Srvs extends RealmObject {
    private RealmList<Srv> srv;

    public void setSrv(List<Srv> srv) {
        this.srv = new RealmList<>();
        this.srv.addAll(srv);
    }

    public List<Srv> getSrv() {
        return this.srv;
    }


}
