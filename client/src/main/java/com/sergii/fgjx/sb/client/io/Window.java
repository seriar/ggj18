package com.sergii.fgjx.sb.client.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;

public class Window {
    public static final String TITLE = "SPC BRTO";

    private final long windowId;
    private final int width;
    private final int height;
    private boolean fullscreen;

    public Window(int width, int height, boolean fullscreen) {
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        int xpos = (vidMode.width() - width) / 2;
        int ypos = (vidMode.height() - height) / 2;
        windowId = createWindow(TITLE, false, xpos, ypos);
    }

    private long createWindow(String title, boolean fullscreen, int xpos, int ypos){
        long window = 0L;
        window = GLFW.glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : 0L, 0);
        if( window == 0L){
            throw new IllegalStateException("Failed to create a window");
        }

        GLFW.glfwShowWindow(window);
        GLFW.glfwMakeContextCurrent(window);
        if(!fullscreen)
            glfwSetWindowPos(window, xpos, ypos);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        return window;
    }

    public boolean shouldClose(){
        return glfwWindowShouldClose(windowId);
    }

    public void swapBuffers(){
        glfwSwapBuffers(windowId);
    }

    public void registerKeyboardControl(KeyboardControl keyboardControl){
        glfwSetKeyCallback(windowId, keyboardControl);
    }
}
