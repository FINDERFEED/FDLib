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


    vec2 offsetFromCenter = texCoord - vec2(0.5,0.5);

    float strength = clamp(length(offsetFromCenter), 0, 1);
    strength = smoothstep(0,1,strength);

//    vec2 baseOffset = normalize(-offsetFromCenter) * strength * chromaticAbberationStrength;

    vec2 baseOffset = vec2(0,strength * chromaticAbberationStrength);

    mat2 rotRed = rotationMatrix(PI / 3);
    mat2 rotBlue = rotationMatrix(-PI / 3);

    vec2 offsetRed = rotRed * baseOffset;
    vec2 offsetBlue = rotBlue * baseOffset;

    vec4 main = texture(DiffuseSampler, texCoord);
    float red = texture(DiffuseSampler, texCoord + offsetRed).r;
    float green = texture(DiffuseSampler, texCoord).g;
    float blue = texture(DiffuseSampler, texCoord + offsetBlue).b;

    vec4 result = vec4(red,green,blue,main.a);

    fragColor = result;

}