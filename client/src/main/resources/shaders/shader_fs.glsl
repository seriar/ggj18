#version 120

uniform sampler2D sampler;

varying vec2 v_tex_coords;

void main() {
    gl_FragColor = texture2D(sampler, v_tex_coords);
}
