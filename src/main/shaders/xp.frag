#version 330 core

layout(location = 0) out vec4 color;

uniform vec3 barColor;

void main()
{
    color = vec4(barColor, 1.0);
}
