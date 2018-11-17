package com.ssd.modelrenderer3d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class OpenGLView extends GLSurfaceView {

    private static float TOUCH_SCALE_FACTOR = 0.002f;
    private float mPreviousX;
    private float mPreviousY;
    private boolean pinchMode = false;
    private float oldDist = 0;
    public static float sizeCoef = 1;
    private ScaleGestureDetector mDetector;
    public static String shape;

    public OpenGLView(Context context) {
        super(context);
        init();
    }

    public OpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(3);
        super.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);

        // Renders primitive shapes
        OpenGLRenderer renderer = new OpenGLRenderer();
        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        mDetector = new ScaleGestureDetector(getContext(), new ScaleDetectorListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        if(mDetector.onTouchEvent(e)){
        }

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {

            case MotionEvent.ACTION_MOVE:

                // handle rotation as per finger drag
                float deltaX = (x - mPreviousX) / 2f;
                float deltaY = (y - mPreviousY) / 2f;

                OpenGLRenderer.mDeltaX += deltaX;
                OpenGLRenderer.mDeltaY += deltaY;
        }

        mPreviousX = x;
        mPreviousY = y;

        return true;
    }

    // pinch zoom functionality
    private class ScaleDetectorListener implements ScaleGestureDetector.OnScaleGestureListener{

        float scaleFocusX = 0;
        float scaleFocusY = 0;

        public boolean onScale(ScaleGestureDetector arg0) {
            float scale = arg0.getScaleFactor() * sizeCoef;

            sizeCoef = scale;

            requestRender();

            return true;
        }

        public boolean onScaleBegin(ScaleGestureDetector arg0) {
            invalidate();

            scaleFocusX = arg0.getFocusX();
            scaleFocusY = arg0.getFocusY();

            return true;
        }

        public void onScaleEnd(ScaleGestureDetector arg0) {
            scaleFocusX = 0;
            scaleFocusY = 0;
        }
    }

}
