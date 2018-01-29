package com.sergii.fgjx.sb.client.rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Texture {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final int id;
    private final int width;
    private final int height;

    public Texture(String fileName){
        logger.trace("Reading a texture file on {}", fileName);

        InputStream resource = getClass().getResourceAsStream("/textures/" + fileName);
        try {
            BufferedImage image = ImageIO.read(resource);
            width = image.getWidth();
            height = image.getHeight();
            int pixelColoursSize = width * height * 4;

            int[] rawPixels = image.getRGB(0, 0, width, height,null, 0, width);

            ByteBuffer pixels = BufferUtils.createByteBuffer(pixelColoursSize);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int pixel = rawPixels[i * width + j];
                    pixels.put((byte) ((pixel >> 16) & 0xFF));  //RED
                    pixels.put((byte) ((pixel >> 8) & 0xFF));   //GREEN
                    pixels.put((byte) (pixel & 0xFF));          //BLUE
                    pixels.put((byte) ((pixel >> 24) & 0xFF));  //ALPHA
                }
            }
            pixels.flip();

            id = GL11.glGenTextures();
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        } catch (IOException e) {
            throw new RuntimeException("Failed reading a texture file on " + fileName, e);
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void bind(int sampler){
        if(sampler >=0 && sampler <=31) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, id);
        }else{
            throw new IllegalArgumentException("The sampler value should be in range : [0,31]");
        }
    }


}