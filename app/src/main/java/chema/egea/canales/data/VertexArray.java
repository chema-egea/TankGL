package chema.egea.canales.data;

/**
 * Created by chema on 10/01/2016.
 */
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import chema.egea.canales.Constants;

/*
    This code contains a FloatBuffer that will be used to store our vertex array data
    in native code
 */
public class VertexArray
{
    private final FloatBuffer floatBuffer;

    // EL CONSTRUCTOR TOMA UN ARRAY DE JAVA DE FLOATS Y LOS ESCRIBE EN EL FloatBuffer
    public VertexArray(float[] vertexData)
    {
        floatBuffer = ByteBuffer
                .allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }

    // CONSTRUCTOR ALTERNATIVO PARA PODER MANEJARNOS CON EL LECTOR DEL PUCHOL
    public VertexArray(FloatBuffer bufferfloats)
    {
        floatBuffer = bufferfloats;
    }


    // CREAMOS ESTE METODO GENERICO PARA ASOCIAR UN ATRIBUTO EN UN SHADER CON LOS DATOS
    public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride)
    {
        floatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, floatBuffer);
        glEnableVertexAttribArray(attributeLocation);

        floatBuffer.position(0);
    }
}
