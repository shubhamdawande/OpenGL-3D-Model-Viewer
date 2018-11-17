package com.ssd.modelrenderer3d;

import android.graphics.Color;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Pyramid extends PrimitiveShape{

    private FloatBuffer vertexBuffer;
    private int mProgram;

    // define pyramid co-ordinates
    public static float pLength = 0.4f;
    private float pyramidCoordinates[] = {
            // front top triangle
            0.0f,  pLength, 0.0f, // top
            -pLength, -pLength, pLength, // bottom left
            pLength, -pLength, pLength,  // bottom right

            // right top triangle
            0.0f,  pLength, 0.0f, // top
            pLength, -pLength, pLength, // bottom left
            pLength, -pLength, -pLength,  // bottom right

            // back top triangle
            0.0f,  pLength, 0.0f, // top
            pLength, -pLength, -pLength, // bottom left
            -pLength, -pLength, -pLength,  // bottom right

            // left top triangle
            0.0f,  pLength, 0.0f, // top
            -pLength, -pLength, -pLength, // bottom left
            -pLength, -pLength, pLength  // bottom right
    };

    // define vertex and fragment shaders
    String vertexShaderCode =
            "#version 300 es 			  \n"
                    + "uniform mat4 uMVPMatrix;     \n"
                    + "in vec4 vPosition;           \n"
                    + "void main()                  \n"
                    + "{                            \n"
                    + "   gl_Position = uMVPMatrix * vPosition;  \n"
                    + "}                            \n";

    String fragmentShaderCode =
            "#version 300 es		 			          	\n"
                    + "precision mediump float;					  	\n"
                    + "uniform vec4 vColor;	 			 		  	\n"
                    + "out vec4 fragColor;	 			 		  	\n"
                    + "void main()                                  \n"
                    + "{                                            \n"
                    + "  fragColor = vColor;                    	\n"
                    + "}                                            \n";

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private int COORDS_PER_VERTEX = 3;

    // different colors for pyramid sides
    float redColor[] = { Color.red(Color.RED) / 255f, Color.green(Color.RED) / 255f, Color.blue(Color.RED) / 255f, 1f};
    float greenColor[] = { Color.red(Color.GREEN) / 255f, Color.green(Color.GREEN) / 255f, Color.blue(Color.GREEN) / 255f, 1f};
    float blueColor[] = { Color.red(Color.BLUE) / 255f, Color.green(Color.BLUE) / 255f, Color.blue(Color.BLUE) / 255f, 1f};
    float yellowColor[] = { Color.red(Color.YELLOW) / 255f, Color.green(Color.YELLOW) / 255f, Color.blue(Color.YELLOW) / 255f, 1f};
    float grayColor[] = { Color.red(Color.GRAY) / 255f, Color.green(Color.GRAY) / 255f, Color.blue(Color.GRAY) / 255f, 1f};
    float cyanColor[] = { Color.red(Color.CYAN) / 255f, Color.green(Color.CYAN) / 255f, Color.blue(Color.CYAN) / 255f, 1f};

    //private final int vertexCount = pyramidCoordinates.length / COORDS_PER_VERTEX;
    //private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex


    public Pyramid() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                pyramidCoordinates.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(pyramidCoordinates);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        // shaders
        int vertexShader = OpenGLRenderer.loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = OpenGLRenderer.loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES30.glCreateProgram();

        // add the vertex shader to program
        GLES30.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES30.glAttachShader(mProgram, fragmentShader);

        // attach vPosition to attribute 0
        GLES30.glBindAttribLocation(mProgram, 0, "vPosition");

        // creates OpenGL ES program executables
        GLES30.glLinkProgram(mProgram);

        int[] linked = new int[1];
        GLES30.glGetProgramiv(mProgram, GLES30.GL_LINK_STATUS, linked, 0);

    }

    // draw function
    @Override
    public void draw(float[] mMVPMatrix) {

        // Add program to OpenGL ES environment
        GLES30.glUseProgram(mProgram);

        mMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        OpenGLRenderer.checkGlError("glGetUniformLocation");

        // get handle to fragment shader's vColor member
        mColorHandle = GLES30.glGetUniformLocation(mProgram, "vColor");

        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        vertexBuffer.position(0);
        GLES30.glVertexAttribPointer(0, COORDS_PER_VERTEX, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        GLES30.glEnableVertexAttribArray(0);

        // Here we draw all the pyramid sides
        int startPos = 0;
        // Set color for drawing the pyramid
        GLES30.glUniform4fv(mColorHandle, 1, redColor, 0);
        // Draw the pyramid triangle
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, startPos, COORDS_PER_VERTEX);
        startPos += COORDS_PER_VERTEX;

        GLES30.glUniform4fv(mColorHandle, 1, greenColor, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, startPos, COORDS_PER_VERTEX);
        startPos += COORDS_PER_VERTEX;

        GLES30.glUniform4fv(mColorHandle, 1, yellowColor, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, startPos, COORDS_PER_VERTEX);
        startPos += COORDS_PER_VERTEX;

        GLES30.glUniform4fv(mColorHandle, 1, blueColor, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, startPos, COORDS_PER_VERTEX);
        startPos += COORDS_PER_VERTEX;

    }

}
