package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.api.messages.responses.CodeTransmissionResponse;
import com.sergii.fgjx.sb.api.messages.responses.Response;
import com.sergii.fgjx.sb.client.Client;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TransmissionButtonLessButton extends ButtonLessButton {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int MAX_TIMEOUT = 15;
    private final Client client;

    public TransmissionButtonLessButton(int key, int action, World world, GameState target) {
        super(key, action, world, target);
        this.client = world.getCommunicationClient();
    }

    @Override
    public void invoke() {
        //nop
    }

    @Override
    public void trigger() {
        try {
            CompletableFuture<Response> future = client.requestCode();
            Response resp = future.get(MAX_TIMEOUT, TimeUnit.SECONDS);
            if (resp instanceof CodeTransmissionResponse) {
                world.setCode(((CodeTransmissionResponse) resp).getCode());
                world.progressAfter(3, target);
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
    }
}
