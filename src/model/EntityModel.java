package model;

import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class EntityModel {
    private int count;

    private int vboID;
    private int indicesID;
    private int textureID;
    private int normalsID;

    public EntityModel(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
        count = indices.length;
        vboID = createVertexBuffer(storeDataInFloatBuffer(vertices));
        textureID = createVertexBuffer(storeDataInFloatBuffer(textureCoords));
        normalsID = createVertexBuffer(storeDataInFloatBuffer(normals));
        indicesID = createIndicesBuffer(storeDataInIntBuffer(indices));
    }

    public EntityModel(float[] vertices, int dimension) {
        count = vertices.length / dimension;
        vboID = createVertexBuffer(storeDataInFloatBuffer(vertices));
    }

    public int getCount() {
        return count;
    }

    public int getVboID() {
        return vboID;
    }

    public int getIndicesID() {
        return indicesID;
    }

    public int getTextureID() {
        return textureID;
    }

    public int getNormalsID() { return normalsID; }

    public void clean() {
        glBindBuffer(GL_VERTEX_ARRAY, 0);
        glDeleteBuffers(vboID);
        glDeleteBuffers(textureID);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(indicesID);
    }

    private int createVertexBuffer(FloatBuffer buffer) {
        int objectID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, objectID);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return objectID;
    }

    private int createIndicesBuffer(IntBuffer buffer) {
        int objectID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, objectID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        return objectID;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] vertices) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices).flip();
        return buffer;
    }

    private IntBuffer storeDataInIntBuffer(int[] indices) {
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices).flip();
        return buffer;
    }
}
