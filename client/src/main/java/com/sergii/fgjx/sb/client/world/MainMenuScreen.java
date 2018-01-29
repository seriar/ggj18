package com.sergii.fgjx.sb.client.world;

public class MainMenuScreen extends MenuScreen {



    private final World world;

    public MainMenuScreen(String text, World world) {
        super(text);
        this.world = world;
    }

    public void close() {
        world.setClosing(true);
    }


}
