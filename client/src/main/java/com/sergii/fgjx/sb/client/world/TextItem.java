package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.client.rendering.Model;
import com.sergii.fgjx.sb.client.rendering.Shader;
import org.apache.commons.lang3.ArrayUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class TextItem {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final int VERTICES_PER_CHAR = 4;
    private Model model;
    private String text;
    private int cols;
    private int rows;
    private float charWidth;
    private float charHeight;


    private FontTexture fontTexture;

    public TextItem(int cols, int rows, float charWidth, float charHeight, FontTexture fontTexture) {
        this.cols = cols;
        this.rows = rows;
        this.fontTexture = fontTexture;
        this.charHeight = charHeight;
        this.charWidth = charWidth;
    }

    public void setText(String text) {
        this.text = text;
        this.model = buildMesh(text, fontTexture);
    }

    public String getText() {
        return text;
    }

    private Model buildMesh(String text, FontTexture fontTexture) {
        ArrayList<Float> verticesList = new ArrayList();
        ArrayList<Float> textureCoordinatesList = new ArrayList();
        ArrayList<Integer> indicesList = new ArrayList();

        char[] characters = text.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int character = characters[i];
            int x = character % fontTexture.getTextureCols();
            int y = character / fontTexture.getTextureCols();

            int screenPosX = i % cols;
            int screenPosY = i / cols;

            // top left
            verticesList.add((float) screenPosX * charWidth);
            verticesList.add((float) -screenPosY * charHeight);
            verticesList.add(0.0f);

            textureCoordinatesList.add((float) x * fontTexture.getCharacterWidth());
            textureCoordinatesList.add((float) y * fontTexture.getCharacterHeight());

            indicesList.add(i * VERTICES_PER_CHAR);

            // bottom left
            verticesList.add((float) screenPosX * charWidth);
            verticesList.add((float) - (screenPosY + 1) * charHeight);
            verticesList.add(0.0f);

            textureCoordinatesList.add((float) x * fontTexture.getCharacterWidth());
            textureCoordinatesList.add((float) (y + 1) * fontTexture.getCharacterHeight());

            indicesList.add(i * VERTICES_PER_CHAR + 1);


            // bottom right
            verticesList.add((float) (screenPosX + 1) * charWidth);
            verticesList.add((float) - (screenPosY + 1) * charHeight);
            verticesList.add(0.0f);

            textureCoordinatesList.add((float) (x + 1) * fontTexture.getCharacterWidth());
            textureCoordinatesList.add((float) (y + 1) * fontTexture.getCharacterHeight());

            indicesList.add(i * VERTICES_PER_CHAR + 2);

            // top right
            verticesList.add((float) (screenPosX + 1) * charWidth);
            verticesList.add((float) -screenPosY * charHeight);
            verticesList.add(0.0f);

            textureCoordinatesList.add((float) (x + 1) * fontTexture.getCharacterWidth());
            textureCoordinatesList.add((float) (y) * fontTexture.getCharacterHeight());

            indicesList.add(i * VERTICES_PER_CHAR + 3);
            // close upper right triangle
            indicesList.add(i * VERTICES_PER_CHAR);
            indicesList.add(i * VERTICES_PER_CHAR + 2);

        }

        float[] vertices = ArrayUtils.toPrimitive(verticesList.toArray(new Float[0]),0.0f);
        float[] textureCoordinates = ArrayUtils.toPrimitive(textureCoordinatesList.toArray(new Float[0]),0.0f);
        int[] indices = ArrayUtils.toPrimitive(indicesList.toArray(new Integer[0]),0);
        if (logger.isDebugEnabled()) {
            logger.trace("Created vertices {}", ArrayUtils.toString(vertices));
            logger.trace("Created textureCoordinates {}", ArrayUtils.toString(textureCoordinates));
            logger.trace("Created indices {}", ArrayUtils.toString(indices));
        }
        return new Model(vertices, textureCoordinates, indices);

    }


    public void render(Shader shader, int posX, int posY, int scale) {
        shader.bind();

        Matrix4f projection = new Matrix4f(World.PROJECTION);
        Matrix4f target = new Matrix4f(projection.translate(new Vector3f(posX, posY,0)));
        target.mul(new Matrix4f().scale(scale));

        shader.setUniformValue("sampler", 0);
        shader.setUniform("projection", target);


        fontTexture.getTexture().bind();
        model.render();

        shader.unbind();
    }

}
