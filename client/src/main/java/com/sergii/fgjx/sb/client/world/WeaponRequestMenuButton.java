package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.api.Messages;
import com.sergii.fgjx.sb.api.messages.responses.Response;
import com.sergii.fgjx.sb.api.messages.responses.SelectWeaponResponse;
import com.sergii.fgjx.sb.api.messages.responses.SessionJoinResponse;
import com.sergii.fgjx.sb.client.Client;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WeaponRequestMenuButton extends SimpleMenuButton {
    public static final int MAX_TIMEOUT = 10;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final Client client;
    protected final int option;

    public WeaponRequestMenuButton(int key, int action, World world, GameState target, int option) {
        super(key, action, world, target);
        client = world.getCommunicationClient();
        this.option = option;
    }

    @Override
    public void invoke() {
        if (Messages.Weapon.values().length > option) {
            try {
                CompletableFuture<Response> future = client.requestWeapon(option);
                Response resp = future.get(MAX_TIMEOUT, TimeUnit.SECONDS);
                if (resp instanceof SelectWeaponResponse) {
                    logger.info("Progressing to {}", target.name());
                    world.progress(target);
                }
            } catch (MqttException e) {
                logger.error("We are doomed", e);
            } catch (InterruptedException e) {
                logger.error("We are doomed: interrupted", e);
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                logger.error("We are doomed: exec", e);
            } catch (TimeoutException e) {
                logger.error("We are doomed: timeouted", e);
            }
        } else {
            logger.debug("Option not available");
        }
    }
}
