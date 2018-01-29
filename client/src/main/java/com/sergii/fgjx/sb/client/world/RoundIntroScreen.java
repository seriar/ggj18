package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.api.messages.responses.Response;
import com.sergii.fgjx.sb.api.messages.responses.RoleResponse;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class RoundIntroScreen extends ModifiableMenuScreen {
    private final static float POLL_INTERVAL = 1f;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final World world;
    private int roundId;

    private CompletableFuture<Response> future;
    private int players;

    public RoundIntroScreen(String text, World world) {
        super(text);
        this.world = world;
        roundId = 1;
        updateScreen();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (future == null) {
            logger.debug("Requesting the player role.");
            try {
                future = world.getCommunicationClient().requestRole();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        if (future != null && future.isDone()) {
            Response value = future.getNow(null);
            if (value != null) {
                future = null;
                if (value instanceof RoleResponse) {
                    RoleResponse response = (RoleResponse) value;
                    if (response.isMaster()) {
                        world.progress(GameState.SELECT_WEAPON);
                    } else {
                        world.progress(GameState.ATTACK);
                    }
                    roundId++;
                    updateScreen();
                }
            }
        }
    }

    public void updateScreen() {
        super.updateScreen(processParam(roundId));
    }

    private String processParam(int size) {
        if (size > 9) {
            return "FINAL";
        } else {
            return StringUtils.leftPad(String.valueOf(size), 5);
        }
    }
}
