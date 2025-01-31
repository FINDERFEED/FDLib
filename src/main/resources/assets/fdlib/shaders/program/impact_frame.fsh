#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 ScreenSize;
uniform float treshhold;
uniform float treshholdLerp;
uniform float invert;
uniform float maxEstimatedGrayscale;

in vec2 texCoord;

out vec4 fragColor;

void main(){


    vec4 color = texture(DiffuseSampler, texCoord);

    float gray = max(max(color.x,color.y),color.z);

    gray /= maxEstimatedGrayscale;

    if (gray > treshhold){
        gray = 1;
    }else{
        if (treshholdLerp != 0){
            float v = treshhold - gray;
            gray = smoothstep(0,1,1 - min(1,v / treshholdLerp));
        }else{
            gray = 0;
        }

    }

    if (invert > 0){
        gray = 1 - gray;
    }

    fragColor = vec4(gray,gray,gray,color.a);

}