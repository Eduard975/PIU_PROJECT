#version 330 core

layout(location = 0) out vec4 color;

in DATA
{
    vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform bool isUsable;

void main()
{
    vec4 texColor = texture(tex, fs_in.tc);

    if (isUsable) {
        color = texColor;
    } else {
        float grey = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
        color = vec4(vec3(grey), texColor.a);
    }
}
