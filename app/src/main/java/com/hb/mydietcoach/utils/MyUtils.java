package com.hb.mydietcoach.utils;

import android.content.Context;
import android.widget.Toast;

public class MyUtils {

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
