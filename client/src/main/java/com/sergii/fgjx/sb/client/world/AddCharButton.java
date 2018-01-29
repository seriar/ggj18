package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.client.io.AnyKeyCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddCharButton extends AnyKeyCallback {
    private World world;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AddCharButton(World world) {
        this.world = world;
    }

    @Override
    public void invoke() {
        logger.info("Pressed... {}", pressedKey);
        world.addChar(pressedKey);
    }
}
