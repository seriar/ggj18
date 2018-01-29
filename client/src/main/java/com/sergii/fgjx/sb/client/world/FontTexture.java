package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.client.rendering.Texture;

public class FontTexture {

    private final float characterWidth;
    private final float characterHeight;
    private final int textureCols;
    private final int textureRows;
    private final Texture texture;

    public FontTexture(float characterWidth, float characterHeight, int textureCols, int textureRows, Texture texture) {
        this.characterWidth = characterWidth;
        this.characterHeight = characterHeight;
        this.textureCols = textureCols;
        this.textureRows = textureRows;
        this.texture = texture;
    }

    public float getCharacterWidth() {
        return characterWidth;
    }

    public float getCharacterHeight() {
        return characterHeight;
    }

    public int getTextureCols() {
        return textureCols;
    }

    public int getTextureRows() {
        return textureRows;
    }

    public Texture getTexture() {
        return texture;
    }
}
