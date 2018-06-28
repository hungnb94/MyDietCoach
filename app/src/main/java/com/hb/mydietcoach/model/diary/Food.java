package com.hb.mydietcoach.model.diary;

import java.io.Serializable;

public class Food implements IItemDiary, Serializable{
    private long id;
    private String name, calories, weight, time;

    public Food() {
    }

    public Food(long id, String name, String calories, String weight, String time) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.weight = weight;
        this.time = time;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
