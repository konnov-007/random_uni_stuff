package konnov.commr.vk.labndk;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Lib.on_surface_changed(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Lib.on_draw_frame();
    }
}
