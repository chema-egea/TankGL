package chema.egea.canales.objects;

/**
 * Created by chema on 10/01/2016.
 */

import android.content.Context;

import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.translateM;

public class Suelo extends BaseObject
{
    public Suelo(Context context, int recurso3D)
    {
        super(context,recurso3D);
    }
    /*
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // Order of coordinates: X, Y, S, T

            // Triangle Fan - centro
               0f,    0f, 0.5f, 0.5f,

            -20f, -20f,   0f, 0.9f,
             20f, -20f,   1f, 0.9f,

             20f,  20f,   1f, 0.1f,
            -20f,  20f,   0f, 0.1f,
            //se cierra coincidiendo con el primer punto que no era el centro
            -20f, -20f,   0f, 0.9f };

    private final VertexArray vertexArray;
    public final float[] modelMatrix = new float[16];
    public final float[] modelViewProjectionMatrix = new float[16];

    // INICIALIZAMOS LA CLASE CREANDO EL/LOS VERTEX ARRAY (LE PASAMOS UN ARRAY DE FLOATS Y NOS CREA EL FLOATBUFFER QUE PODEMOS MANEJAR PARA PINTAR)
    public Suelo()
    {
        vertexArray = new VertexArray(VERTEX_DATA);
        calculateModelMatrix();
    }

    // METODO PARA ENLAZAR EL VERTEX ARRAY CON EL SHADER PROGRAM
    public void bindData(TextureShaderProgram textureProgram)
    {
        vertexArray.setVertexAttribPointer(0, textureProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, textureProgram.getTextureCoordinatesAttributeLocation(), TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
    }

    //ACTUALIZAMOS LA MVP
    public void updateMVP(float[] viewProjectionMatrix) {
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    private void calculateModelMatrix()
    {
        float[] auxRotationMatrix=new float[16];
        setIdentityM(auxRotationMatrix, 0);

        rotateM(auxRotationMatrix, 0, 0, 0f, 1f, 0f);
        rotateM(auxRotationMatrix, 0, -90, 1f, 0f, 0f);

        float[] auxTranslationMatrix=new float[16];
        setIdentityM(auxTranslationMatrix, 0);

        translateM(auxTranslationMatrix, 0, 0, 0, 0);

        multiplyMM(modelMatrix, 0, auxTranslationMatrix, 0, auxRotationMatrix, 0);

    }

    //METODO PARA DIBUJAR EL SUELO
    public void draw() {
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }


    public void positionSueloInScene(float[] viewProjectionMatrix)
    {
        // The table is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }*/

}
