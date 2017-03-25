package chema.egea.canales.objects;

import android.content.Context;

import chema.egea.canales.data.VertexArray;
import chema.egea.canales.programs.TanqueShaderProgram;
import chema.egea.canales.util.Resource3DSReader;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static chema.egea.canales.Constants.BYTES_PER_FLOAT;

/**
 * Created by chema on 14/01/2016.
 */
public class BaseObject
{
    // Cantidad de componentes de posicion por vertice (x,y,z)
    protected static final int POSITION_COMPONENT_COUNT = 3;
    // Cantidad de componentes de normales por vertice (x,y,z)
    protected static final int NORMAL_COMPONENT_COUNT = 3;
    // Cantidad de componentes de mapas UV por vertice (U,V)
    protected static final int UV_COMPONENT_COUNT = 2;
    // Calculo del tamaño de los datos (3+3+2 = 8 floats) entre vertices (le decimos a opengl cuantos bytes hay entre vertices)
    protected static final int STRIDE = (POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT + UV_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    Resource3DSReader obj3DS;

    public final float[] modelMatrix = new float[16];
    public final float[] modelViewProjectionMatrix = new float[16];
    protected final VertexArray[] vertexArray;

    //Posicion tanque variables
    protected float x_T = 0;
    protected float y_T = 0;
    protected float z_T = 0;

    // Rotacion alrededor de los ejes
    protected float x_R = 0f;
    protected float y_R = 0f;


    // INICIALIZAMOS LA CLASE CREANDO EL/LOS VERTEX ARRAY (LE PASAMOS UN ARRAY DE FLOATS Y NOS CREA EL FLOATBUFFER QUE PODEMOS MANEJAR PARA PINTAR)
    public BaseObject(Context context, int recurso3D)
    {
        // Lee un archivo 3DS desde un recurso
        obj3DS = new Resource3DSReader();
        obj3DS.read3DSFromResource(context, recurso3D);

        vertexArray = new VertexArray[obj3DS.dataBuffer.length];
        for (int i=0;i<obj3DS.dataBuffer.length;i++)
        {
            vertexArray[i] = new VertexArray(obj3DS.dataBuffer[i]);
        }

        calculateModelMatrix();
    }

    // METODO PARA ENLAZAR EL VERTEX ARRAY CON EL SHADER PROGRAM
    public void bindData(TanqueShaderProgram tanqueProgram)
    {
        for (int i=0;i<obj3DS.dataBuffer.length;i++)
        {
            //setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride)

            //Vector de vertices
            vertexArray[i].setVertexAttribPointer(0, tanqueProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
            //Vector de normales
            vertexArray[i].setVertexAttribPointer(POSITION_COMPONENT_COUNT, tanqueProgram.getNormalTextureCoordinatesAttributeLocation(), NORMAL_COMPONENT_COUNT, STRIDE);
            //Vector de UVs
            vertexArray[i].setVertexAttribPointer((POSITION_COMPONENT_COUNT+NORMAL_COMPONENT_COUNT), tanqueProgram.getUVTextureCoordinatesAttributeLocation(), NORMAL_COMPONENT_COUNT, STRIDE);
        }
    }

    //ACTUALIZAMOS LA MVP
    public void updateMVP(float[] viewProjectionMatrix)
    {
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    //METODO PARA DIBUJAR EL TANQUE (PINTA SU VERTEX ARRAY)
    public void draw()
    {
        for (int i = 0; i < obj3DS.numMeshes; i++)
        {
            glDrawArrays(GL_TRIANGLES, 0, obj3DS.numVertices[i]);

        }
    }


    protected void calculateModelMatrix()
    {
        float[] auxRotationMatrix=new float[16];
        setIdentityM(auxRotationMatrix, 0);

        rotateM(auxRotationMatrix, 0, y_R, 0f, 1f, 0f);
        rotateM(auxRotationMatrix, 0, x_R, 1f, 0f, 0f);

        float[] auxTranslationMatrix=new float[16];
        setIdentityM(auxTranslationMatrix, 0);

        translateM(auxTranslationMatrix, 0, x_T, y_T, z_T);

        multiplyMM(modelMatrix, 0, auxTranslationMatrix, 0, auxRotationMatrix, 0);

    }


    // ROTAR EL TANQUE EN LA ESCENA
    public void rotateObjectInScene(float rx, float ry)
    {
        x_R=rx;
        y_R=ry;

        calculateModelMatrix();

    }

    // POSICIONAR EL TANQUE EN LA ESCENA
    public void positionObjectInScene(float x, float y, float z)
    {
        x_T = x;
        y_T = y;
        z_T = z;

        calculateModelMatrix();
    }

    // MOVER EL TANQUE POR LA ESCENA
    public void moverTanque(float x)
    {
        float movX = (float) (x * Math.sin(Math.toRadians(y_R)));
        float movZ = (float) (x * Math.cos(Math.toRadians(y_R)));
        positionObjectInScene(x_T + movX, y_T, z_T + movZ);
    }

    public void addRotationToObject(float rX, float rY)
    {
        rotateObjectInScene(x_R + rX, y_R + rY);
    }

    public float[] getObjectPosition()
    {
        float[] auxPosition = new float[3];
        auxPosition[0] = x_T;
        auxPosition[1] = y_T;
        auxPosition[2] = z_T;

        return auxPosition;
    }

    public float[] getObjectRotation()
    {
        float[] auxRotation = new float[2];
        auxRotation[0] = x_R;
        auxRotation[1] = y_R;

        return auxRotation;
    }
}
