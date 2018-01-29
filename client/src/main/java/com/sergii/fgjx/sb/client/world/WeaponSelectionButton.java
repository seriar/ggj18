package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeaponSelectionButton extends SimpleMenuButton{

    public static final int MAX_TIMEOUT = 3;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final Client client;
    protected final int option;

    public WeaponSelectionButton(int key, int action, World world, GameState target, Client client, int option) {
        super(key, action, world, target);
        this.client = client;
        this.option = option;
    }

    @Override
    public void invoke() {

    }
}
