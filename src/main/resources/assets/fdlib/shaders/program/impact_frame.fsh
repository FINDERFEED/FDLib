#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 ScreenSize;
uniform float treshhold;
uniform float treshholdLerp;
uniform float invert;
uniform float maxEstimatedGrayscale;

in vec2 texCoord;

out vec4 fragColor;

const vec2 points[9] = vec2[9](

    vec2(0.25,0.25),
    vec2(0.5,0.25),
    vec2(0.75,0.25),

    vec2(0.25,0.5),
    vec2(0.5,0.5),
    vec2(0.75,0.5),

    vec2(0.25,0.75),
    vec2(0.5,0.75),
    vec2(0.75,0.75)

);

float estimateGrayscale(){

    float gray = 0;

    for (int i = 0; i < 9; i++){

        vec2 samplePoint = points[i];
        vec4 pixel = texture(DiffuseSampler, samplePoint);
        float g = max(pixel.r, max(pixel.g, pixel.b));
        gray = max(gray, g);

    }

    return gray;
}

void main(){

    float estimatedGray = estimateGrayscale();

    vec4 color = texture(DiffuseSampler, texCoord);

    float gray = max(max(color.x,color.y),color.z);

    gray /= estimatedGray;

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