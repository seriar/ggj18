package com.sergii.fgjx.sb.client.world;

import org.apache.commons.lang3.StringUtils;

public class SessionsMenuScreen extends ModifiableMenuScreen {

    public SessionsMenuScreen(String text) {
        super(text);
    }

    @Override
    public void updateScreen(String... params) {
        String[] processedParams = {
                "           ",
                "           ",
                "           ",
                "           "
        };
        for (int i = 0; i < params.length; i++) {
            processedParams[i] = (i + 1) + ". " + processParam(params[i]);
        }
        super.updateScreen(processedParams);
    }

    private String processParam(String param) {
        if (param.length() > 8) {
            return param.substring(0,8);
        } else {
            return StringUtils.leftPad(param, 8);
        }
    }
}
