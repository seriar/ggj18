package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.client.Client;
import com.sergii.fgjx.sb.client.io.KeyboardControl;
import com.sergii.fgjx.sb.client.rendering.Shader;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

import static com.sergii.fgjx.sb.client.Game.RESOLUTIONS;

public class World {
    public static final int WIDTH = RESOLUTIONS[0][0];
    public static final int HEIGHT = RESOLUTIONS[0][1];
    public static final Matrix4f PROJECTION = new Matrix4f().setOrtho2D(-WIDTH / 2, WIDTH / 2, -HEIGHT / 2, HEIGHT / 2);
    private boolean closing;
    private final ScreenManager screenManager;
    private final Client communicationClient;
    private GameStateMachine gameStateMachine;
    private KeyboardControl control;
    private String sessionId;
    private List<String> availableSessions;
    CompletableFuture win;
    CompletableFuture lose;
    private static String code = "";
    private static String activeCode = "";

    private StringBuilder activaton;


    public World(Client communicationClient, KeyboardControl control, CompletableFuture win, CompletableFuture lose) {
        this.win = win;
        this.lose = lose;
        gameStateMachine = new GameStateMachine();
        this.control = control;
        this.communicationClient = communicationClient;
        screenManager = new ScreenManager(this);
        availableSessions = new ArrayList<>();
        activaton = new StringBuilder();
    }


    public String getCode() {
        return code;
    }
    public String getActiveCode() {
        return activeCode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void addChar(char character) {
        activaton.append(character);
    }
    public String getActivationCode() {
        final String code = activaton.toString();
        activaton = new StringBuilder();
        return code;
    }

    public KeyboardControl getKeyboardControl() {
        return control;
    }

    public GameState getState() {
        return gameStateMachine.getState();
    }

    public Client getCommunicationClient() {
        return communicationClient;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
        communicationClient.setSessionId(sessionId);
    }

    public List<String> getAvailableSessions() {
        return availableSessions;
    }

    public void setAvailableSessions(List<String> availableSessions) {
        this.availableSessions = availableSessions;
    }

    public void progress(GameState next) {
        gameStateMachine.nextState(next);
        if (gameStateMachine.getState() == GameState.EXIT) {
            closing = true;
        }
    }


    public void progressAfter(int seconds, GameState target) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                progress(target);
            }
        }, seconds*1000);
    }


    public void registerKeys(KeyboardControl control) {
        this.control = control;
    }

    public boolean isClosing() {
        return closing;
    }

    public void setClosing(boolean closing) {
        this.closing = closing;
    }

    public void update(float delta) {
        if (win.isDone()) {
            gameStateMachine.win();
        }
        if (lose.isDone()) {
            gameStateMachine.lost();
        }
        screenManager.update(delta, gameStateMachine.getState());

    }

    public void render(Shader shader) {
        screenManager.render(shader, gameStateMachine.getState());
    }

    public static String getStaticCode() {
        return code;
    }

}
