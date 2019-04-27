package com.example.user.cursovaya;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Object {

    private ArrayList<float[]> vertices = new ArrayList<>();
    private ArrayList<float[]> textures = new ArrayList<>();
    private ArrayList<float[]> normales = new ArrayList<>();
    public ArrayList<int[]> faces = new ArrayList<>();

    public FloatBuffer vertex_buffer, texture_buffer, normales_buffer;

    private int texture_id;

    public int[] texture = new int[1];

    public Object(String file_name, int texture) throws Exception {
        texture_id = texture;
        Renderer.parsingObjFile(Environment.getExternalStorageDirectory().getPath() +
                file_name, vertices, textures, normales, faces);
        getVertexCoordinates();
        getTextureCoordinates();
        getNormalesCoordinates();
    }

    private void getVertexCoordinates() {
        ByteBuffer byte_buffer = ByteBuffer.allocateDirect(faces.size() * 9 * 4);
        byte_buffer.order(ByteOrder.nativeOrder());
        vertex_buffer = byte_buffer.asFloatBuffer();
        for (int i = 0; i < faces.size(); i++) {
            int vA = faces.get(i)[0] - 1;
            int vB = faces.get(i)[3] - 1;
            int vC = faces.get(i)[6] - 1;
            vertex_buffer.put(vertices.get(vA)[0]);
            vertex_buffer.put(vertices.get(vA)[1]);
            vertex_buffer.put(vertices.get(vA)[2]);
            vertex_buffer.put(vertices.get(vB)[0]);
            vertex_buffer.put(vertices.get(vB)[1]);
            vertex_buffer.put(vertices.get(vB)[2]);
            vertex_buffer.put(vertices.get(vC)[0]);
            vertex_buffer.put(vertices.get(vC)[1]);
            vertex_buffer.put(vertices.get(vC)[2]);
        }
        vertex_buffer.position(0);
    }

    private void getTextureCoordinates() {
        ByteBuffer byte_buffer = ByteBuffer.allocateDirect(faces.size() * 6 * 4);
        byte_buffer.order(ByteOrder.nativeOrder());
        texture_buffer = byte_buffer.asFloatBuffer();
        for (int i = 0; i < faces.size(); i++) {
            int vtA = faces.get(i)[1] - 1;
            int vtB = faces.get(i)[4] - 1;
            int vtC = faces.get(i)[7] - 1;
            texture_buffer.put(textures.get(vtA)[0]);
            texture_buffer.put(textures.get(vtA)[1]);
            texture_buffer.put(textures.get(vtB)[0]);
            texture_buffer.put(textures.get(vtB)[1]);
            texture_buffer.put(textures.get(vtC)[0]);
            texture_buffer.put(textures.get(vtC)[1]);
        }
        texture_buffer.position(0);
    }

    private void getNormalesCoordinates() {
        ByteBuffer byte_buffer = ByteBuffer.allocateDirect(faces.size() * 9 * 4);
        byte_buffer.order(ByteOrder.nativeOrder());
        normales_buffer = byte_buffer.asFloatBuffer();
        for (int i = 0; i < faces.size(); i++) {
            int vnA = faces.get(i)[2] - 1;
            int vnB = faces.get(i)[5] - 1;
            int vnC = faces.get(i)[8] - 1;
            normales_buffer.put(normales.get(vnA)[0]);
            normales_buffer.put(normales.get(vnA)[1]);
            normales_buffer.put(normales.get(vnA)[2]);
            normales_buffer.put(normales.get(vnB)[0]);
            normales_buffer.put(normales.get(vnB)[1]);
            normales_buffer.put(normales.get(vnB)[2]);
            normales_buffer.put(normales.get(vnC)[0]);
            normales_buffer.put(normales.get(vnC)[1]);
            normales_buffer.put(normales.get(vnC)[2]);
        }
        normales_buffer.position(0);
    }

    public void loadTexture(Context context) {
        GLES20.glGenTextures(1, texture, 0);
        InputStream stream;
        Bitmap bitmap;
        stream = context.getResources().openRawResource(texture_id);
        bitmap = BitmapFactory.decodeStream(stream);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }

    public void scale(float x, float y, float z) {
        for (int i = 0; i < vertices.size(); i++) {
            vertices.get(i)[0] *= x;
            vertices.get(i)[1] *= y;
            vertices.get(i)[2] *= z;
        }
        getVertexCoordinates();
    }

    public void translate(float x, float y, float z) {
        for (int i = 0; i < vertices.size(); i++) {
            vertices.get(i)[0] += x;
            vertices.get(i)[1] += y;
            vertices.get(i)[2] += z;
        }
        getVertexCoordinates();
    }
}
