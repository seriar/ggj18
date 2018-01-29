package com.sergii.fgjx.sb.server;

import com.sergii.fgjx.sb.api.Topics;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Player {
    public static final int MAX_HEALTH = 100;

    private String id;
    private int health;
    private boolean alive = true;

    public Player(String id) {
        this.id = id;
        health = MAX_HEALTH;
    }

    public boolean hit(int damage) {
        health -= damage;
        return (health < 0);
    }

    public boolean isAlive() {
        return alive;
    }

    public String getId() {
        return id;
    }

    public int getHealth() {
        return health;
    }

    /**
     * Sends information about round results: personal damage/team damage/enemy evasion
     *                                        personal evasion/personal damage/update health bars
     */
    public void updateClient(int personalEffort, int teamEffort, int enemyEvasion, int dealtDamage,
                             int personalEvasion, int personalDamage, int health) {

    }

    /**
     * one player of the team is asked for difficulty options
     */
    public void requestDifficulty(int low, int mid, int high) {

    }

    /**
     * Showing code to repeat
     * @param code
     */
    public void showCode(String code) {

    }

    /**
     * compares the codes and timestamps
     */
    public void handleAttackCode(String code, int delta) {

    }

    /**
     * compares the codes and timestamps
     */
    public void handleDefenceCode(String code, int delta) {

    }

    public void sendWon(MqttClient client) {
        try {
            client.publish(Topics.PLAYER_WON.value(id), new MqttMessage(new byte[]{0x01}));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void sendLost(MqttClient client) {
        try {
            client.publish(Topics.PLAYER_LOST.value(id), new MqttMessage(new byte[]{0x00}));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
