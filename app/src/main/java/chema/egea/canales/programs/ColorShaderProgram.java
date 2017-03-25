package chema.egea.canales.programs;

import android.content.Context;

import com.opengl10.android.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by chema on 10/01/2016.
 */

//TODOS LOS PROGRAMAS QUE CREEMOS HACEMOS QUE HEREDEN DE ShaderProgram
public class ColorShaderProgram extends ShaderProgram
{
    // Uniform locations
    private final int uMatrixLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aColorLocation;

    // CONSTRUCTOR DEL SHADER - LLAMAMOS A SUPER PARA CREAR EL PROGRAMA Y RECIBIMOS LOS UNIFORM Y ATTRIBUTES DEL PROGRAMA
    public ColorShaderProgram(Context context)
    {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
    }

    // METODO PARA ESTABLECER LAS UNIFORM DEL PROGRAMA DE OPENGL
    public void setUniforms(float[] matrix, float r, float g, float b)
    {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    // GETTERS PARA LOS ATTRIBUTES
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }
}
