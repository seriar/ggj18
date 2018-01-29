package com.sergii.fgjx.sb.api.messages.responses;

import java.util.List;
import java.util.Set;

public class PlayerListResponse extends Response {

    private final List<String> players;

    public PlayerListResponse(String id, List<String> players) {
        super(id);
        this.players = players;
    }

    public List<String> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "PlayerListResponse{" +
                "players=" + players +
                ", id='" + id + '\'' +
                '}';
    }
}
