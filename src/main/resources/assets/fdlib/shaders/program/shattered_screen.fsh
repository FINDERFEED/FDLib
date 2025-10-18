#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D sampler0;
uniform vec2 ScreenSize;
uniform float maxOffset;

in vec2 texCoord;

out vec4 fragColor;

mat2 rotationMatrix(float angle){

    return mat2(
        cos(angle), -sin(angle),
        sin(angle), cos(angle)
    );

}

void main(){

    vec2 samplerTexCoord = vec2(texCoord.x, 1 - texCoord.y);

    vec4 samplerColor = texture(sampler0, samplerTexCoord);


    vec4 result = samplerColor;

    if (samplerColor != vec4(0)){

        if (samplerColor.g == 0){

            float rotationAngle = samplerColor.r * 3.14 * 2;

            mat2 rotation = rotationMatrix(rotationAngle);



            vec2 offset = rotation * vec2(0,0.1);

            result = texture(DiffuseSampler, texCoord - offset);

        }

    }else{
        result = texture(DiffuseSampler, texCoord);
    }




    fragColor = result;

}