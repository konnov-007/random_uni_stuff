package com.example.user.lab_3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRender implements GLSurfaceView.Renderer {
    Sphere sun = new Sphere(4f);

    double angle = 0;

    Context c;

    public GLRender(Context context) {
        c = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        sun.create_shader();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        sun.initialize_projection(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        sun.linking_attribites();
        sun.draw_sphere();
    }
}
