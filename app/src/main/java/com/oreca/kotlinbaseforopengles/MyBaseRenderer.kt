package com.oreca.kotlinbaseforopengles

import android.content.Context
import android.opengl.GLES32
import android.opengl.GLES32.GL_COLOR_BUFFER_BIT
import android.opengl.GLES32.glClear
import android.opengl.GLES32.glClearColor
import android.opengl.GLES32.glViewport
import android.opengl.GLSurfaceView.Renderer
import com.oreca.kotlinbaseforopengles.util.LoggerConfig
import com.oreca.kotlinbaseforopengles.util.ShaderHelper
import com.oreca.kotlinbaseforopengles.util.TextResourceReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyBaseRenderer(private val context: Context) : Renderer {
    // Vertices data
    private val vertices: FloatArray = floatArrayOf(
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
    )

    // variable to store program, vbo and vao ids
    private var program = 0
    private var VBO = 0
    private var VAO = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // set color in color buffer to light red
        glClearColor(1.0f, 0.5f, 0.5f, 0f)

        // Create program linked vertex and fragment shader
        val vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.vertex_shader)
        val fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.fragment_shader)
        program = ShaderHelper.buildProgram(vertexShaderSource, fragmentShaderSource)
        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(program)
        }
        GLES32.glUseProgram(program)

        // Create IntBuffer to hold the IDs for VAO, VBO
        val vaoBuffer = IntBuffer.allocate(1)
        val vboBuffer = IntBuffer.allocate(1)
        // Generate the VAO, VBO, and EBO using IntBuffer
        GLES32.glGenVertexArrays(1, vaoBuffer)
        GLES32.glGenBuffers(1, vboBuffer)
        // Assign the generated IDs from the IntBuffer to their variables
        VAO = vaoBuffer.get(0)
        VBO = vboBuffer.get(0)

        // Bind the VAO to store state of vbo
        GLES32.glBindVertexArray(VAO)
        // Bind the VBO and upload vertex data
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, VBO)
        // Convert the vertices array to a FloatBuffer
        val vertexBuffer: FloatBuffer = ByteBuffer
            .allocateDirect(vertices.size * Float.SIZE_BYTES)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexBuffer.put(vertices)
        vertexBuffer.position(0)
        // Upload the data to the GPU
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, vertices.size * Float.SIZE_BYTES, vertexBuffer, GLES32.GL_STATIC_DRAW)

        // Define the vertex attribute pointer
        GLES32.glVertexAttribPointer(0, 3, GLES32.GL_FLOAT, false, 3 * Float.SIZE_BYTES, 0)
        GLES32.glEnableVertexAttribArray(0)

        // Unbind the VBO & VAO when not use
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0)
        GLES32.glBindVertexArray(0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)

        // rebind vao and draw triangle
        GLES32.glBindVertexArray(VAO)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, 3)
        GLES32.glBindVertexArray(0)
    }
}