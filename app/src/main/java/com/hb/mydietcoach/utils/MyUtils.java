package com.hb.mydietcoach.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class MyUtils {

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * Get different day from last weight logging date
     *
     * @param firstDateMiniseconds:  first date
     * @param secondDateMiniseconds: second date
     * @return: positive amount of date
     */
    public static int getDifferentDay(long firstDateMiniseconds, long secondDateMiniseconds) {
        Calendar temp = new GregorianCalendar();
        temp.setTimeInMillis(firstDateMiniseconds);

        //First date
        Calendar calst = new GregorianCalendar(temp.get(Calendar.YEAR),
                temp.get(Calendar.MONTH),
                temp.get(Calendar.DAY_OF_MONTH));

        temp.setTimeInMillis(secondDateMiniseconds);
        //Second date
        Calendar calnd = new GregorianCalendar(temp.get(Calendar.YEAR),
                temp.get(Calendar.MONTH),
                temp.get(Calendar.DAY_OF_MONTH));

        long msDiff = calnd.getTimeInMillis() - calst.getTimeInMillis();
        long diff = Math.abs(TimeUnit.MILLISECONDS.toDays(msDiff));

        if (diff > 7) return 7;
        return (int) diff;
    }


    /**
     * Round a number by default number format
     *
     * @param roundNumber: number need to round
     * @return: round number
     */
    public static float roundFloat(int numberAfterDecimalPoint, float roundNumber) {
        if (numberAfterDecimalPoint <= 0) return roundNumber;

        float tmp = (float) Math.pow(10, numberAfterDecimalPoint);
        return Math.round(roundNumber * tmp) / tmp;
    }
}
