#version 330 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 texture;

uniform mat4 pr_matrix;
uniform vec4 tile_color;

out vec4 v_texCoord;
out vec4 v_color;

void main()
{
    gl_Position = pr_matrix * position;

    v_texCoord = texture;
    v_color = tile_color;
}