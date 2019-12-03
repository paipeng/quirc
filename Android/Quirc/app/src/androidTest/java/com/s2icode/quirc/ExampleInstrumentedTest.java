package com.s2icode.quirc;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = ExampleInstrumentedTest.class.getSimpleName();

    private QuircUtils quircUtils;



    @Before
    public void grantPhonePermission() {
        quircUtils = new QuircUtils();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    @Test
    public void jniTest() {
        // Context of the app under test.
        String h = quircUtils.stringFromJNI();
        Log.i(TAG, "quirc JNI Test: " + h);
        Assert.assertEquals(true, "Hello from C++".equals(h));
    }

    @Test
    public void testQRCodeDecode() {
        Bitmap bitmap = ImageUtils.getBitmapFromAsset("qrcode.jpg");
        byte[] pixels;
        Assert.assertNotNull(bitmap);
        if (bitmap != null) {
            Log.i(TAG, "bitmap valid");
            long start = System.currentTimeMillis();
            pixels = ImageUtils.bitmapToGrayByteArray(bitmap);
            String text = quircUtils.decode( pixels, bitmap.getWidth(), bitmap.getHeight());
            Log.i(TAG, "quirc decode: " + text);

            Assert.assertEquals(true, "Test only".equals(text));
        }
    }

}
