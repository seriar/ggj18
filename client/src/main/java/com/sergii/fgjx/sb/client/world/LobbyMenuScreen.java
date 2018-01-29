package com.sergii.fgjx.sb.client.world;

import org.apache.commons.lang3.StringUtils;

public class LobbyMenuScreen extends ModifiableMenuScreen {

    public LobbyMenuScreen(String text) {
        super(text);
    }

    public void updateScreen(int size) {
        super.updateScreen(processParam(size));
    }

    private String processParam(int size) {
        if (size > 99999) {
            return "100k+";
        } else {
            return StringUtils.leftPad(String.valueOf(size), 5);
        }
    }
}
