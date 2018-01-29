package com.sergii.fgjx.sb.server;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class Callback implements MqttCallback {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void connectionLost(Throwable throwable) {
        logger.error("Connection lost");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        logger.info("Message arrived: {}:{}", Instant.now().getEpochSecond(), Instant.now().getNano());
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        logger.info("Delivery complete {}:{}", Instant.now().getEpochSecond(), Instant.now().getNano());
    }
}
