package com.hb.mydietcoach.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class BitmapUtils {
    private static final String TAG = BitmapUtils.class.getSimpleName();


    /**
     * Get bitmap from image file
     *
     * @param imgFile: input image file
     * @return: Image bitmap
     */
    public static Bitmap getBitmapFromFile(File imgFile) {
        //RGB_565 quality is smaller than default ARGB_8888
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;

        return BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
    }

    /**
     * Resize bitmap to new size
     *
     * @param bitmap:    source bitmap
     * @param maxWidth:  new width
     * @param maxHeight: new height
     * @return: new scale bitmap
     */
    public static Bitmap getResizedBitmap(Bitmap bitmap, float maxWidth, float maxHeight) {
        if (maxWidth <= 0 || maxHeight <= 0) return bitmap;

        float width = bitmap.getWidth();
        float height = bitmap.getHeight();

        if (width > maxWidth) {
            height = (maxWidth / width) * height;
            width = maxWidth;
        }
        if (height > maxHeight) {
            width = (maxHeight / height) * width;
            height = maxHeight;
        }

        return Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
    }

    public static File saveBitmap(Bitmap bitmap, String fileName) {
        File file = null;
        FileOutputStream out = null;
        try {
            File directory = Environment.getExternalStorageDirectory();
            if (!directory.exists()) directory.mkdirs();
            file = new File(directory, fileName + ".png");
            file.createNewFile();
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            return file;
        } catch (Exception e) {
            Log.e(TAG, "Save image file failed");
            Log.e(TAG, "File name " + Objects.requireNonNull(file).getAbsolutePath());
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
