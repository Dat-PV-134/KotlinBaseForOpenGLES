#version 320 es

precision mediump float;  // Define precision for float types

in vec3 ourColor;
out vec4 FragColor;

void main() {
    FragColor = vec4(ourColor.x, ourColor.y, ourColor.z, 1.0);
}
