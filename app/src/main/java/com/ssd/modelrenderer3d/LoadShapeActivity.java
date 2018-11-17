package com.ssd.modelrenderer3d;

import android.app.Activity;
import android.os.Bundle;

public class LoadShapeActivity extends Activity {

    public static String STRING_EXTRA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get extra parameters passed via intent
        String obj = null;
        if (getIntent().hasExtra(STRING_EXTRA)) {
            obj = getIntent().getStringExtra(STRING_EXTRA);
        }

        // set view as GLSurfaceview
        OpenGLView openGLView = new OpenGLView(this);
        openGLView.shape = obj;
        setContentView(openGLView);
    }

}
