#version 120

uniform float time;
uniform vec2 resolution;

void main(void) {
    vec2 pos = (gl_FragCoord.xy / resolution.xy);
    gl_FragColor = vec4(vec3(pos.x, pos.y, 1), 1.0);
}