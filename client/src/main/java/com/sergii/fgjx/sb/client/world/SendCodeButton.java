package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.client.Client;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendCodeButton extends SimpleMenuButton {
    private final Client client;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SendCodeButton(int key, int action, World world, GameState target) {
        super(key, action, world, target);
        this.client = world.getCommunicationClient();
    }

    @Override
    public void invoke() {
        logger.info("Pressed... ENTER");
        try {
            client.sendAndForgetCode(world.getActivationCode());
            world.progress(GameState.SELECT_WEAPON);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
