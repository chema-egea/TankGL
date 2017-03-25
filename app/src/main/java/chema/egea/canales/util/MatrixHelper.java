package chema.egea.canales.util;

/**
 * Created by chema on 10/01/2016.
 */
public class MatrixHelper
{
    //CON ESTE METODO VAMOS A CALCULAR LA MATRIX PERSPECTIVA DE FORMA MANUAL, EVITANDO CUALQUIER BUG DE ANDROID E INCOMPATIBLIDADES
    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f)
    {
        /*
            LOS PASOS QUE VAMOS A SEGUIR SON:

            1. CALCULAR LONGITUD FOCAL. Que estara basada en el campo de vision a traves del eje y.
            We use Java’s Math class to calculate the tangent, and since it wants the angle
            in radians, we convert the field of vision from degrees to radians. We then
            calculate the focal length as described in the previous section.

            2. ESCRIBIR LA MATRIZ.
            This writes out the matrix data to the floating-point array defined in the
            argument m, which needs to have at least sixteen elements. OpenGL stores
            matrix data in column-major order, which means that we write out data one
            column at a time rather than one row at a time. The first four values refer to
            the first column, the second four values to the second column, and so on.

         */


        //CALCULAMOS LA LONGITUD FOCAL
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

        //ESCRIBIMOS LA MATRIZ
        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;
        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;
        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;
        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }

}
