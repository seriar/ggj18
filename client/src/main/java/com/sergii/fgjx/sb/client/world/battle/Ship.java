package com.sergii.fgjx.sb.client.world.battle;

import com.sergii.fgjx.sb.client.rendering.Model;
import com.sergii.fgjx.sb.client.rendering.Shader;
import com.sergii.fgjx.sb.client.rendering.Texture;
import com.sergii.fgjx.sb.client.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ship {

    public static final float ZERO = 0f;

    public static final float PI = 3.14159f;
    public static final float DELTA_ANGLE = 0.52359f; // pi/6
    public static final float DELTA_POS = 25;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TEXTURE_FILE_1 = "green.png";
    private static final String TEXTURE_FILE_2 = "red.png";

    private final int width;
    private final int posX;
    private final int posY;


    private final Model model;
    private Texture texture;
    private final float angle;

    public Ship(int width, int posX, int posY, boolean left) {
        this.width = width;

        this.posX = posX;
        this.posY = posY;
        String textureOption;
        if (left) {
            angle = ZERO;
            textureOption = TEXTURE_FILE_1;
        } else {
            angle = PI;
            textureOption = TEXTURE_FILE_2;
        }

        float[] vertices = new float[]{
                -1f,   .5f,  .0f,   //0
                1f,   0f,  .0f,    //1
                -1f,  -.5f,  .0f,    //2
        };
        float[] textureCoords = new float[]{
                0, 0,
                1, 1,
                0, 1
        };
        int[] incices = new int[]{
                0,1,2
        };
        model = new Model(vertices, textureCoords, incices);
        texture = new Texture(textureOption);
        texture.bind(0);
    }

    public void update(float delta){

    }

    public void render(Shader shader){
        shader.bind();

        Matrix4f projection = new Matrix4f(World.PROJECTION);
        Matrix4f target = new Matrix4f(projection.translate(new Vector3f(posX, posY,0)));
        target.rotate(angle, 0, 0, 1);
        target.mul(new Matrix4f().scale(width * 2));

        shader.setUniformValue("sampler", 0);
        shader.setUniform("projection", target);

        texture.bind();
        model.render();
        shader.unbind();
    }

}
