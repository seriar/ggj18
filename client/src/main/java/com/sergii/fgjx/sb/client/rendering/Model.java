package com.sergii.fgjx.sb.client.rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBindBuffer;

public class Model {

    private static final int DRAW_SIZE = 3;
    private final int drawCount;
    private final int vertexId;
    private final int textureId;
    private final int indicesId;


    public Model(float[] verticles, float[] textureCoords, int[] indices){
        drawCount = indices.length;
        vertexId = GL15.glGenBuffers();

        FloatBuffer vertBuffer = createFloatBuffer(verticles);
        FloatBuffer textCoorduffer = createFloatBuffer(textureCoords);
        IntBuffer indicesBuffer = createIntBuffer(indices);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexId);
        glBufferData(GL15.GL_ARRAY_BUFFER, vertBuffer, GL_STATIC_DRAW);

        textureId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureId);
        glBufferData(GL15.GL_ARRAY_BUFFER, textCoorduffer, GL_STATIC_DRAW);

        indicesId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void render(){
        GL20.glEnableVertexAttribArray(0); // FROM TEXTURES
        GL20.glEnableVertexAttribArray(1); // FROM TEXTURES

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexId);
        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureId);
        GL20.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesId);
        GL11.glDrawElements(GL11.GL_TRIANGLES, drawCount, GL11.GL_UNSIGNED_INT, 0);

        // UNBIND
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

    }

    private FloatBuffer createFloatBuffer(float[] verticles) {
        FloatBuffer data = BufferUtils.createFloatBuffer(verticles.length);
        data.put(verticles);
        data.flip();
        return data;
    }

    private IntBuffer createIntBuffer(int[] indices) {
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
        return indicesBuffer;
    }

}
