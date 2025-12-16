#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 ScreenSize;
uniform float chromaticAbberationStrength;

in vec2 texCoord;

out vec4 fragColor;

#define PI 3.14

mat2 rotationMatrix(float angle){

    return mat2(
        cos(angle), -sin(angle),
        sin(angle), cos(angle)
    );

}

void main(){

    vec4 result = texture(DiffuseSampler, texCoord);

    vec2 offsetFromCenter = texCoord - vec2(0.5,0.5);

    float strength = clamp(length(offsetFromCenter), 0, 1);
    strength = smoothstep(0,1,strength);

    vec2 baseOffset = vec2(0, strength);

    mat2 rotRed = rotationMatrix(PI / 4);
    mat2 rotBlue = rotationMatrix(-PI / 4);
    mat2 rotGreen = rotationMatrix(PI);

    vec2 offsetRed = rotRed * baseOffset;
    vec2 offsetBlue = rotBlue * baseOffset;
    vec2 offsetGreen = rotGreen * baseOffset;

    float red = texture(DiffuseSampler, texCoord + offsetRed).r;
    float blue = texture(DiffuseSampler, texCoord + offsetBlue).g;
    float green = texture(DiffuseSampler, texCoord + offsetGreen).b;

    result += vec4(red,blue,green,0);

    fragColor = result;

}