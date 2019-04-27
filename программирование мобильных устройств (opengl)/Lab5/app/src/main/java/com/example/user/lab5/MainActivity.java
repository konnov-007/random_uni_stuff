package com.example.user.lab5;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        int size = 8;
        glSurfaceView.setEGLConfigChooser(size, size, size, size, size*2, 0);
        glSurfaceView.setRenderer(new MyRenderer(this));
        setContentView(glSurfaceView);
    }

}
