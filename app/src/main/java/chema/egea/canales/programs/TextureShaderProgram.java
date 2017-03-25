package chema.egea.canales.programs;

import android.content.Context;

import com.opengl10.android.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by chema on 10/01/2016.
 */
//TODOS LOS PROGRAMAS QUE CREEMOS HACEMOS QUE HEREDEN DE ShaderProgram
public class TextureShaderProgram extends ShaderProgram
{
    // Uniform locations
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;

    // CONSTRUCTOR DEL SHADER - LLAMAMOS A SUPER PARA CREAR EL PROGRAMA Y RECIBIMOS LOS UNIFORM Y ATTRIBUTES DEL PROGRAMA
    public TextureShaderProgram(Context context)
    {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    // METODO PARA ESTABLECER LAS UNIFORM DEL PROGRAMA DE OPENGL
    public void setUniforms(float[] matrix, int textureId)
    {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0);

        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation, 0);
    }

    // GETTERS PARA LOS ATTRIBUTES
    public int getPositionAttributeLocation()
    {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation()
    {
        return aTextureCoordinatesLocation;
    }
}