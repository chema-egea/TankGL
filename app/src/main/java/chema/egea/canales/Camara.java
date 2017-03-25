package chema.egea.canales;

import static android.opengl.Matrix.setLookAtM;

/**
 * Created by chema on 13/01/2016.
 */
public class Camara
{
    private final float[] viewMatrix = new float[16];

    float T_x = 0;
    float T_y = 0;
    float T_z = 0;

    float R_x = 0;
    float R_y = (float) Math.toRadians(45);

    float radiusZoom = 5;


    public Camara()
    {
        recalcularCamara();
    }

    private void recalcularCamara()
    {
        //CALCULAMOS EL EYE DEL LOOK AT
        float[] ojo = new float[3];
        ojo[0] = (float) (radiusZoom*-Math.sin(Math.toRadians(R_x))*Math.cos(Math.toRadians(R_y)));
        ojo[1] = (float) (radiusZoom*-Math.sin(Math.toRadians(R_y)));
        ojo[2] = (float) (-radiusZoom*Math.cos(Math.toRadians(R_x))*Math.cos(Math.toRadians(R_y)));

        ojo[0] += T_x;
        ojo[1] += T_y;
        ojo[2] += T_z;

        //setLookAtM(float[] rm, int rmOffset, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ)
        setLookAtM(getVistaCamara(), 0, ojo[0], ojo[1], ojo[2], T_x, T_y, T_z, 0f, 1f, 0f);
    }


    public void setNewPositionCamera(float x, float y, float z)
    {
        T_x = x;
        T_y = y;
        T_z = z;

        recalcularCamara();
    }
    public void addToPositionCamera(float x, float y, float z)
    {

        float movX = (float) (x * Math.cos(R_y));
        float movZ = (float) (z * Math.sin(R_y));
        setNewPositionCamera(T_x + movX, T_y + y, T_z + movZ);
    }
    public void setNewRotation(float rotX, float rotY)
    {
        R_x += -rotX*90f;
        R_y += rotY*90f;

        if(R_y>-20) R_y=-20;
        if(R_y<-80) R_y=-80;


        recalcularCamara();
    }
    public void setNewDistanceZoom(float scaledZoom)
    {
        radiusZoom = scaledZoom;

        recalcularCamara();
    }

    public float[] getVistaCamara()
    {
        return viewMatrix;
    }


}
