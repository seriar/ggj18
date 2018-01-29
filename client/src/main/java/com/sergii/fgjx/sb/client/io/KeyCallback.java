package com.sergii.fgjx.sb.client.io;

public abstract class KeyCallback {

    private final int key;
    private final int action;

    public KeyCallback(int key, int action) {
        this.key = key;
        this.action = action;
    }

    public boolean matches(int key, int action) {
        return key == this.key && action == this.action;
    }

    public abstract void invoke();

}
