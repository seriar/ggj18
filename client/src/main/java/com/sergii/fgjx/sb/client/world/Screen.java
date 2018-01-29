package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.client.rendering.Shader;

public interface Screen {
    /**
     * Should update the menu status
     * @param delta delta time in ms
     */
    void update(float delta);

    /**
     * Should render the screen
     * @param shader the shader object to be used in rendering
     */
    void render(Shader shader);

}
