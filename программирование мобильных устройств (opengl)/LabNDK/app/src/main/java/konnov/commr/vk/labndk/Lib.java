package konnov.commr.vk.labndk;


public class Lib {
    static {
        System.loadLibrary("native-lib");
    }
    public static native void on_draw_frame();
    public static native void on_surface_changed(int width, int height);
}
