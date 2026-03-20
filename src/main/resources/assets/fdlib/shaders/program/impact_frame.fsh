#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;
uniform float treshhold;
uniform float treshholdLerp;
uniform float invert;

in vec2 texCoord;

out vec4 fragColor;

float maxGray(vec4 c) {
    return max(max(c.r, c.g), c.b);
}

float estimateSceneGray() {
    vec2 s = vec2(OutSize.x, OutSize.y);
    vec2 p1 = vec2(s.x * 0.25, s.y * 0.25);
    vec2 p2 = vec2(s.x * 0.50, s.y * 0.25);
    vec2 p3 = vec2(s.x * 0.75, s.y * 0.25);
    vec2 p4 = vec2(s.x * 0.25, s.y * 0.50);
    vec2 p5 = vec2(s.x * 0.50, s.y * 0.50);
    vec2 p6 = vec2(s.x * 0.75, s.y * 0.50);
    vec2 p7 = vec2(s.x * 0.25, s.y * 0.75);
    vec2 p8 = vec2(s.x * 0.50, s.y * 0.75);
    vec2 p9 = vec2(s.x * 0.75, s.y * 0.75);

    float m = 0.0;
    m = max(m, maxGray(texture(DiffuseSampler, p1 / s)));
    m = max(m, maxGray(texture(DiffuseSampler, p2 / s)));
    m = max(m, maxGray(texture(DiffuseSampler, p3 / s)));
    m = max(m, maxGray(texture(DiffuseSampler, p4 / s)));
    m = max(m, maxGray(texture(DiffuseSampler, p5 / s)));
    m = max(m, maxGray(texture(DiffuseSampler, p6 / s)));
    m = max(m, maxGray(texture(DiffuseSampler, p7 / s)));
    m = max(m, maxGray(texture(DiffuseSampler, p8 / s)));
    m = max(m, maxGray(texture(DiffuseSampler, p9 / s)));
    return m;
}

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);

    float gray = maxGray(color);
    float maxEstimatedGrayscale = estimateSceneGray();

    gray /= max(maxEstimatedGrayscale, 0.001);
    gray = clamp(gray, 0.0, 1.0);

    float edge;

    if (treshholdLerp != 0) {
        edge = smoothstep(treshhold - treshholdLerp, treshhold + treshholdLerp, gray);
    } else {
        edge = step(treshhold, gray);
    }

    gray = edge;

    if (invert > 0) {
        gray = 1 - gray;
    }

    fragColor = vec4(gray, gray, gray, color.a);
}