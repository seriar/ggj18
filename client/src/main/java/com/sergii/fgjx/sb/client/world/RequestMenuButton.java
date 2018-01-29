package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.api.messages.responses.Response;
import com.sergii.fgjx.sb.api.messages.responses.SessionListResponse;
import com.sergii.fgjx.sb.client.Client;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RequestMenuButton extends SimpleMenuButton {
    public static final int MAX_TIMEOUT = 10;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final Client client;
    public RequestMenuButton(int key, int action, World world, GameState target) {
        super(key, action, world, target);
        client = world.getCommunicationClient();
    }

    @Override
    public void invoke() {
        try {
            CompletableFuture<Response> future = client.requestSessions();
            Response resp = future.get(MAX_TIMEOUT, TimeUnit.SECONDS);
            if (resp instanceof SessionListResponse) {
                final SessionListResponse response = (SessionListResponse) resp;

                Set<String> sessionIds = response.getSessionIds();
                String [] sessions = sessionIds.toArray(new String[sessionIds.size()]);


                world.getScreenManager().updateSessions(sessions);
                List<String> availableSessions = Arrays.asList(sessions);
                world.setAvailableSessions(availableSessions);
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
    }
}
