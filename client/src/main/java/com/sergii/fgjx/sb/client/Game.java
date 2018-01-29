package com.sergii.fgjx.sb.client;

import com.sergii.fgjx.sb.client.io.KeyboardControl;
import com.sergii.fgjx.sb.client.io.Timer;
import com.sergii.fgjx.sb.client.io.Window;
import com.sergii.fgjx.sb.client.rendering.Shader;
import com.sergii.fgjx.sb.client.world.World;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;

public class Game {
    public static final int[][] RESOLUTIONS = {{640, 480}, {1024, 800}, {1200, 900}};

    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Window window;
    private final KeyboardControl keyboardControl;
    private final Client client;
    public Game(Client client) {
        logger.info("Starting the game...");
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize glfw");
        }
        window = new Window(RESOLUTIONS[1][0],RESOLUTIONS[1][1], false);

        GL.createCapabilities();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.client = client;
        keyboardControl = new KeyboardControl();
        window.registerKeyboardControl(keyboardControl);

    }

    public void start(CompletableFuture win, CompletableFuture lose) {
        logger.info("Game main cycle started.");
        //soundManager.play("background");
        double frameCap = 1.0 / 60.0;
        double startTime = Timer.getTime();
        double unprocessed = 0.0;
        double frameTime = 0.0;
        int frames = 0;
        Shader shader = new Shader("shader");
        World world = new World(client, keyboardControl, win, lose);
        world.registerKeys(keyboardControl);

//        world.registerSoundSystem(soundManager);
        while (!window.shouldClose() && !world.isClosing()) {

            boolean shouldRender = false;

            double currentTime = Timer.getTime();
            double passed = currentTime - startTime;
            startTime = currentTime;
            unprocessed += passed;
            frameTime += passed;
            while (unprocessed >= frameCap) {
                shouldRender = true;

                unprocessed -= frameCap;
                glfwPollEvents();

                if (frameTime >= 1.0) {
                    logger.trace("Current FPS: {}", frames);
                    frameTime = 0.0;
                    frames = 0;
                }
            }

            if (shouldRender) {

                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

//                if (!world.pause) {
                    world.update((float) frameCap);
//                }
//
                world.render(shader);

                window.swapBuffers();
                frames++;
            }

        }

        GLFW.glfwTerminate();
        client.stop();
        System.exit(0);
    }

}
