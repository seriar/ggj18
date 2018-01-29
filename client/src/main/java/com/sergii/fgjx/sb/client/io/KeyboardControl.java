package com.sergii.fgjx.sb.client.io;

import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.ArrayList;
import java.util.List;

public class KeyboardControl implements GLFWKeyCallbackI {

    private List<KeyCallback> callbacks = new ArrayList<>();

    public void registerCallback(KeyCallback callback) {
        callbacks.add(callback);
    }

    public void unregisterCallback(KeyCallback callback){
        callbacks.remove(callback);
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        for (KeyCallback callback : callbacks) {
            if (callback.matches(key, action)) {
                callback.invoke();
            }
        }

    }

}
