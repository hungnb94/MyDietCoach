package com.hb.mydietcoach.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * Read text from file in assets folder
     *
     * @param fileName: file name
     * @return: content of file
     */
    public static String readFileFromAssets(Context context, String fileName) {
        StringBuilder allContent = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fileName)));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                allContent.append(mLine);
                allContent.append("\n");
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException");
                    e.printStackTrace();
                }
            }
        }
        return allContent.toString();
    }
}
