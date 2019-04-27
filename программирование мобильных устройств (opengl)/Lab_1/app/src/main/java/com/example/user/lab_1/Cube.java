package com.example.user.lab_1;

import javax.microedition.khronos.opengles.GL10;

public class Cube {
    private Square square;

    public Cube() {
        square = new Square();
    }

    public void draw(GL10 gl) {
        //лицевая грань
        gl.glPushMatrix();
        gl.glColor4f(0, 1, 0, 1);
        square.draw(gl);
        gl.glPopMatrix();
        //дно
        gl.glPushMatrix();
        gl.glColor4f(0, 1, 1, 1);
        gl.glTranslatef(0,-1f,-1f);
        gl.glRotatef(90, 1, 0, 0);
        square.draw(gl);
        gl.glPopMatrix();
        //крыша
        gl.glPushMatrix();
        gl.glTranslatef(0,1f,-1f);
        gl.glRotatef(90,1,0,0);
        gl.glColor4f(1,0,0,1);
        square.draw(gl);
        gl.glPopMatrix();
        //задняя грань
        gl.glPushMatrix();
        gl.glTranslatef(0,0,-2f);
        gl.glColor4f(1,0,1,1);
        square.draw(gl);
        gl.glPopMatrix();
        //боковая левая грань
        gl.glPushMatrix();
        gl.glTranslatef(-1f,0,-1f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glColor4f(1,1,0,1);
        square.draw(gl);
        gl.glPopMatrix();
        //боковая правая грань
        gl.glPushMatrix();
        gl.glTranslatef(1f,0,-1f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glColor4f(1,1,1,1);
        square.draw(gl);
        gl.glPopMatrix();
    }

    public void draw_cube(GL10 gl) {
        gl.glRotatef(GLRender.rotate_angle,1,1,1);
        GLRender.rotate_angle = GLRender.rotate_angle + 2f;
        draw(gl);
    }
}
