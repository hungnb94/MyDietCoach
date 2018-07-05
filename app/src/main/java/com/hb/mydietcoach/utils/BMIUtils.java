package com.hb.mydietcoach.utils;

public class BMIUtils {
    public static final float UNDER_WEIGHT = 18.5f;
    public static final float NORMAL_WEIGHT_LOWER = 25f;
    public static final float NORMAL_WEIGHT_UPPER = 30f;
    public static final float OVER_WEIGHT = 30f;


    /**
     * Get BMI index
     *
     * @param weight: weight in kg
     * @param height: height in m
     * @return: BMI index
     */
    public static double getBMI(double weight, double height) {
        return weight / (height * height);
    }

}
