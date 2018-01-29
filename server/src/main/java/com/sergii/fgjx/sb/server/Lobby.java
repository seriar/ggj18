package com.sergii.fgjx.sb.server;

import com.sergii.fgjx.sb.api.Topics;
import com.sergii.fgjx.sb.api.messages.MessageSerializer;
import com.sergii.fgjx.sb.api.messages.StringSerializer;
import com.sergii.fgjx.sb.api.messages.requests.*;
import com.sergii.fgjx.sb.api.messages.responses.PlayerListResponse;
import com.sergii.fgjx.sb.api.messages.responses.SessionJoinResponse;
import com.sergii.fgjx.sb.api.messages.responses.SessionListResponse;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Lobby implements MqttCallback {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SessionManager sessionManager;
    private final String topic;
    private final String id;
    private final MqttClient client;
    private final MessageSerializer deserializer;

    public Lobby() {
        logger.info("Creating the lobby on broker - {}", Server.BROKER);
        sessionManager = new SessionManagerImpl();
        sessionManager.createSession();
        sessionManager.createSession();
        sessionManager.createSession();
        deserializer = new StringSerializer();
        topic = Topics.LOBBY.value();
        logger.info("Lobby topic is set to be - {}", topic);
        id = "lobby-" + UUID.randomUUID().toString();
        logger.info("Server id - {}", id);

        try {
            client = new MqttClient(Server.BROKER, id, Server.PERSISTENCE);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            logger.info("Connecting...");
            client.connect(options);
            logger.info("Seting callbacks...");
            client.setCallback(this);
            logger.info("Subscribing...");
            client.subscribe(topic);
            logger.info("Lobby is set up!");
        } catch (MqttException e) {
            throw new IllegalStateException("Cannot connect to broker" + Server.BROKER, e);
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        logger.error("Connection lost for lobby! ", throwable);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        logger.info("Server received a request");
        Request request = deserializer.deserializeRequest(mqttMessage.getPayload());
        if (request != null) {
            if (request instanceof SessionCreationRequest) {
                logger.info("Processing a session creation request");

                Session session = sessionManager.createSession();
                session.joinSession(request.getRequester(), request.getId());
            } else if (request instanceof SessionListRequest) {
                logger.info("Processing a session list request");
                Set<String> sessions = sessionManager.listPendingSessions();
                String requestId = request.getId();
                logger.info("Processing a session list request: {}", requestId);

                byte[] bytes = deserializer.serializeResponse(new SessionListResponse(requestId, sessions, sessions.size()));
                try {
                    logger.info("Replying to : {}", request.getRequester());
                    client.publish(Topics.PLAYER.value(request.getRequester()), new MqttMessage(bytes));
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            } else if (request instanceof SessionJoinRequest) {
                logger.info("Processing a session join request");
                SessionJoinRequest joinRequest = (SessionJoinRequest) request;
                logger.info("Processing a session player join request, {}", joinRequest.getSession());
                sessionManager.joinSession(joinRequest.getSession(), joinRequest.getRequester(), request.getId());
            } else if (request instanceof PlayerListRequest) {
                logger.info("Processing a session player list request");

                PlayerListRequest playerListRequest = (PlayerListRequest) request;
                final String sessinoId = playerListRequest.getSessionId();

                Session session = sessionManager.getSession(sessinoId);
                if (session != null) {
                    publishPlayers(request.getRequester(), request.getId(), session.getPlayers());
                }

            } else {
                logger.warn("Parsed request is not handled");
            }
        } else {
            logger.warn("Request cannot be parsed");
        }
    }



    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    private void publishJoinFailure(String requesterId, String id) {
        final SessionJoinResponse sessionJoinResponse = new SessionJoinResponse(id, this.id,false);
        try {
            final MqttMessage message = new MqttMessage(deserializer.serializeResponse(sessionJoinResponse));
            client.publish(Topics.PLAYER.value(requesterId), message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void publishJoinSuccess(String requesterId, String id) {
        final SessionJoinResponse sessionJoinResponse = new SessionJoinResponse(id, this.id,false);
        try {
            final MqttMessage message = new MqttMessage(deserializer.serializeResponse(sessionJoinResponse));
            client.publish(Topics.PLAYER.value(requesterId), message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void publishPlayers(String requesterId, String id, List<String> players) {
        final PlayerListResponse response = new PlayerListResponse(id, players);
        final MqttMessage message = new MqttMessage(deserializer.serializeResponse(response));
        try {
            client.publish(Topics.PLAYER.value(requesterId), message);
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

}
