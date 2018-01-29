package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.api.messages.responses.CodeTransmissionResponse;
import com.sergii.fgjx.sb.api.messages.responses.Response;
import com.sergii.fgjx.sb.client.Client;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class ButtonLessButton extends SimpleMenuButton {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int MAX_TIMEOUT = 15;
    protected final Client client;

    public ButtonLessButton(int key, int action, World world, GameState target) {
        super(key, action, world, target);
        this.client = world.getCommunicationClient();
    }

    @Override
    public void invoke() {
        //nop
    }

    void trigger() {

    }
}
