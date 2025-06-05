#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
varying vec4 v_color;

uniform sampler2D u_texture;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoords);
    float gray = dot(texColor.rgb, vec3(0.3, 0.59, 0.11));
    gl_FragColor = vec4(gray, gray, gray, texColor.a) * v_color;
}
