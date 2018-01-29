package com.sergii.fgjx.sb.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

public class Team {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    List<Player> players;
    List<String> ids;

    public Team() {
        this.players = new ArrayList<>();
        this.ids = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
        ids.add(player.getId());
    }

    public int size() {
        return players.size();
    }

    public void complete() {

    }
    public boolean contains(String id) {
        return ids.contains(id);
    }

    public String getRoundMaster() {
        int master = new Random().nextInt(players.size());
        return players.get(master).getId();
    }

    public boolean dealDamage(int damage) {
        boolean alive = players.get(0).hit(damage);
        logger.info("You dead yet?: {}", alive);
        logger.info("HP left: {}", players.get(0).getHealth());
        return alive;
    }
}
