package com.example.user.lab_1;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Square{
    float[] points = new float[] {
            -1,1,0,
            -1,-1,0,
            1,-1,0,
            1,1,0
    };
    ByteBuffer byte_buffer;
    FloatBuffer float_buffer;

    public Square() {
        byte_buffer = ByteBuffer.allocateDirect(4*3*4);
        byte_buffer.order(ByteOrder.nativeOrder());
        float_buffer = byte_buffer.asFloatBuffer();
        float_buffer.put(points);
        float_buffer.position(0);
    }

    public void draw(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3,GL10.GL_FLOAT,0, float_buffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN,0,4);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    public void draw_square(GL10 gl) {
        gl.glRotatef(GLRender.rotate_angle,1,1,1);
        gl.glColor4f(1, 1, 0, 1);
        gl.glScalef(2f, 2f, 2f);
        GLRender.rotate_angle = GLRender.rotate_angle + 2f;
        draw(gl);
    }
}
