package chema.egea.canales.util;

import static android.opengl.GLES20.*;
import android.util.Log;

/*
	ShaderHelper class that is going to create a
	new OpenGL shader object, compile our shader code, and return the shader
	object for that shader code. Once we have this boilerplate code in place, we’ll
	be able to reuse it in our future projects
 */

public class ShaderHelper
{
	private static final String TAG = "ShaderHelper";

	/*
		The compileVertexShader(shaderCode) method is a helper method that calls
		compileShader() with shader type GL_VERTEX_SHADER.
	 */
	public static int compileVertexShader(String shaderCode)
	{
		return compileShader(GL_VERTEX_SHADER, shaderCode);
	}
	/*
		The compileVertexShader(shaderCode) method is a helper method that calls
		compileShader() with shader type GL_FRAGMENT_SHADER.
	 */
	public static int compileFragmentShader(String shaderCode)
	{
		return compileShader(GL_FRAGMENT_SHADER, shaderCode);
	}

	/*
		The compileShader(shaderCode) method takes in source code for a shader and
		the shader’s type. The type can be GL_VERTEX_SHADER for a vertex shader, or
		GL_FRAGMENT_SHADER for a fragment shader. If OpenGL was able to successfully
		compile the shader, then this method will return the shader object
		ID to the calling code. Otherwise it will return zero.
	 */

	private static int compileShader(int type, String shaderCode)
	{

		/*

		PASOS INICIALES

		1. We first create an object using a call such as glCreateShader(). This call will return an integer.

		2. This integer is the reference to our OpenGL object. Whenever we want to refer to this object in the future, we’ll pass the same integer back to OpenGL.

		3. A return value of 0 indicates that the object creation failed and is analogous to a return value of null in Java code.

		 */

		//PASO 1 Y 2. CREAMOS UN NUEVO OBJETO SHADER CON glCreateShader(type), INDICANDO EL TIPO DE SHADER COMO PARAMETRO (VERTEX O FRAGMENT)
		final int shaderObjectId = glCreateShader(type);

		// PASO 3. SI EL ID DEVUELTO AL CREAR EL SHADER ES 0, ENTONCES LA CREACION HA FALLADO :(
		if (shaderObjectId == 0)
		{
			if (LoggerConfig.ON)
			{
				Log.w(TAG, "No se pudeo crear un nuevo shader.");
			}
			return 0;
		}

		/*
		Once we have a valid shader object, we call glShaderSource(shaderObjectId, shaderCode)
		to upload the source code. This call tells OpenGL to read in the source code
		defined in the String shaderCode and associate it with the shader object referred to
		by shaderObjectId.
		 */
		glShaderSource(shaderObjectId, shaderCode);
		// LUEGO DE ASIGNAR EL CODIGO DEL SHADER QUE HEMOS DEFINIDO A NUESTRO OBJETO SHADER CREADO, LLAMAMOS A glCompileShader PARA COMPILAR
		glCompileShader(shaderObjectId);

		//AHORA AÑADIMOS CODIGO PARA DETERMINAR SI OPENGL PUDO COMPILAR CON EXITO O NO EL SHADER

		/*
		To check whether the compile failed or succeeded, we first create a new int
		array with a length of 1 and call it compileStatus. We then call glGetShaderiv(shaderObjectId,
		GLES20.GL_COMPILE_STATUS, compileStatus, 0). This tells OpenGL to read the
		compile status associated with shaderObjectId and write it to the 0th element of
		compileStatus.
		 */

		final int[] compileStatus = new int[1]; //ESTO ES UN PATRON HABITUAL, PARA LEER O ALMACENAR DATOS SE PASAN ARRAYS DE 1 A OPENGL EN LUGAR DE SIMPLES VARIABLES
		glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0); //CON ESTO OPENGL LEE EL ESTADO DE COMPILACION ASOCIADO CON shaderObjectId Y LO ESCRIBE EN NUESTRO ARRAY compileStatus
		
		if (LoggerConfig.ON)
		{
			// Print the shader info log to the Android log output.
			Log.v(TAG, "Resultado de la compilacion del codigo:" + "\n" + shaderCode + "\n:" + glGetShaderInfoLog(shaderObjectId));
		}
		//CON LOS DATOS DE COMPILACION, HACEMOS UNA COSA U OTRA (SI SALE 0 ES ERROR)
		if (compileStatus[0] == 0)
		{
			// Si falla borramos el objeto shader.
			glDeleteShader(shaderObjectId);
			if (LoggerConfig.ON)
			{
				Log.w(TAG, "La compilacion del shader ha fallado.");
			}
			return 0;
		}

		return shaderObjectId;
	}

	/*
		El metodo linkProgram se encarga de crear el programa opengl que contiene el vertex shader y el fragment shader que le digamos
	 */
	public static int linkProgram(int vertexShaderId, int fragmentShaderId)
	{
		//NOTA: EL PROCESO ES PARECIDO A compileShader

		//Creamos un objeto programa y nos devuelve una id que nos guardamos(recordemos que esa id es un codigo que nos devuelve opengl indicando que internamente ha creado un objeto que se identifica con eso)
		final int programObjectId = glCreateProgram();

		//Si el objeto programa que hemos intentado crear devuelve id de 0, entonces es que hubo error
		if (programObjectId == 0)
		{
			if (LoggerConfig.ON)
			{
				Log.w(TAG, "No se puede crear un nuevo programa");
			}
			return 0;
		}

		// El siguiente paso es asignar los shaders que queremos unir al programa (OJO, ESTO LOS ASIGNA, PERO NO LOS UNE)
		// Using glAttachShader(), we attach both our vertex shader and our fragment shader to the program object.

		// Añadimos los shaders
		glAttachShader(programObjectId, vertexShaderId);
		glAttachShader(programObjectId, fragmentShaderId);

		// Finalmente, unimos los shaders de nuestro programa con glLinkProgram

		// Enlazamos los shaders
		glLinkProgram(programObjectId);

		//PARA TERMINAR, COMPROBAMOS SI LA UNION DE LOS SHADERS FUE CORRECTA O NO. ESTE PROCESO ES IGUAL QUE EN compile shader
		// Comprobamos el resultado
		final int[] linkStatus = new int[1]; //creamos nuestro array de 1 para asignar aqui el estado del resultado
		glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0); //le pedimos a opengl que nos diga el resultado de union del programa y que devuelva el valor a linkStatus
		// Añadimos la traza al log
		if (LoggerConfig.ON)
		{
			Log.v(TAG, "Resultado del enlace del programa:\n" + glGetProgramInfoLog(programObjectId));
		}
		// Comprobamos el resultados del enlace
		if (linkStatus[0] == 0) //si el resultado de link status es 0 es que ha habido un error
		{
			// Si falla borra el objeto programa.
			glDeleteProgram(programObjectId);
			if (LoggerConfig.ON)
			{
				Log.w(TAG, "Ha fallado el enlazado del programa.");
			}
			return 0;
		}
		// Union correcta, devolvemos el identificador
		return programObjectId;
	}

	// METODO PARA COMPROBAR QUE UN PROGRAMA SEA VALIDO PARA EL ESTADO ACTUAL DE OPENGL
	public static boolean validateProgram(int programObjectId)
	{
		// LLAMAMOS A glValidateProgram PARA VALIDAR EL PROGRAMA, LUEGO COMPROBAMOS ESTE ESTADO DE VALIDACION CON glGetProgramiv, usando el parametro GL_VALIDATE_STATUS
		glValidateProgram(programObjectId);
		
		final int[] validateStatus = new int[1];
		glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);

		// SI OPENGL TIENE ALGO INTERESANTE QUE DECIRNOS, LO HARA EN EL LOG DEL PROGRAMA
		Log.v(TAG, "Resultado de la validacion del programa: " + validateStatus[0] + "\nLog:" + glGetProgramInfoLog(programObjectId));
		return validateStatus[0] != 0;
	}

	// FUNCION HELPER QUE COMULE LOS SHADERS DEFINIDOS POR vertexShaderSource Y fragmentShaderSource Y HAGA LINK DE ELLOS COMO PROGRAMA
	public static int buildProgram(String vertexShaderSource, String fragmentShaderSource)
	{
		/*
			This helper function will compile the shaders defined by vertexShaderSource and
			fragmentShaderSource and link them together into a program. If logging is turned
			on, it will also validate the program.
		 */
		int program;
		// Compile the shaders.
		int vertexShader = compileVertexShader(vertexShaderSource);
		int fragmentShader = compileFragmentShader(fragmentShaderSource);
		// Link them into a shader program.
		program = linkProgram(vertexShader, fragmentShader);
		if (LoggerConfig.ON)
		{
			validateProgram(program);
		}

		return program;
	}
}
