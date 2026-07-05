varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_texture_mask;

void main(){


    vec4 mask = texture2D(u_texture_mask, v_texCoords);
    vec4 color = texture2D(u_texture, v_texCoords);

    if(mask.a < 0.99){
        color.a = 0.0;
    }
    gl_FragColor = color;
}