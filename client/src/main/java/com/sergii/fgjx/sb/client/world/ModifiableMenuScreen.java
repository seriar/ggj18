package com.sergii.fgjx.sb.client.world;

import java.util.Formatter;
import java.util.Locale;

public class ModifiableMenuScreen extends MenuScreen {

    private final String menuTextTemplate;
    private final Formatter formatter;
    private final StringBuilder stringBuilder;

    public ModifiableMenuScreen(String text) {
        super(text);
        menuTextTemplate = text;
        stringBuilder = new StringBuilder();
        this.formatter = new Formatter(stringBuilder, Locale.US);
    }

    public void updateScreen(String...params) {
        stringBuilder.setLength(0);
        final String text = formatter.format(menuTextTemplate, params).toString();
        menu.setText(text);
    }

}
