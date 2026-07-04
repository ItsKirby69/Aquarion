
uniform vec4 u_target_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

void main(){
    vec4 orig = texture2D(u_texture, v_texCoords);
    vec4 color = vec4(0.0, 0.0, 0.0,0.0);
    color = orig;
    if(orig.a > 0.0 && orig.rgb == u_target_color.rgb){
    	color = vec4(0.0, 0.0, 0.0,0.0);
    }
    gl_FragColor = color;
}