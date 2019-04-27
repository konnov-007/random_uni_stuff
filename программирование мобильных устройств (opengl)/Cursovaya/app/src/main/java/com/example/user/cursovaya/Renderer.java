package com.example.user.cursovaya;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.io.*;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Renderer implements GLSurfaceView.Renderer {

    private Object[] objects = new Object[7];

    private Context context;

    private int[] depthMapFbo = new int[1];
    private int[] depthMap = new int[1];
    private int screenWidth, screenHeight;

    static private float[] modelMatrix = new float[16];
    static private float[] viewMatrix = new float[16];
    static private float[] modelViewMatrix = new float[16];
    static private float[] projectionMatrix = new float[16];
    static private float[] modelViewProjectionMatrix = new float[16];

    static private Shader firstShader, secondShader;

    private String firstVertexShader =
            "precision highp float;" +
            "uniform mat4 light_space_matrix;" +
            "attribute vec3 a_vertex;" +
            "void main() {" +
            "gl_Position = light_space_matrix * vec4(a_vertex, 1.0);" +
            "}";

    private String firstFragmentShader =
            "precision highp float;" +
            "void main() {" +
            "}";

    private String secondVertexShader =
            "uniform mat4 u_model_view_projection_matrix;" +
            "uniform mat4 light_space_matrix;" +
            "attribute vec2 a_tex_coordinates;" +
            "attribute vec3 a_vertex;" +
            "attribute vec3 a_normal;" +
            "varying vec2 v_tex_coordinates;" +
            "varying vec3 v_vertex;" +
            "varying vec3 v_normal;" +
            "varying vec4 v_light_vertex;" +
            "void main() {" +
            "v_vertex = a_vertex;" +
            "vec3 n_normal = normalize(a_normal);" +
            "v_normal = n_normal;"+
            "v_tex_coordinates = a_tex_coordinates;" +
            "v_light_vertex = light_space_matrix * vec4(a_vertex, 1.0);" +
            "gl_Position = u_model_view_projection_matrix * vec4(a_vertex, 1.0);" +
            "}";

    private String secondFragmentShader =
            "precision highp float;" +
            "uniform vec3 u_camera;" +
            "uniform vec3 u_light_position;" +
            "uniform sampler2D u_texture;" +
            "uniform sampler2D u_s_texture;" +
            "varying vec3 v_vertex;" +
            "varying vec3 v_normal;" +
            "varying vec4 v_light_vertex;" +
            "varying vec2 v_tex_coordinates;" +
            "void main() {" +
            "vec3 n_normal = normalize(v_normal);" +
            "vec3 light_vector = normalize(u_light_position - v_vertex);" +
            "vec3 look_vector = normalize(u_camera - v_vertex);" +
            "float ambient = 0.3;" +
            "float k_diffuse = 1.0;" +
            "float k_specular = 0.0;" +
            "float diffuse = k_diffuse * max(dot(n_normal, light_vector), 0.0);" +
            "vec3 reflect_vector = reflect(-light_vector, n_normal);" +
            "float specular = k_specular * pow(max(dot(look_vector, reflect_vector), 0.0), 40.0);" +
            "vec4 one = vec4(1.0, 1.0, 1.0, 1.0);" +
            "vec4 depth = v_light_vertex / v_light_vertex.w;" +
            "float depth_light = texture2D(u_s_texture, depth.st).z;" +
            "float shadow = depth.z < depth_light ? 1.0 : 0.0;" +
            "vec4 light = (ambient + shadow * (diffuse + specular)) * one;" +
            "gl_FragColor = texture2D(u_texture, v_tex_coordinates) * light;" +
            "}";

    Renderer(Context context) {
        this.context = context;
        try {
            String objectsPath = "/objects/";
            objects[0] = new Object(objectsPath + "table.obj", R.raw.table);
            objects[1] = new Object(objectsPath + "banana.obj", R.raw.banana);
            objects[2] = new Object(objectsPath + "tomato.obj", R.raw.tomato);
            objects[3] = new Object(objectsPath + "carrot.obj", R.raw.carrot);
            objects[4] = new Object(objectsPath + "orange.obj", R.raw.orange);
            objects[5] = new Object(objectsPath + "candle.obj", R.raw.candle);
            objects[6] = new Object(objectsPath + "bottle.obj", R.raw.bottle);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void parsingObjFile(String file_name, ArrayList<float[]> vertexes,
                               ArrayList<float[]> textures, ArrayList<float[]> normales,
                               ArrayList<int[]> faces) throws Exception {
        FileReader file = new FileReader(file_name);
        BufferedReader buffer = new BufferedReader(file);
        String line;
        while ((line = buffer.readLine()) != null) {
            if (line.contains("v ")) {
                String[] tokens = line.split(" ");
                float[] vertexes_array = new float[] {
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]),
                        Float.parseFloat(tokens[3])
                };
                vertexes.add(vertexes_array);
            }
            else if (line.contains("vt ")) {
                String[] tokens = line.split(" ");
                float[] textures_array = new float[] {
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2])
                };
                textures.add(textures_array);
            }
            else if (line.contains("vn ")) {
                String[] tokens = line.split(" ");
                float[] normales_array = new float[] {
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]),
                        Float.parseFloat(tokens[3])
                };
                normales.add(normales_array);
            }
            else if (line.contains("f ")) {
                String[] tokens = line.split(" ");
                int[] faces_array = new int[9];
                int index = 0;
                for (int i = 1; i < tokens.length; i++) {
                    String[] coordinates = tokens[i].split("/");
                    faces_array[index++] = Integer.parseInt(coordinates[0]);
                    faces_array[index++] = Integer.parseInt(coordinates[1]);
                    faces_array[index++] = Integer.parseInt(coordinates[2]);
                }
                faces.add(faces_array);
            }
        }
    }

    private void createDepthMapTexture() {
        GLES20.glGenTextures(1, depthMap, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthMap[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0,
                GLES20.GL_DEPTH_COMPONENT, 2048, 2048, 0,
                GLES20.GL_DEPTH_COMPONENT, GLES20.GL_UNSIGNED_INT, null);
        GLES20.glGenFramebuffers(1, depthMapFbo, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, depthMapFbo[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
                GLES20.GL_TEXTURE_2D, depthMap[0], 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    private void initializeViewMatrix() {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.setLookAtM(viewMatrix, 0, 0, 4, 4, 0f, 0, 0, 0, 1, 0);
        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
    }

    private void initializeProjectionMatrix(int width, int height) {
        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 20);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
    }

    private void initializeMvpMatrix(int mode) {
        if (mode == 1) {
            Matrix.setIdentityM(Renderer.modelMatrix, 0);
            Matrix.setLookAtM(Renderer.viewMatrix, 0, 1.3f, 4f, 0.5f,
                    0, 0, 0, 0, 1, 0);
            float ratio = (float) screenWidth / screenHeight;
            Matrix.frustumM(Renderer.projectionMatrix, 0, -1.1f * ratio, 1.1f * ratio,
                    1.1f * -1, 1.1f * 1, 1, 20);
            Matrix.multiplyMM(Renderer.modelViewMatrix, 0, Renderer.viewMatrix,
                    0, Renderer.modelMatrix, 0);
            Matrix.multiplyMM(Renderer.modelViewProjectionMatrix, 0,
                    Renderer.projectionMatrix, 0, Renderer.modelViewMatrix, 0);
        }
        if (mode == 2) {
            Matrix.setIdentityM(Renderer.modelMatrix, 0);
            Matrix.setLookAtM(Renderer.viewMatrix, 0, 0, 4, 4,
                    0, 0, 0, 0, 1, 0);
            float ratio = (float) screenWidth / screenHeight;
            Matrix.frustumM(Renderer.projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 20);
            Matrix.multiplyMM(Renderer.modelViewMatrix, 0, Renderer.viewMatrix,
                    0, Renderer.modelMatrix, 0);
            Matrix.multiplyMM(Renderer.modelViewProjectionMatrix, 0,
                    Renderer.projectionMatrix, 0, Renderer.modelViewMatrix, 0);
        }
    }

    private void renderScene(int mode) {
        if (mode == 1) {
            initializeMvpMatrix(1);
            for (int i = 0; i < 7; i++) {
                firstShader.use_program();
                firstShader.linkLightModelViewProjectionMatrix(Renderer.modelViewProjectionMatrix);
                firstShader.linkVertexBuffer(objects[i].vertex_buffer);
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, objects[i].faces.size() * 3);
            }
        }
        if (mode == 2) {
            for (int i = 0; i < 7; i++) {
                secondShader.linkVertexBuffer(objects[i].vertex_buffer);
                secondShader.linkNormalBuffer(objects[i].normales_buffer);
                secondShader.linkDepthMap(depthMap);
                secondShader.linkTexture(objects[i].texture);
                secondShader.linkTextureBuffer(objects[i].texture_buffer);
                initializeMvpMatrix(1);
                secondShader.linkLightModelViewProjectionMatrix(Renderer.modelViewProjectionMatrix);
                initializeMvpMatrix(2);
                secondShader.linkModelViewProjectionMatrix(Renderer.modelViewProjectionMatrix);
                secondShader.linkCamera(0, 4, 4);
                secondShader.linkLightSource(1.3f, 4f, 0.5f);
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, objects[i].texture[0]);
                GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthMap[0]);
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, objects[i].faces.size() * 3);
            }
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        createDepthMapTexture();
        initializeViewMatrix();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        firstShader = new Shader(firstVertexShader, firstFragmentShader);
        secondShader = new Shader(secondVertexShader, secondFragmentShader);
        for (int i = 0; i < 7; i++) {
            objects[i].loadTexture(context);
        }
        objects[1].scale(0.2f, 0.2f, 0.2f);
        objects[1].translate(-1, 2, 1);
        objects[2].scale(0.1f, 0.1f, 0.1f);
        objects[2].translate(0f, 2.5f, 1.8f);
        objects[3].scale(0.1f, 0.1f, 0.1f);
        objects[3].translate(1f, 2.5f, 1.8f);
        objects[4].scale(0.2f, 0.2f, 0.2f);
        objects[4].translate(-0.5f, 2.5f, 0.8f);
        objects[5].scale(1f, 0.7f, 1f);
        objects[5].translate(1.3f, 3f, 0.5f);
        objects[6].scale(0.5f, 0.2f, 0.2f);
        objects[6].translate(0.5f, 2.1f, 0.1f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        screenWidth = width;
        screenHeight = height;
        gl.glViewport(0, 0, width, height);
        initializeProjectionMatrix(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        try {
            GLES20.glCullFace(GLES20.GL_FRONT);
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, depthMapFbo[0]);
            GLES20.glViewport(0 ,0, 2048, 2048);
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            renderScene(1);
            GLES20.glCullFace(GLES20.GL_BACK);
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            GLES20.glViewport(0 ,0, screenWidth, screenHeight);
            renderScene(2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
