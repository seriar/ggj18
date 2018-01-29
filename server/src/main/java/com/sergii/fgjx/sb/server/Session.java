package com.sergii.fgjx.sb.server;

import com.sergii.fgjx.sb.api.Messages;
import com.sergii.fgjx.sb.api.Topics;
import com.sergii.fgjx.sb.api.messages.MessageSerializer;
import com.sergii.fgjx.sb.api.messages.StringSerializer;
import com.sergii.fgjx.sb.api.messages.requests.*;
import com.sergii.fgjx.sb.api.messages.responses.*;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

public class Session implements MqttCallback {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String id;
    private final int teamSize = 1;
    private final int totalPlayers = teamSize * 2;
    private final List<String> players;
    private final HashMap<String, MqttMessage> responses;
    private final HashMap<String, MqttMessage> weaponResponses;
    private final HashMap<String, MqttMessage> codeResponses;

    private boolean full;
    private Team teamA = new Team();
    private Team team1 = new Team();
    private String masterA;
    private String master1;

    private Messages.Weapon weapon1;
    private Messages.Weapon weaponA;

    private boolean master1CodeReq;
    private boolean masterACodeReq;

    private final static char[] BURRITO_CHAR_SET = "qwertyuiopasgklzxnm12357890".toCharArray();
    private final static int BURRITO_CODE_LENGTH = 10;
    private final static char[] LASER_CHAR_SET = "12345".toCharArray();
    private final static int LASER_CODE_LENGTH = 6;
    private final static char[] PLASMA_CHAR_SET = "1qaz2wsx".toCharArray();
    private final static int PLASMA_CODE_LENGTH = 9;
    private final static char[] TORPEDOES_CHAR_SET = "12qwaszx90opklnm".toCharArray();
    private final static int TORPEDOES_CODE_LENGTH = 13;

    private final MqttClient client;
    private final MessageSerializer serializer;
    private String code1;
    private String codeA;
    private Instant start;

    public Session() {
        this.id = UUID.randomUUID().toString();
        players = new ArrayList<>();
        responses = new HashMap<>();
        weaponResponses = new HashMap<>();
        codeResponses = new HashMap<>();
        try {
            client = new MqttClient(Server.BROKER, id, Server.PERSISTENCE);
            serializer = new StringSerializer();
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            logger.info("Connecting...");
            client.connect(options);
            logger.info("Setitng callbacks...");
            client.setCallback(this);
            logger.info("Subscribing...");
            client.subscribe(Topics.SESSION.value(id));
            logger.info("Lobby is set up!");
        } catch (MqttException e) {
            throw new IllegalArgumentException("Cannot create the session server");
        }
    }

    public String getId() {
        return id;
    }

    public boolean isFull() {
        return full;
    }

    public boolean joinSession(String playerId, String reqId) {
        if (!full) {
            players.add(playerId);
            if (players.size() >= totalPlayers) {
                full = true;
                start();
            }
            SessionJoinResponse response = new SessionJoinResponse(reqId, id, true);
            try {
                client.publish(Topics.PLAYER.value(playerId), new MqttMessage(serializer.serializeResponse(response)));
            } catch (MqttException e) {
                e.printStackTrace();
            }
            logger.debug("Players: {} out of {}", players.size(), totalPlayers);
            return true;
        } else {
            SessionJoinResponse response = new SessionJoinResponse(reqId, id, false);
            try {
                client.publish(Topics.PLAYER.value(playerId), new MqttMessage(serializer.serializeResponse(response)));
            } catch (MqttException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public List<String> getPlayers() {
        return players;
    }

    private void start() {
        makeTeams();
        pickRoundMasters();
    }

    private void makeTeams() {
        for (int i = 0; i < players.size(); i++) {
            final Player player = new Player(players.get(i));
            if (i < teamSize) {
                team1.addPlayer(player);
            } else {
                teamA.addPlayer(player);
            }
        }
        teamA.complete();
        team1.complete();
        if (team1.size() != teamA.size()) {
            System.out.println("WTF");
        }
    }

    private void pickRoundMasters() {
        masterA = teamA.getRoundMaster();
        master1 = team1.getRoundMaster();
    }

    public String generateCode(Messages.Weapon weapon) {
        switch (weapon) {

            case BURRITO:
                return generateCode(BURRITO_CODE_LENGTH, BURRITO_CHAR_SET);
            case HIGH_ENERGY_LAZER:
                return generateCode(LASER_CODE_LENGTH, LASER_CHAR_SET);
            case PLAZMA_CANONS:
                return generateCode(PLASMA_CODE_LENGTH, PLASMA_CHAR_SET);
            case TORPEDOES:
                return generateCode(TORPEDOES_CODE_LENGTH, TORPEDOES_CHAR_SET);
            case UNRECOGNIZED:
                break;

        }
        return null;

    }



    private String generateCode(int length, char[] charSet) {
        final StringBuilder sb = new StringBuilder(length);
        final Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(charSet[rnd.nextInt(charSet.length)]);
        }
        return sb.toString();
    }

    private void generateCodes() {
        logger.debug("GEnerating codes");
        code1 = generateCode(weapon1);
        codeA = generateCode(weaponA);
        logger.debug("Codes : {} , {}", code1, codeA);

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        logger.debug("Request Received...");
        Request request = serializer.deserializeRequest(message.getPayload());
        logger.debug("Request Received: {} from {}",request.getId(), request.getRequester());
        if (request instanceof PlayerListRequest) {
            PlayerListRequest req = (PlayerListRequest) request;
            PlayerListResponse response = new PlayerListResponse(req.getId(), players);
            client.publish(Topics.PLAYER.value(request.getRequester()),
                    new MqttMessage(serializer.serializeResponse(response)));
        } else if (request instanceof RoleRequest) {
            RoleRequest req = (RoleRequest) request;
            boolean master = req.getRequester().equalsIgnoreCase(master1) || req.getRequester().equalsIgnoreCase(masterA);
            RoleResponse response = new RoleResponse(req.getId(), master);
            responses.put(Topics.PLAYER.value(request.getRequester()),
                    new MqttMessage(serializer.serializeResponse(response)));
            logger.debug("Role response is prepared...");
            if (responses.size() >= players.size()) {
                logger.debug("Sending roles and starting the round...");
                sendAllRequests();
            }
        } else if (request instanceof SelectWeaponRequest) {
            logger.debug("Select weapon request received...");

            SelectWeaponRequest req = (SelectWeaponRequest) request;
            SelectWeaponResponse response = new SelectWeaponResponse(req.getId());
            if (req.getRequester().equalsIgnoreCase(master1)) {
                weapon1 = req.getWeapon();
                logger.debug("Select weapon request received: TEAM 1: {}", weapon1.name());

            } else if (req.getRequester().equalsIgnoreCase(masterA)) {
                weaponA = req.getWeapon();
                logger.debug("Select weapon request received: TEAM A: {}", weaponA.name());

            }
            weaponResponses.put(Topics.PLAYER.value(request.getRequester()),
                    new MqttMessage(serializer.serializeResponse(response)));
            logger.debug("Creating response...");
            if (weapon1 != null && weaponA != null) {
                generateCodes();
                sendAllWeapons();
                weapon1 = null;
                weaponA = null;
            }
        } else if (request instanceof CodeTransmissionRequest) {
            logger.debug("Code transmission request received...");

            CodeTransmissionRequest req = (CodeTransmissionRequest) request;
            CodeTransmissionResponse response = null;
            if (req.getRequester().equalsIgnoreCase(master1)) {
                logger.debug("Code transmission request received: master 1 : {}", code1);
                response = new CodeTransmissionResponse(req.getId(), code1);
                master1CodeReq = true;
                logger.debug("active!");
            } else if (req.getRequester().equalsIgnoreCase(masterA)) {
                logger.debug("Code transmission request received: master A : {}", codeA);
                response = new CodeTransmissionResponse(req.getId(), codeA);
                masterACodeReq = true;
                logger.debug("active!");
            }
            logger.debug("adding to responses");

            if (response != null) {
                codeResponses.put(Topics.PLAYER.value(request.getRequester()),
                        new MqttMessage(serializer.serializeResponse(response)));
            }
            logger.debug("checking if reply");

            if (masterACodeReq && master1CodeReq) {
                logger.debug("Code transmission starts");
                master1CodeReq = false;
                masterACodeReq = false;
                sendCodes();
            }
            logger.debug("done");
        } else if (request instanceof ActivationRequest) {

            ActivationRequest req = (ActivationRequest) request;
            String goal = null;
            String requester = req.getRequester();
            Team enemy;
            Team team;
            if (teamA.contains(requester)) {
                goal = codeA;
                enemy = team1;
                team = teamA;
            } else if (team1.contains(requester)) {
                goal = code1;
                enemy = teamA;
                team = team1;

            } else {
                logger.warn("Who are you {} ?", requester);
                return;
            }
            int delta = (int) (Instant.now().getEpochSecond() - start.getEpochSecond());
            int quality = calculateQuality(goal, req.getCode());
            int damage = quality * 100000 / delta;
            logger.info("Damage {}", damage);
            if (enemy.dealDamage(damage)) {
                logger.info("KILL!");
                team.players.forEach(player -> player.sendWon(client));
                enemy.players.forEach(player -> player.sendLost(client));
            }
            ActivationResponse response = new ActivationResponse(req.getId(), quality, delta);
            client.publish(Topics.PLAYER.value(request.getRequester()),
                    new MqttMessage(serializer.serializeResponse(response)));
        }
    }

    private int calculateQuality(String goal, String str2) {
        logger.info("Comparing {} vs {}", goal, str2);
        int goalWeight = stringWeight(goal);
        int diff = goalWeight - stringWeight(str2);
        if (diff < 0) {
            diff = -diff;
        }
        if (diff == 0) {
            diff++;
        }
        int quality = goalWeight / diff / goal.length();
        logger.debug("Quality= {}", quality);
        return quality;
    }

    private int stringWeight(String str) {
        char[] arr = str.toCharArray();
        int sum = 0;
        for (char c : arr) {
            sum += c - 48;
        }
        return sum;
    }

    private void sendAllRequests() {
        responses.forEach((key, value) -> {
            try {
                client.publish(key, value);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
        responses.clear();
    }


    private void sendAllWeapons() {
        logger.debug("Sending all weapons");
        weaponResponses.forEach((key, value) -> {
            try {
                client.publish(key, value);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
        weaponResponses.clear();
    }

    private void sendCodes() {
        start = Instant.now();
        codeResponses.forEach((topic, message) -> {
            try {
                client.publish(topic, message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
