package com.s2icode.quirc;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ImageUtils {
    private static final String TAG = ImageUtils.class.getSimpleName();


    public static Bitmap getBitmapFromAsset(String filePath) {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Resources res = appContext.getResources();
        AssetManager assetManager = res.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }



    public static byte[] bitmapToColorBGRByteArray(Bitmap bm) {
        // Create the buffer with the correct size
        Log.v(TAG, "bitmapToColorByteArray");
        int iBytes = bm.getWidth() * bm.getHeight();
        byte[] res = null;
        Bitmap.Config format = bm.getConfig();
        if (format == Bitmap.Config.ARGB_8888) {
            Log.v(TAG, "Bitmap.Config.ARGB_8888");
            res = new byte[iBytes * 3];
            for (int i = 0; i < bm.getHeight(); i++) {
                for (int j = 0; j < bm.getWidth(); j++) {
                    int colour = bm.getPixel(j, i);

                    // RGB -> BGR
                    res[i * bm.getWidth() * 3 + j * 3 + 2] = (byte) (colour >> 16 & 0xFF);
                    res[i * bm.getWidth() * 3 + j * 3 + 1] = (byte) (colour >> 8 & 0xFF);
                    res[i * bm.getWidth() * 3 + j * 3] = (byte) (colour & 0xFF);

                }
            }
        } else if (format == Bitmap.Config.RGB_565) {
            ByteBuffer buffer = ByteBuffer.allocate(iBytes * 2);
            // Log.e("DBG", buffer.remaining()+""); -- Returns a correct number based on dimensions
            // Copy to buffer and then into byte array
            bm.copyPixelsToBuffer(buffer);
            byte[] arr = buffer.array();
            for (int i = 0; i < iBytes; i++) {
                float A, R, G, B;
                R = ((arr[i * 2] & 0xF8));
                G = ((arr[i * 2] & 0x7) << 5) + ((arr[i * 2 + 1] & 0xE0) >> 5);
                B = ((arr[i * 2 + 1] & 0x1F) << 3);
                byte r = (byte) ((R + G + B) / 3);
                res[i] = r;
            }
        }
        // Log.e("DBG", buffer.remaining()+""); -- Returns 0
        return res;
    }

    public static byte[] bitmapToGrayByteArray(Bitmap bm) {
        // Create the buffer with the correct size
        int iBytes = bm.getWidth() * bm.getHeight();
        byte[] res = new byte[iBytes];
        Bitmap.Config format = bm.getConfig();
        if (format == Bitmap.Config.ARGB_8888) {
            ByteBuffer buffer = ByteBuffer.allocate(iBytes * 4);
            // Log.e("DBG", buffer.remaining()+""); -- Returns a correct number based on dimensions
            // Copy to buffer and then into byte array
            bm.copyPixelsToBuffer(buffer);
            byte[] arr = buffer.array();
            for (int i = 0; i < iBytes; i++) {
                int R, G, B;
                R = ((arr[i * 4]) & 0xff);
                G = (arr[i * 4 + 1] & 0xff);
                B = (arr[i * 4 + 2] & 0xff);
                //A=arr[i*4+3];
                byte r = (byte) ((R + G + B) / 3);
                res[i] = r;
            }
        } else if (format == Bitmap.Config.RGB_565) {
            ByteBuffer buffer = ByteBuffer.allocate(iBytes * 2);
            // Log.e("DBG", buffer.remaining()+""); -- Returns a correct number based on dimensions
            // Copy to buffer and then into byte array
            bm.copyPixelsToBuffer(buffer);
            byte[] arr = buffer.array();
            for (int i = 0; i < iBytes; i++) {
                float A, R, G, B;
                R = ((arr[i * 2] & 0xF8));
                G = ((arr[i * 2] & 0x7) << 5) + ((arr[i * 2 + 1] & 0xE0) >> 5);
                B = ((arr[i * 2 + 1] & 0x1F) << 3);
                byte r = (byte) ((R + G + B) / 3);
                res[i] = r;
            }
        }
        // Log.e("DBG", buffer.remaining()+""); -- Returns 0
        return res;
    }
}
