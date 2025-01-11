#version 330 core

layout(location = 0) out vec4 color;

in DATA
{
    vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform bool isAlly = false;

void main()
{
    vec4 texColor = texture(tex, fs_in.tc);

    if (isAlly) {
        // Slightly reduce green and add blue for a turquoise effect
        texColor.g *= 0.8; // Reduce green slightly
        texColor.b += 0.5 * texColor.g; // Add blue proportional to green
    }

    color = texColor;


    if(color.w < 1.0){
        discard;
    }
}