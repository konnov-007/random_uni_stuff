package com.example.user.lab_2;

import android.os.Bundle;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity {
    GLRender render = new GLRender(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glsurfaceview = new GLSurfaceView(this);
        glsurfaceview.setRenderer(render);
        glsurfaceview.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(glsurfaceview);
    }
}