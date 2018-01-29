package com.sergii.fgjx.sb.server;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    public static String BROKER = "tcp://localhost:1883";
    public static final MemoryPersistence PERSISTENCE = new MemoryPersistence();

    private static final int QOS = 2;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String [] args) {
        if (args.length == 1) {
            System.out.println("Trying broker:" + args[0]);
            BROKER = args[0];
        }
        Server server = new Server();
        server.start();
    }

    private void start() {
        logger.info("Starting SB server");
        Lobby lobby = new Lobby();
        logger.info("Are we here yet?");
    }
}
