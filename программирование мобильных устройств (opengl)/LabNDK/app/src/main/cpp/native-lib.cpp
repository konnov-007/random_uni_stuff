#include <jni.h>
#include <string>
#include <GLES/gl.h>

GLfloat points[] = {
        -1,1,0,
        -1,-1,0,
        1,-1,0,
        1,1,0
};

extern "C" JNIEXPORT void JNICALL
Java_konnov_commr_vk_labndk_Lib_on_1draw_1frame(JNIEnv *env, jobject) {
    glClearColor(0.0,0.0,0.0,1.0);
    glClearDepthf(1.0);
    glEnable(GL_DEPTH_TEST);
    glColorMask(1, 1, 0, 1);
    glEnableClientState(GL_VERTEX_ARRAY);
    glVertexPointer(3, GL_FLOAT,0, points);
    glDrawArrays(GL_TRIANGLE_FAN,0,4);
    glDisableClientState(GL_VERTEX_ARRAY);
}

extern "C" JNIEXPORT void JNICALL
Java_konnov_commr_vk_labndk_Lib_on_1surface_1changed(JNIEnv *env, jobject, jint width,
                                                      jint height) {
    glViewport(0, 0, width, height);
}