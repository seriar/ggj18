package com.sergii.fgjx.sb.client.rendering;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Camera {

    public static final int SCALE = 128;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Vector3f position;
    private Matrix4f projection;
    private Matrix4f scale;
    private int worldWidth = 0;
    private int worldHeight = 0;

    private final Vector3f home;
    private final int halfWidth;
    private final int halfHeight;
    private int left;
    private int top;

    public Camera(int width, int height) {

        halfWidth = width / 2;
        halfHeight = height / 2;
        position = new Vector3f(-halfWidth, halfHeight,0);
        home = position;
        scale = new Matrix4f().scale(SCALE);
        projection = new Matrix4f().setOrtho2D(-halfWidth, halfWidth, -halfHeight, halfHeight);

    }

    public void bindToWorldSize(int x, int y){
        worldWidth = x * SCALE;
        worldHeight = y * SCALE;
        left = -halfWidth + SCALE / 2;
        top = halfHeight - SCALE / 2;
        position = new Vector3f(left, top,0);
    }
    public void home(){
        this.position = new Vector3f(home);
    }
    public void setPosition(Vector3f position) {
        // left with scale
        if( position.x > left){
            position.x = left;
        }
        // world without one screen
        if( position.x < - worldWidth + (SCALE / 2 + halfWidth) ){
            position.x = - worldWidth + (SCALE / 2 + halfWidth) ;
        }
        // top with scale

        if( position.y < top){
            position.y = top;
        }
        // world without one screen
        if( position.y > worldHeight - (SCALE / 2 + halfHeight) ){
            position.y = worldHeight - (SCALE / 2 + halfHeight);
        }

        this.position = position;
    }

    public void setPosition(float x, float y, float z) {
        this.setPosition(new Vector3f(x, y, z));
    }
    public void addPosition(float x, float y, float z) {
        float x1 = position.x + x;
        float y1 = position.y + y;
        float z1 = position.z + z;
        this.setPosition(x1, y1, z1);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f getProjection() {
        Matrix4f target = new Matrix4f();
        Matrix4f pos = new Matrix4f().setTranslation(position);
        projection.mul(pos, target);
        return target;
    }

    public Matrix4f getScale(){
        return scale;
    }
}
