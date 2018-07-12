package com.hb.mydietcoach.model.diary;

import java.util.List;

public class ExerciseAssets {
    private String name;

    private int calories_130lbs;

    private int id;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setCalories_130lbs(int calories_130lbs) {
        this.calories_130lbs = calories_130lbs;
    }

    public int getCalories_130lbs() {
        return this.calories_130lbs;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return getName();
    }

    public class RootExercise {

        private List<ExerciseAssets> exercises;

        public void setExercises(List<ExerciseAssets> exercises) {
            this.exercises = exercises;
        }

        public List<ExerciseAssets> getExercises() {
            return this.exercises;
        }
    }
}