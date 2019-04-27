package com.example.user.lab_2;

import javax.microedition.khronos.opengles.GL10;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Sphere {
        private FloatBuffer mVertexBuffer;
        private FloatBuffer textureBuffer;
        int n = 0;

        public Sphere(float R) {
            int dtheta = 15, dphi = 5;

            int theta, phi;
            float DTOR = (float) (Math.PI / 180.0f);

            ByteBuffer byteBuf = ByteBuffer.allocateDirect(5000 * 3 * 4);
            byteBuf.order(ByteOrder.nativeOrder());
            mVertexBuffer = byteBuf.asFloatBuffer();
            byteBuf = ByteBuffer.allocateDirect(5000 * 2 * 4);
            byteBuf.order(ByteOrder.nativeOrder());
            textureBuffer = byteBuf.asFloatBuffer();
            for (theta =- 90; theta <= 90 - dtheta; theta += dtheta) {
                for (phi = 0; phi <= 360 - dphi; phi += dphi) {

                    mVertexBuffer.put((float)(Math.cos(theta*DTOR) * Math.cos(phi*DTOR))*R);
                    mVertexBuffer.put((float)(Math.cos(theta*DTOR) * Math.sin(phi*DTOR))*R);
                    mVertexBuffer.put((float)(Math.sin(theta*DTOR))*R);

                    mVertexBuffer.put((float)(Math.cos((theta+dtheta)*DTOR) * Math.cos(phi*DTOR))*R);
                    mVertexBuffer.put((float)(Math.cos((theta+dtheta)*DTOR) * Math.sin(phi*DTOR))*R);
                    mVertexBuffer.put((float)(Math.sin((theta+dtheta)*DTOR))*R);

                    mVertexBuffer.put((float)(Math.cos((theta+dtheta)*DTOR) * Math.cos((phi+dphi)*DTOR))*R);
                    mVertexBuffer.put((float)(Math.cos((theta+dtheta)*DTOR) * Math.sin((phi+dphi)*DTOR))*R);
                    mVertexBuffer.put((float)(Math.sin((theta+dtheta)*DTOR))*R);
                    n += 3;

                    mVertexBuffer.put((float)(Math.cos(theta*DTOR) * Math.cos((phi+dphi)*DTOR))*R);
                    mVertexBuffer.put((float)(Math.cos(theta*DTOR) * Math.sin((phi+dphi)*DTOR))*R);
                    mVertexBuffer.put((float)(Math.sin(theta*DTOR))*R);
                    n++;
                    textureBuffer.put(phi / 360.0f);
                    textureBuffer.put((90 + theta) / 180.0f);

                    textureBuffer.put(phi / 360.0f);
                    textureBuffer.put((90 + theta + dtheta) / 180.0f);

                    textureBuffer.put((phi + dphi) / 360.0f);
                    textureBuffer.put((90 + theta + dtheta) / 180.0f);

                    textureBuffer.put((phi + dphi) / 360.0f);
                    textureBuffer.put((90 + theta) / 180.0f);

                }
            }
            mVertexBuffer.position(0);
            textureBuffer.position(0);
        }

    public void draw_sphere(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
        for (int i = 0; i < n; i += 4) {
            gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, i,4);
        }
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }
}
