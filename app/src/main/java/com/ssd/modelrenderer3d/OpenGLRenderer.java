package com.ssd.modelrenderer3d;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.file.OpenOption;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

    private PrimitiveShape mShape;
    private static float mTransX = 0, mTransY = 0;
    private static float mAngle = 0;
    private static String TAG = "OpenGLRenderer";
    public static float mDeltaX = 0, mDeltaY = 0;
    public static float scaleF = 1f;

    // projection matrix
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private final float[] mAccRotationMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0f,0f,0f,0.9f);

        if(OpenGLView.shape.equals("cube")){
            mShape = new Cube();
        }
        else if(OpenGLView.shape.equals("pyramid")){
            mShape = new Pyramid();
        }

        Matrix.setIdentityM(mAccRotationMatrix, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0,0, width, height);

        float aspect = (float) width/height;
        Matrix.perspectiveM(mProjectionMatrix, 0, 53.13f, aspect, 1f, 40f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        GLES30.glEnable(GLES30.GL_DEPTH_TEST);

        // camera view set
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        Matrix.setIdentityM(mRotationMatrix, 0);

        // handle translation
        Matrix.translateM(mRotationMatrix, 0, mTransX, mTransY, 0);

        // handle rotation
        Matrix.rotateM(mRotationMatrix, 0, mDeltaX, 0,1,0);
        Matrix.rotateM(mRotationMatrix, 0, -mDeltaY, 1,0,0);
        mDeltaX = 0f;
        mDeltaY = 0f;
        float[] mTemporaryMatrix = new float[16];
        Matrix.multiplyMM(mTemporaryMatrix, 0, mRotationMatrix, 0, mAccRotationMatrix, 0);
        System.arraycopy(mTemporaryMatrix, 0, mAccRotationMatrix, 0, 16);

        // combine rotation and translation
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mAccRotationMatrix, 0);

        // scaling
        Matrix.scaleM(mMVPMatrix, 0, OpenGLView.sizeCoef, OpenGLView.sizeCoef, OpenGLView.sizeCoef);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        mShape.draw(mMVPMatrix);

        //mAngle+=.4;
    }

    // load and compile shader
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES30.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);

        return shader;
    }

    // error check utility
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    //used the touch listener to move the cube up/down (y) and left/right (x)
    public static float getY() {
        return mTransY;
    }

    public static void setY(float mY) {
        mTransY = mY;
    }

    public static  float getX() {
        return mTransX;
    }

    public static void setX(float mX) {
        mTransX = mX;
    }

    public static void setAngle(){
        mAngle += .4;
    }
}
