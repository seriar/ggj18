package com.sergii.fgjx.sb.client.rendering;

import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_TRUE;

public class Shader {

    public static final String SHADERS_LOCATION = "shaders";
    public static final String FS_GLSL_POSTFIX = "_fs.glsl";
    public static final String VS_GLSL_POSTFIX = "_vs.glsl";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final int program;
    private final int vertexShader;
    private final int fragmetShader;

    public Shader(String fileName){

        program = GL20.glCreateProgram();

        vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexShader, readFile(fileName + VS_GLSL_POSTFIX));
        GL20.glCompileShader(vertexShader);
        if( GL20.glGetShaderi(vertexShader, GL20.GL_COMPILE_STATUS) != GL_TRUE){
            throw new IllegalStateException("Failed loading vertex shader " + fileName);
        }

        fragmetShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmetShader, readFile(fileName + FS_GLSL_POSTFIX));
        GL20.glCompileShader(fragmetShader);
        if( GL20.glGetShaderi(fragmetShader, GL20.GL_COMPILE_STATUS) != GL_TRUE){
            throw new IllegalStateException("Failed loading fragment shader " + fileName);
        }

        GL20.glAttachShader(program, vertexShader);
        GL20.glAttachShader(program, fragmetShader);

        GL20.glBindAttribLocation(program, 0, "vertices"); // verices goes to shader_vs.glsl
        // 0 goes to model::render()
        GL20.glBindAttribLocation(program, 1, "textures");

        GL20.glLinkProgram(program);
        if(GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) != GL_TRUE){
            throw new RuntimeException("Failed linking program " + program + " for file " +fileName );
        }

        GL20.glValidateProgram(program);
        if(GL20.glGetProgrami(program, GL20.GL_VALIDATE_STATUS) != GL_TRUE){
            throw new RuntimeException("Failed validating program " + program + " for file " +fileName );
        }
        GL20.glDetachShader(program, vertexShader);
        GL20.glDetachShader(program, fragmetShader);
    }

    private String readFile(String fileName){
        String locationFormat = "/%s/%s";
        String location = String.format(locationFormat, SHADERS_LOCATION, fileName);
        logger.trace("Reading shader file {}", location);
        InputStream stream = this.getClass().getResourceAsStream(location);
        try {
            return IOUtils.toString(stream);
        } catch (IOException e) {
            throw new RuntimeException("Failed readning " + SHADERS_LOCATION + " file in " + location, e);
        }
    }

    public void setUniformValue(String name, int value){
        int location = GL20.glGetUniformLocation(program, name);
        if( location == -1 ){
            throw new RuntimeException("Was not able to locate the uniforma variable in shader.");
        }
        GL20.glUniform1i( location, value );
    }

    public void setUniform(String name, Matrix4f value){
        int location = GL20.glGetUniformLocation(program, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);

        if( location == -1 ){
            throw new RuntimeException("Was not able to locate the uniforma variable in shader.");
        }
        GL20.glUniformMatrix4fv( location,false , buffer );
    }

    public void bind(){
        GL20.glUseProgram(program);
    }

    public void unbind(){
        GL20.glUseProgram(0);

    }
}
