package com.sergii.fgjx.sb.api;

public enum Topics {

    LOBBY("sb/lobby"),
    SESSION("sb/session/"),
    PLAYER("sb/player/"),
    PLAYER_WON("sb/player/won/"),
    PLAYER_LOST("sb/player/lost/");

    String topic;
    Topics(String topic) {
        this.topic = topic;
    }

    public String value() {
        return topic;
    }

    public String value(String id) {
        return topic + id;
    }
}
