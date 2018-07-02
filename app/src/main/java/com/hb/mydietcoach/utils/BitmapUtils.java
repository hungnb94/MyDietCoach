package com.hb.mydietcoach.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

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
}
