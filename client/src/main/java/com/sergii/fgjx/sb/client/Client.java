package com.sergii.fgjx.sb.client;

import com.sergii.fgjx.sb.api.Messages;
import com.sergii.fgjx.sb.api.Topics;
import com.sergii.fgjx.sb.api.messages.MessageSerializer;
import com.sergii.fgjx.sb.api.messages.StringSerializer;
import com.sergii.fgjx.sb.api.messages.requests.*;
import com.sergii.fgjx.sb.api.messages.responses.*;
import com.sergii.fgjx.sb.client.world.World;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Client implements MqttCallback {

    private static final MemoryPersistence PERSISTENCE = new MemoryPersistence();;
    private static String DEFAULT_BROKER = "tcp://localhost:1883";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String id;
    private final MessageSerializer serializer;
    private MqttClient client;
    private String sessionId;
    public int sessions = 0;
    static CompletableFuture win = new CompletableFuture();
    static CompletableFuture lose = new CompletableFuture();
    private final HashMap<String, CompletableFuture<Response>> pedning;

    public static void main(String [] args) {
        if (args.length == 1)
            DEFAULT_BROKER = args[0];
        Client client = new Client();
        client.start(DEFAULT_BROKER);
        Game game = new Game(client);
        game.start(win, lose);
    }

    public Client() {
        pedning = new HashMap<>();
        id = UUID.randomUUID().toString();
        serializer = new StringSerializer();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    private void start(String broker) {
        logger.info("Starting SB client...");
        logger.info("Connecting to lobby...");
        try {
            client = new MqttClient(broker, id, PERSISTENCE);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.connect(options);
            client.setCallback(this);
            String myTopic = Topics.PLAYER.value(id);
            logger.info("Client connected on {}", myTopic);
            client.subscribe(myTopic);
            client.subscribe(Topics.PLAYER_LOST.value(id));
            client.subscribe(Topics.PLAYER_WON.value(id));
            client.subscribe(myTopic);
            requestSessions();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<Response> requestSessions() throws MqttException {
        SessionListRequest request = new SessionListRequest(id, 4);
        String requestId = request.getId();
        logger.info("Requesting sessions: reqId = {}", requestId);
        byte[] b = serializer.serializeRequest(request);
        client.publish(Topics.LOBBY.value(), new MqttMessage(b));
        CompletableFuture<Response> future = new CompletableFuture<>();
        pedning.put(requestId, future);
        return future;
    }


    public CompletableFuture<Response> requestJoinSession(String sessionId) throws MqttException {
        SessionJoinRequest request = new SessionJoinRequest(id, sessionId);
        String requestId = request.getId();
        logger.info("Requesting to joint: reqId = {}, session = {}", requestId, sessionId);
        byte[] b = serializer.serializeRequest(request);
        client.publish(Topics.LOBBY.value(), new MqttMessage(b));
        CompletableFuture<Response> future = new CompletableFuture<>();
        pedning.put(requestId, future);
        return future;
    }

    public CompletableFuture<Response> requestPlayers() throws MqttException {
        PlayerListRequest request = new PlayerListRequest(id, sessionId); // TODO: session id kinda not needed
        String requestId = request.getId();
        logger.info("Requesting players: reqId = {}", requestId);
        byte[] b = serializer.serializeRequest(request);
        client.publish(Topics.SESSION.value(sessionId), new MqttMessage(b));
        CompletableFuture<Response> future = new CompletableFuture<>();
        pedning.put(requestId, future);
        return future;
    }

    public CompletableFuture<Response> requestRole() throws MqttException {
        RoleRequest request = new RoleRequest(id); // TODO: session id kinda not needed
        String requestId = request.getId();
        logger.info("Requesting role: reqId = {}", requestId);
        byte[] b = serializer.serializeRequest(request);
        client.publish(Topics.SESSION.value(sessionId), new MqttMessage(b));
        CompletableFuture<Response> future = new CompletableFuture<>();
        pedning.put(requestId, future);
        return future;
    }

    public CompletableFuture<Response> requestWeapon(int option) throws MqttException {
        if (option < 4) {
            Messages.Weapon weapon = Messages.Weapon.forNumber(option);
            SelectWeaponRequest request = new SelectWeaponRequest(id, weapon);
            String requestId = request.getId();
            logger.debug("Requesting weapon: reqId = {}, weapon: {}", requestId, weapon.name());
            byte[] b = serializer.serializeRequest(request);
            client.publish(Topics.SESSION.value(sessionId), new MqttMessage(b));
            CompletableFuture<Response> future = new CompletableFuture<>();
            pedning.put(requestId, future);
            return future;
        }
        return null;
    }

    public CompletableFuture<Response> requestCode() throws MqttException {
        CodeTransmissionRequest request = new CodeTransmissionRequest(id);
        String requestId = request.getId();
        logger.debug("Requesting code: reqId = {}", requestId);
        byte[] b = serializer.serializeRequest(request);
        client.publish(Topics.SESSION.value(sessionId), new MqttMessage(b));
        CompletableFuture<Response> future = new CompletableFuture<>();
        pedning.put(requestId, future);
        return future;
    }

    public void sendAndForgetCode(String code) throws MqttException {
        ActivationRequest request = new ActivationRequest(id, code);
        String requestId = request.getId();
        logger.debug("Requesting se: reqId = {}, code= {} ", requestId, code);
        byte[] b = serializer.serializeRequest(request);
        client.publish(Topics.SESSION.value(sessionId), new MqttMessage(b));
    }

    public void stop() {
        logger.info("Stopping the client {}", id);
        try {
            client.disconnect();
        } catch (MqttException e) {
            logger.error("Cannot disconnect..." , e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        logger.error("Connection lost: {}", id, cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        if (topic.contains("won")) {
            win.complete(null);
            return;
        }
        if (topic.contains("lost")) {
            lose.complete(null);
            return;
        }
        Response response = serializer.deserializeResponse(message.getPayload());
        logger.info("Received message from server: {}", response.toString());
        if (response instanceof SessionListResponse) {
            SessionListResponse listResponse = (SessionListResponse) response;
            sessions = listResponse.getSessionIds().size();
            listResponse.getSessionIds().forEach(session -> logger.info("Session {}", session));
            CompletableFuture<Response> future = pedning.get(response.getId());
            logger.info("Completing {}", response.getId());
            if (future != null) {
                future.complete(listResponse);
            } else {
                logger.info("No requesters {}", response.getId());
            }
        } else if (response instanceof SessionJoinResponse) {
            SessionJoinResponse joinResponse = (SessionJoinResponse) response;
            CompletableFuture<Response> future = pedning.get(response.getId());
            logger.info("Completing {}", response.getId());
            if (future != null) {
                future.complete(joinResponse);
            } else {
                logger.info("No requesters {}", response.getId());
            }
        } else if (response instanceof PlayerListResponse) {

            PlayerListResponse joinResponse = (PlayerListResponse) response;
            CompletableFuture<Response> future = pedning.get(response.getId());
            logger.info("Completing {}", response.getId());
            if (future != null) {
                future.complete(joinResponse);
            } else {
                logger.info("No requesters {}", response.getId());
            }
        } else if (response instanceof RoleResponse) {

            RoleResponse roleReponse = (RoleResponse) response;
            CompletableFuture<Response> future = pedning.get(response.getId());
            logger.info("Completing {}", response.getId());
            if (future != null) {
                future.complete(roleReponse);
            } else {
                logger.info("No requesters {}", response.getId());
            }
        } else if (response instanceof SelectWeaponResponse) {
            SelectWeaponResponse res = (SelectWeaponResponse) response;
            CompletableFuture<Response> future = pedning.get(response.getId());
            logger.info("Completing {}", response.getId());
            if (future != null) {
                future.complete(res);
            } else {
                logger.info("No requesters {}", response.getId());
            }
        } else if (response instanceof CodeTransmissionResponse) {
            CodeTransmissionResponse res = (CodeTransmissionResponse) response;
            CompletableFuture<Response> future = pedning.get(response.getId());
            logger.info("Completing {}", response.getId());
            if (future != null) {
                future.complete(res);
            } else {
                logger.info("No requesters {}", response.getId());
            }
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }


}
