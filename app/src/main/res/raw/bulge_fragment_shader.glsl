#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_TexCoordinate;
uniform sampler2D u_Texture;

const float aspectRatio = 1.5;
const vec2 center = vec2(0.5, 0.5);
const float radius = 0.5;
//const float scale = 1.;

//uniform float aspectRatio;
//uniform vec2 center;
//uniform float radius;
uniform float scale;

void main()
{
    vec2 textureCoordinateToUse = vec2(v_TexCoordinate.x, (v_TexCoordinate.y * aspectRatio + center.y - center.y * aspectRatio));
    float dist = distance(center, textureCoordinateToUse);
    textureCoordinateToUse = v_TexCoordinate;
    if (dist < radius)
    {
        textureCoordinateToUse -= center;
        float percent = 1.0 - ((radius - dist) / radius) * scale;
        percent = percent * percent;
        textureCoordinateToUse = textureCoordinateToUse * percent;
        textureCoordinateToUse += center;
    }
    gl_FragColor = texture2D(u_Texture, textureCoordinateToUse);
}