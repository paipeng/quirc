package com.s2icode.quirc;

public class QuircUtils {
    private static final String TAG = QuircUtils.class.getSimpleName();
    static {
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        String libpath = System.getProperty("java.library.path");
        libpath = libpath + ":-Djava.library.path=/Users/paipeng/Development/xcode_project/s2i_opencv/Java/S2iOpenCV/src/main/libs";
        System.setProperty("java.library.path",libpath);

        System.out.println("java.library.path: " + System.getProperty("java.library.path"));

        System.loadLibrary("quirc");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();



    public native String decode(byte[] data, int width, int height);

}
