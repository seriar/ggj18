package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.api.messages.responses.PlayerListResponse;
import com.sergii.fgjx.sb.api.messages.responses.Response;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WaitingRoomScreen extends ModifiableMenuScreen {
    private final static float POLL_INTERVAL = 1f;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final World world;
    private float passed = 0f;

    private CompletableFuture<Response> future;
    private int players;

    public WaitingRoomScreen(String text, World world) {
        super(text);
        this.world = world;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        passed += delta;
        if (future != null && future.isDone()) {
            Response value = future.getNow(null);
            if (value != null) {
                future = null;
                if (value instanceof PlayerListResponse) {
                    PlayerListResponse response = (PlayerListResponse) value;

                    List<String> players = response.getPlayers();
                    String[] parameters = players.toArray(new String[players.size()]);
                    this.players = 2;
                    this.updateScreen(parameters);
                    if (parameters.length == this.players) {
                        world.progress(GameState.ROUND_INTRO);
                    }
                }
            }
        }
        if (passed > POLL_INTERVAL && future == null) {
            logger.debug("Requesting the player list.");
            try {
                future = world.getCommunicationClient().requestPlayers();
            } catch (MqttException e) {
                e.printStackTrace();
            }
            passed = 0;
        }
    }

    @Override
    public void updateScreen(String... params) {
        String[] processedParams = {
                "1. Waiting ",
                "2. Waiting ",
                "           ",
                "           "
        };

        for (int i = 0; i < 2 && i < params.length; i++) {
            processedParams[i] = (i + 1) + ". " + processParam(params[i]);
        }
        super.updateScreen(processedParams);
    }

    private String processParam(String param) {
        if (param.length() > 8) {
            return param.substring(0,8);
        } else {
            return StringUtils.leftPad(param, 8);
        }
    }
}
