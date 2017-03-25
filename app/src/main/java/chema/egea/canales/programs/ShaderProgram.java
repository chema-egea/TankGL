package chema.egea.canales.programs;

import static android.opengl.GLES20.glUseProgram;
import android.content.Context;

import chema.egea.canales.util.ShaderHelper;
import chema.egea.canales.util.TextResourceReader;

/**
 * Created by chema on 10/01/2016.
 */

/*
    We start out the class by defining some common constants. In the constructor,
    we call the helper function that we’ve just defined, and we use it to build an
    OpenGL shader program with the specified shaders. We close off the class
    with useProgram(), which will call glUseProgram() to tell OpenGL to use program for
    subsequent rendering.
 */
abstract class ShaderProgram
{
    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_COLOR = "u_Color";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    // Shader program
    protected final int program;

    // METODO QUE LLAMA A SHADER HELPER Y QUE NOS VA A LEER, ENLAZAR, Y COMPILAR UN PROGRAMA PASANDOLE EL VERTEX SHADER Y EL FRAGMENT SHADER EN CUESTION
    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId)
    {
        // Compile the shaders and link the program.
        program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId), TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
    }

    // METODO PARA INDICAR A OPENGL QUE USE EL PROGRAMA
    public void useProgram()
    {
        // Set the current OpenGL shader program to this program.
        glUseProgram(program);
    }
}