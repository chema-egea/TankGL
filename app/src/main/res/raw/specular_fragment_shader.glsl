precision mediump float;			// Precision media

varying vec4 v_Color;				// in: color recibido desde el vertex shader

void main()
{
	//un fragment shader debe siempre escribir algo en su gl_FragColor
	gl_FragColor = v_Color;
}