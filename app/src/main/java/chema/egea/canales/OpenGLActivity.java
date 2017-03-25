package chema.egea.canales;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.opengl10.android.R;


public class OpenGLActivity extends Activity
{
	private GLSurfaceView glSurfaceView;
	private boolean rendererSet = false;

	// PARA CONTROLAR EL GIRO DE LA MALLA
	private float historicalX;
	private float historicalY;

	// PARA GESTIONAR EL ZOOM
	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);

		// la clase que permite escribir las aplicaciones con OpenGL
        glSurfaceView = new GLSurfaceView(this);

		// OpenGLRenderer implementa GLSurfaceView.Renderer, es una interfaz de renderizado generica donde se escribe el codigo de lo que queremos que se dibuje
		final OpenGLRenderer openGLRenderer = new OpenGLRenderer(this);

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        //final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		final boolean supportsEs2 =
        		configurationInfo.reqGlEsVersion >= 0x20000
        		|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
        		&& (Build.FINGERPRINT.startsWith("generic")
        		|| Build.FINGERPRINT.startsWith("unknown")
        		|| Build.MODEL.contains("google_sdk")
        		|| Build.MODEL.contains("Emulator")
        		|| Build.MODEL.contains("Android SDK built for x86")));

		Log.e("DEBUG", "DESPUES DE DECLARAR VARIABLES, supportsEs2 vale: "+supportsEs2);

		if (supportsEs2)
		{
        	// Request an OpenGL ES 2.0 compatible context.
        	glSurfaceView.setEGLContextClientVersion(2);
        	// Para que funcione en el emulador
        	glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

			// **********************************************************
        	// ASIGNAMOS AQUI NUESTRO RENDERER
        	glSurfaceView.setRenderer(openGLRenderer);
			rendererSet = true;
			// **********************************************************

			Log.e("DEBUG", "OPENGL SOPORTADO, ASIGNADO RENDERER");
        	Toast.makeText(this, "OpenGL ES 2.0 soportado", Toast.LENGTH_LONG).show();

        }
		else
		{
        	Toast.makeText(this, "Este dispositivo no soporta OpenGL ES 2.0", Toast.LENGTH_LONG).show();
        	return;
        }

		//INICIALIZAMOS EL GESTURE PARA EL ZOOM
		mScaleDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());
        
        glSurfaceView.setOnTouchListener(new OnTouchListener()
		{
        	@Override
        	public boolean onTouch(View v, MotionEvent event)
			{
        	  if (event != null)
			  {
        		// Convert touch coordinates into normalized device
        		// coordinates, keeping in mind that Android's Y
        		// coordinates are inverted.
        		final float normalizedX =   (event.getX() / (float) v.getWidth()) * 2 - 1;
        		final float normalizedY = -((event.getY() / (float) v.getHeight()) * 2 - 1);

				  //Si detectamos varios dedos
				  if(event.getPointerCount()>1)
				  {
					  mScaleDetector.onTouchEvent(event);

					  glSurfaceView.queueEvent(new Runnable()
					  {
						  @Override
						  public void run()
						  {
							  openGLRenderer.handleZoom(mScaleFactor+1.0f);
						  }
					  });
				  }
				  else
				  {
					  if (event.getAction() == MotionEvent.ACTION_DOWN)
					  {
						  //DEBEMOS USAR QUEUE EVENT PARA COMUNICARNOS CON EL OPENGL RENDERER, PORQUE VA POR HILOS
						  glSurfaceView.queueEvent(new Runnable()
						  {
							  @Override
							  public void run()
							  {
								  historicalX = normalizedX;
								  historicalY = normalizedY;
								  openGLRenderer.handleTouchPress(normalizedX, normalizedY);
							  }
						  });
					  }
					  else if (event.getAction() == MotionEvent.ACTION_MOVE)
					  {
						  //DEBEMOS USAR QUEUE EVENT PARA COMUNICARNOS CON EL OPENGL RENDERER, PORQUE VA POR HILOS
						  glSurfaceView.queueEvent(new Runnable()
						  {
							  @Override
							  public void run()
							  {
								  openGLRenderer.handleTouchDrag(normalizedX-historicalX, normalizedY-historicalY);
								  historicalX = normalizedX;
								  historicalY = normalizedY;
							  }
						  });
					  }
				  }
        		return true;
        	  }
			  else
			  {
        		return false;
        	  }
        	}
        });

		//FINALMENTE, ASIGNAMOS DE SET CONTENT VIEW A LA GLSURFACE
		setContentView(glSurfaceView);
    }

    @Override
    protected void onPause()
	{
    	super.onPause();
    	if (rendererSet)
		{
    		glSurfaceView.onPause();
    	}
    }
    		
    @Override
    protected void onResume()
	{
    	super.onResume();
    	if (rendererSet)
		{
    		glSurfaceView.onResume();
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.open_gl1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
		{
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	// CLASE PRIVADA PERSONALIZADA PARA EL GESTURE DE ZOOM
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector)
		{
			mScaleFactor /= detector.getScaleFactor();

			// Don't let the object get too small or too large.
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

			Log.e("GESTURE", "mScaleFactor es " + mScaleFactor);


			return true;
		}
	}
   
}
