package com.sergii.fgjx.sb.client.io;

import org.lwjgl.glfw.GLFW;

public abstract class AnyKeyCallback extends KeyCallback{

    private static final int ZERO = 48;
    private static final int NINE = 57;
    private static final int A = 65;
    private static final int Z = 90;

    protected char pressedKey;

    public AnyKeyCallback() {
        super(0,0);
    }


    @Override
    public boolean matches(int key, int action) {
        if (action == GLFW.GLFW_PRESS) {
            if (key >= 48 && key <= 57) {
                pressedKey = (char) key;
                return true;
            }
            if (key >= 65 && key <= 90) {
                pressedKey = (char) key;
                return true;
            }
        }
        return false;
    }

    @Override
    public void invoke() {

    }
}
