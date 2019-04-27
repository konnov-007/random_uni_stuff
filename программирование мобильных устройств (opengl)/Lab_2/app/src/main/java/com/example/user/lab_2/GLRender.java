package com.example.user.lab_2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRender implements GLSurfaceView.Renderer {
    Sphere sun = new Sphere(4f);
    Sphere earth = new Sphere(1.5f);
    Sphere moon = new Sphere(0.5f);

    double angle = 0;
    static public int[] textures = new int [3];

    Context c;

    public GLRender(Context context) {
        c = context;
    }

    void load_texture(GL10 gl) {
        gl.glGenTextures(3, textures, 0);
        InputStream stream;
        Bitmap bitmap;
        stream = c.getResources().openRawResource(R.raw.sun);
        bitmap = BitmapFactory.decodeStream(stream);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        stream = c.getResources().openRawResource(R.raw.earth);
        bitmap = BitmapFactory.decodeStream(stream);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        stream = c.getResources().openRawResource(R.raw.moon);
        bitmap = BitmapFactory.decodeStream(stream);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[2]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        load_texture(gl);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClearColor(0,0,0,0);
        gl.glClearDepthf(1);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(-10,10, -10, 10, -10, 10);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        angle = (angle == 360) ? 0f : angle + 2f;
        gl.glLoadIdentity();
        gl.glPushMatrix();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        sun.draw_sphere(gl);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glRotatef((float) angle, 0, 1, 0);
        gl.glTranslatef(0, 0, 7);
        gl.glPushMatrix();
        gl.glRotatef((float) angle, 0, 1, 0);
        gl.glTranslatef(0, 0, 2.5f);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[2]);
        moon.draw_sphere(gl);
        gl.glPopMatrix();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
        earth.draw_sphere(gl);
        gl.glPopMatrix();
    }
}
