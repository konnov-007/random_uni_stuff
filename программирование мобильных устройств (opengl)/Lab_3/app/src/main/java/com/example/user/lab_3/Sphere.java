package com.example.user.lab_3;

import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Sphere {
        private FloatBuffer mVertexBuffer;
        private FloatBuffer textureBuffer;

        private float camera_x, camera_y, camera_z;

        private float light_x, light_y, light_z;

        private float[] model_matrix;
        private float[] view_matrix;
        private float[] model_view_matrix;
        private float[] projection_matrix;
        private float[] model_view_projection_matrix;
        private Shader mShader;

        int n = 0;

        public Sphere(float R) {
            initialize_view();

            int dtheta = 8, dphi = 8;

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

        public void initialize_view() {
            light_x = 3f;
            light_y = 5f;
            light_z = 2f;
            model_matrix = new float [16];
            view_matrix = new float [16];
            model_view_matrix = new float [16];
            projection_matrix = new float [16];
            model_view_projection_matrix = new float [16];
            Matrix.setIdentityM(model_matrix, 0);
            camera_x = 1f;
            camera_y = 3.5f;
            camera_z = 3f;
            Matrix.setLookAtM(view_matrix, 0, camera_x, camera_y, camera_z, 0, 0, 0, 0, 1, 0);
            Matrix.multiplyMM(model_view_matrix, 0, view_matrix, 0, model_matrix, 0);
        }

        public void initialize_projection(int width, int height) {
            float ratio = (float) width / height;
            float k = 0.5f;
            float left = -k * ratio;
            float right = k * ratio;
            float bottom = -k;
            float top = k;
            float near = 0.1f;
            float far = 10.0f;
            Matrix.frustumM(projection_matrix, 0, left, right, bottom, top, near, far);
            Matrix.multiplyMM(model_view_projection_matrix, 0, projection_matrix, 0, model_view_matrix, 0);
        }

        public void create_shader() {
            String vertex_shader =
                    "uniform mat4 u_modelViewProjectionMatrix;"+
                            "attribute vec3 a_vertex;"+
                            "attribute vec3 a_normal;"+
                            "varying vec3 v_vertex;"+
                            "varying vec3 v_normal;"+
                            "void main() {"+
                            "v_vertex = a_vertex;"+
                            "vec3 n_normal = normalize(a_normal);"+
                            "v_normal = n_normal;"+
                            "gl_Position = u_modelViewProjectionMatrix * vec4(a_vertex, 1.0);"+
                            "}";

            String fragment_shader =
                    "precision mediump float;"+
                            "uniform vec3 u_camera;"+
                            "uniform vec3 u_lightPosition;"+
                            "varying vec3 v_vertex;"+
                            "varying vec3 v_normal;"+
                            "varying vec4 v_color;"+
                            "void main() {"+
                            "vec3 n_normal = normalize(v_normal);"+
                            "vec3 lightvector = normalize(u_lightPosition - v_vertex);"+
                            "vec3 lookvector = normalize(u_camera - v_vertex);"+
                            "float ambient = 0.05;"+
                            "float k_diffuse = 0.8;"+
                            "float k_specular = 0.4;"+
                            "float diffuse = k_diffuse * max(dot(n_normal, lightvector), 0.0);"+
                            "vec3 reflectvector = reflect(-lightvector, n_normal);"+
                            "float specular = k_specular * pow(max(dot(lookvector, reflectvector),0.0), 40.0);"+
                            "vec4 one = vec4(1.0, 1.0, 1.0, 1.0);"+
                            "gl_FragColor = (ambient + diffuse + specular) * one;"+
                            "}";

            mShader = new Shader(vertex_shader, fragment_shader);
            mShader.link_vertex_buffer(mVertexBuffer);
            mShader.link_normal_buffer(mVertexBuffer);
        }

        public void linking_attribites() {
            mShader.link_model_view_projection_matrix(model_view_projection_matrix);
            mShader.link_camera(camera_x, camera_y, camera_z);
            mShader.link_light_source(light_x, light_y, light_z);
            mShader.use_program();
        }

        public void draw_sphere() {
            for (int i = 0; i < n; i += 4) {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, i,4);
            }
        }
}
