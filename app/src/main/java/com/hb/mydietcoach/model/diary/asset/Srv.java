package com.hb.mydietcoach.model.diary.asset;

import io.realm.RealmObject;

public class Srv extends RealmObject {
    private String cm;  //Calcium %

    private String va;  //Vitamin A %

    private String srvd;

    private String vc;  //Vitamin C %

    private String cr;  //Cabohydrates g

    private String msu; //Unit for msa (MS Unit)

    private String fat; //Fats g

    private String fi;  //Dietary Fiber g

    private String so;  //Sodium mg

    private String pf;  //Polyunsaturated Fat g

    private String ch;  //Cholesterol mg

    private String ir;  //Iron %

    private String tf;  //Trans Fat g

    private String sid; //Unknown this number meaning

    private String pr;  //Protein g

    private String mf;  //Monounsaturated Fat g

    private String po;  //Potasium g

    private String nou; //Unknown but always is 1

    private String cal; //Calories

    private String md;  //Unit

    private String sf;  //Saturated Fat g

    private String msa; //Weight (MS Amount)

    private String su;  //Sugars g

    public void setCm(String cm) {
        this.cm = cm;
    }

    public String getCm() {
        return this.cm;
    }

    public void setVa(String va) {
        this.va = va;
    }

    public String getVa() {
        return this.va;
    }

    public void setSrvd(String srvd) {
        this.srvd = srvd;
    }

    public String getSrvd() {
        return this.srvd;
    }

    public void setVc(String vc) {
        this.vc = vc;
    }

    public String getVc() {
        return this.vc;
    }

    public void setCr(String cr) {
        this.cr = cr;
    }

    public String getCr() {
        return this.cr;
    }

    public void setMsu(String msu) {
        this.msu = msu;
    }

    public String getMsu() {
        return this.msu;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getFat() {
        return this.fat;
    }

    public void setFi(String fi) {
        this.fi = fi;
    }

    public String getFi() {
        return this.fi;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public String getSo() {
        return this.so;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public String getPf() {
        return this.pf;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getCh() {
        return this.ch;
    }

    public void setIr(String ir) {
        this.ir = ir;
    }

    public String getIr() {
        return this.ir;
    }

    public void setTf(String tf) {
        this.tf = tf;
    }

    public String getTf() {
        return this.tf;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSid() {
        return this.sid;
    }

    public void setPr(String pr) {
        this.pr = pr;
    }

    public String getPr() {
        return this.pr;
    }

    public void setMf(String mf) {
        this.mf = mf;
    }

    public String getMf() {
        return this.mf;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getPo() {
        return this.po;
    }

    public void setNou(String nou) {
        this.nou = nou;
    }

    public String getNou() {
        return this.nou;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    public String getCal() {
        return this.cal;
    }

    public void setMd(String md) {
        this.md = md;
    }

    public String getMd() {
        return this.md;
    }

    public void setSf(String sf) {
        this.sf = sf;
    }

    public String getSf() {
        return this.sf;
    }

    public void setMsa(String msa) {
        this.msa = msa;
    }

    public String getMsa() {
        return this.msa;
    }

    public void setSu(String su) {
        this.su = su;
    }

    public String getSu() {
        return this.su;
    }
}