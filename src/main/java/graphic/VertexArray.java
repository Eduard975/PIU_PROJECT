package graphic;

import utils.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static utils.BufferUtils.createFloatBuffer;

import utils.BufferUtils;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArray {

    private int vao, vbo, ibo, tbo;
    private int count;

    public VertexArray(int count) {
        this.count = count;
        vao = glGenVertexArrays();
    }

    public VertexArray(float[] vertices, byte[] indices, float[] textureCoordinates) {
        count = indices.length;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);

        tbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(textureCoordinates), GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.TCOORD_ATTRIB, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.TCOORD_ATTRIB);

        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void bind() {
        glBindVertexArray(vao);
        if (ibo > 0)
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
    }

    public void unbind() {
        if (ibo > 0)
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
    }

    public void draw() {
        if (ibo > 0)
            glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
        else
            glDrawArrays(GL_TRIANGLES, 0, count);
    }

    public void updateTexCoords(float[] textureCoordinates) {
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, BufferUtils.createFloatBuffer(textureCoordinates));
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }


    public void render() {
        bind();
        draw();
    }

    public void updateVertices(float[] vertices) {
        // Bind the VBO (assumes vboID is the ID of the vertex buffer object)
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        // Update the vertex data in the GPU buffer
        glBufferSubData(GL_ARRAY_BUFFER, 0, createFloatBuffer(vertices));

        // Unbind the buffer (optional, but good practice)
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

}