precision mediump float;

uniform sampler2D u_Texture;  
  
varying vec2 v_TexCoordinate;

void main()
{
   vec2 st=v_TexCoordinate;	
   st.y+=sin(st.x*75)*0.02f;
   gl_FragColor = texture2D(u_Texture, st);
}