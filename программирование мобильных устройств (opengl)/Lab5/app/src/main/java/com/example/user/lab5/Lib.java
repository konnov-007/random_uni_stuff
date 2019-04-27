package com.example.user.lab5;

public class Lib {
    static {
        System.loadLibrary("native-lib");
    }

    public static native void onSurfaceCreated();

    public static native void onSurfaceChanged(int width, int height);

    public static native void onDrawFrame();
}