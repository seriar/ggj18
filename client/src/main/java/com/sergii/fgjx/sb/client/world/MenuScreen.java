package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.client.rendering.Shader;
import com.sergii.fgjx.sb.client.rendering.Texture;

public class MenuScreen implements Screen {

    public static final int FONT_SIZE = 30;
    public static final int DISPLAY_COLS = 16;
    public static final int MAX_DISPLAY_ROWS = 16;

    protected final TextItem menu;
    private final int rows;

    public MenuScreen(String text) {
        FontTexture fontTexture = new FontTexture(1f / 16f,1f / 16f,16,16, new Texture("font.png"));
        rows = text.length() / DISPLAY_COLS;
        if (rows > MAX_DISPLAY_ROWS) {
            throw new IllegalArgumentException("Menu does not fit: " + text);
        }
        menu = new TextItem(DISPLAY_COLS, rows, 1f, 1f, fontTexture);
        menu.setText(text);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(Shader shader) {
            menu.render(shader, -FONT_SIZE*(DISPLAY_COLS / 2), FONT_SIZE*rows/2, FONT_SIZE);
    }
}
