package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.client.io.KeyCallback;

public class SimpleMenuButton extends KeyCallback {

    protected final World world;
    protected final GameState target;

    public SimpleMenuButton(int key, int action, World world, GameState target) {
        super(key, action);
        this.world = world;
        this.target = target;
    }

    @Override
    public void invoke() {
        world.progress(target);
    }
}
