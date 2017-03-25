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
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by chema on 11/01/2016.
 */
//TODOS LOS PROGRAMAS QUE CREEMOS HACEMOS QUE HEREDEN DE ShaderProgram
public class TanqueShaderProgram extends ShaderProgram
{
    // Nombre de los uniform
    private static final String U_MVPMATRIX 		= "u_MVPMatrix";
    private static final String U_MVMATRIX 			= "u_MVMatrix";
    private static final String U_COLOR 			= "u_Color";
    private static final String U_TEXTURE 			= "u_TextureUnit";

    // Nombre de los attribute
    private static final String A_POSITION = "a_Position";
    private static final String A_NORMAL   = "a_Normal";
    private static final String A_UV       = "a_UV";

    // Handlers para los shaders
    private int uMVPMatrixLocation;
    private int uMVMatrixLocation;
    private int uColorLocation;
    private int uTextureUnitLocation;
    private int aPositionLocation;
    private int aNormalLocation;
    private int aUVLocation;


    // CONSTRUCTOR DEL SHADER - LLAMAMOS A SUPER PARA CREAR EL PROGRAMA Y RECIBIMOS LOS UNIFORM Y ATTRIBUTES DEL PROGRAMA
    public TanqueShaderProgram(Context context)
    {
        super(context, R.raw.specular_vertex_shader2, R.raw.specular_fragment_shader2);

        // Retrieve uniform locations for the shader program.
        uMVPMatrixLocation = glGetUniformLocation(program, U_MVPMATRIX);
        uMVMatrixLocation = glGetUniformLocation(program, U_MVMATRIX);
        uColorLocation = glGetUniformLocation(program, U_COLOR);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE);

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION); //PEDIMOS A OPENGL LOS DATOS DE LA POSICION DEL ATRIBUTO
        aNormalLocation = glGetAttribLocation(program, A_NORMAL);
        aUVLocation = glGetAttribLocation(program, A_UV);
    }

    // METODO PARA ESTABLECER LAS UNIFORM DEL PROGRAMA DE OPENGL
    public void setUniforms(float[] matrixMVP, float[] matrixModel,int textureId)
    {

        // Envia la matriz de proyeccion multiplicada por modelMatrix al shader
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, matrixMVP, 0);
        // Envia la matriz modelMatrix al shader
        glUniformMatrix4fv(uMVMatrixLocation, 1, false, matrixModel, 0);

        // Actualiza el color del shader
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);


        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0);

        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation, 0);
    }

    //GETTERS DE LOS ATRIBUTOS DE POSICION DE VERTICES, NORMALES, Y UVs
    public int getPositionAttributeLocation()
    {
        return aPositionLocation;
    }
    public int getNormalTextureCoordinatesAttributeLocation()
    {
        return aNormalLocation;
    }
    public int getUVTextureCoordinatesAttributeLocation()
    {
        return aUVLocation;
    }
}