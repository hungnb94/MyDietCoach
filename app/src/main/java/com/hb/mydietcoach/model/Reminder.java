package com.hb.mydietcoach.model;

import java.io.Serializable;
import java.util.Calendar;

public class Reminder implements Serializable{

    private long id;
    private String content;
    Calendar startDate;
    long repeatMilisecond;

    public Reminder() {
    }

    public Reminder(long id, String content, Calendar startDate, int repeatMilisecond) {
        this.id = id;
        this.content = content;
        this.startDate = startDate;
        this.repeatMilisecond = repeatMilisecond;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public long getRepeatMilisecond() {
        return repeatMilisecond;
    }

    public void setRepeatMilisecond(long repeatMilisecond) {
        this.repeatMilisecond = repeatMilisecond;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
