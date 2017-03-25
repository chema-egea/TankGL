package chema.egea.canales.objects;

/**
 * Created by chema on 11/01/2016.
 */

import chema.egea.canales.data.VertexArray;
import chema.egea.canales.programs.ColorShaderProgram;

import chema.egea.canales.Constants;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;

public class Triangulos
{
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 4;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private final VertexArray vertexArray;
    public final float[] modelMatrix = new float[16];
    public final float[] modelViewProjectionMatrix = new float[16];

    private static final float[] VERTEX_DATA = {
            // Order of coordinates: X,Y, R,G,B,A

            // Triangle 1 - ARRIBA
            -0.1f,   0.8f,      0f, 0f, 1f, 0.5f,
            0.1f,    0.8f,      0f, 0f, 1f,0.5f,
            0f,     1f,      0f, 0f, 1f,0.5f,
            // Triangle 2 - ABAJO
            -0.1f,  -0.8f,      0f, 0f, 1f,0.5f,
            0.1f,   -0.8f,      0f, 0f, 1f,0.5f,
            0f,    -1f,      0f, 0f, 1f,0.5f,
            // Triangle 3 - DERECHA
            0.8f,   -0.1f,      0f, 0f, 1f,0.5f,
            0.8f,    0.1f,      0f, 0f, 1f,0.5f,
            1f,     0f,      0f, 0f, 1f,0.5f,
            // Triangle 4 - IZQUIERDA
            -0.8f,  -0.1f,      0f, 0f, 1f,0.5f,
            -0.8f,   0.1f,      0f, 0f, 1f,0.5f,
            -1f,    0f,      0f, 0f, 1f,0.5f,
            // Triangle 5 - TORRETA GIRAR IZQUIERDA
            -0.8f,  -0.8f,      1f, 0f, 0f,0.5f,
            -1f,-0.9f,      1f, 0f, 0f,0.5f,
            -0.8f, -1f,      1f, 0f, 0f,0.5f,
            // Triangle 6 - TORRETA GIRAR DERECHA
            0.8f,   -0.8f,      1f, 0f, 0f,0.5f,
            1f, -0.9f,      1f, 0f, 0f,0.5f,
            0.8f,  -1f,      1f, 0f, 0f,0.5f
    };

    // INICIALIZAMOS LA CLASE CREANDO EL/LOS VERTEX ARRAY (LE PASAMOS UN ARRAY DE FLOATS Y NOS CREA EL FLOATBUFFER QUE PODEMOS MANEJAR PARA PINTAR)
    public Triangulos()
    {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    //ASOCIAMOS ATRIBUTOS CON SHADERS
    public void bindData(ColorShaderProgram colorProgram)
    {
        vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, colorProgram.getColorAttributeLocation(), COLOR_COMPONENT_COUNT, STRIDE);
    }

    //METODO PARA DIBUJAR LOS TRIANGULOS (PINTA SU VERTEX ARRAY)
    public void draw()
    {
        glDrawArrays(GL_TRIANGLES, 0, 18);
    }

    public void positionTrianglesInScene()
    {
        setIdentityM(modelMatrix, 0);
        multiplyMM(modelViewProjectionMatrix, 0, modelMatrix, 0, modelMatrix, 0);
    }

}
