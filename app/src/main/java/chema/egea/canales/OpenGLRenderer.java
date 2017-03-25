package chema.egea.canales;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.opengl10.android.R;

import chema.egea.canales.objects.BaseObject;
import chema.egea.canales.objects.Suelo;
import chema.egea.canales.objects.TanqueBase;
import chema.egea.canales.objects.TanqueRuedas;
import chema.egea.canales.objects.TanqueTorreta;
import chema.egea.canales.objects.TanqueTransparente;
import chema.egea.canales.objects.Triangulos;
import chema.egea.canales.programs.ColorShaderProgram;
import chema.egea.canales.programs.TanqueShaderProgram;
import chema.egea.canales.programs.TextureShaderProgram;
import chema.egea.canales.util.LoggerConfig;
import chema.egea.canales.util.MatrixHelper;
import chema.egea.canales.util.TextureHelper;


public class OpenGLRenderer implements Renderer
{
	//Un String para los typical logs
	private static final String TAG = "OpenGLRenderer";

	//El typical contexto
	private final Context context;

	// Matrices de proyeccion y vista
	private final float[] projectionMatrix = new float[16];
	private final float[] viewProjectionMatrix = new float[16];
	Camara camara = new Camara();

	// SUELO
	private Suelo suelo;
	//TANQUE
	//private TanqueBase tanqueCompletoPrueba;
	private TanqueTorreta tanqueTorreta;
	private TanqueBase tanqueBase;
	private TanqueRuedas tanqueRuedas;
	private TanqueRuedas tanqueRuedas2;
	private TanqueTransparente tanqueTransparente;
	//TRIANGULOS HUD
	private Triangulos triangulosPantalla;
	//EXTRAS PARA COMPLETAR LAS 3 TEXTURAS
	private BaseObject mono;

	// Los programas de opengl que vamos a utilizar
	private TextureShaderProgram textureProgram;
	private ColorShaderProgram colorProgram;
	private TanqueShaderProgram tanqueProgram;

	// Las texturas que vamos a utilizar
	private int texturaSuelo;
	private int texturaTanque;

	//Extra
	private int texturaMono;

	private boolean animacion = false;


	// ***************************************************************
	// 						METODO CONSTRUCTOR
	// ***************************************************************
	public OpenGLRenderer(Context context)
	{
		this.context = context;
	}

	/* *****************************************************************************************************************************************************************
		onSurfaceCreated(), este método es llamado cuando la superficie (a.k.a. surface) es creada o es re-creada, como este método es llamado al inicio del renderizado,
		es un buen lugar para colocar lo que no variara en el ciclo del renderizado. Ejm: el color de fondo, la activación del índice z, etc, etc.
	  ****************************************************************************************************************************************************************** */
	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
	{
		// Establecemos el color de fondo mediante gl.glClearColor()
		glClearColor(0.0f, 0.5f, 0.5f, 0.0f);

		// Inicializamos los objetos que vamos a construir
		suelo = new Suelo(this.context, R.raw.suelo);
		//tanqueCompletoPrueba = new TanqueBase(this.context, R.raw.tanquecompleto);
		tanqueBase = new TanqueBase(this.context, R.raw.tanquecuerpo);
		tanqueRuedas = new TanqueRuedas(this.context, R.raw.tanqueruedas);
		tanqueRuedas2 = new TanqueRuedas(this.context, R.raw.tanqueruedas2);
		tanqueTorreta = new TanqueTorreta(this.context, R.raw.tanquetorreta);
		tanqueTransparente = new TanqueTransparente(this.context, R.raw.tanquetransparente);

		triangulosPantalla = new Triangulos();

		mono = new BaseObject(this.context, R.raw.mono);

		// Inicializamos los programas de opengl que van a utilizar nuestros objetos
		textureProgram = new TextureShaderProgram(context);
		colorProgram = new ColorShaderProgram(context);
		tanqueProgram = new TanqueShaderProgram(context);

		// Cargamos las texturas que van a usar nuestros objetos
		texturaSuelo = TextureHelper.loadTexture(context, R.drawable.texturam);
		texturaTanque = TextureHelper.loadTexture(context, R.drawable.texturas);

		texturaMono = TextureHelper.loadTexture(context, R.drawable.mono_tex);

		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	}

	/* **********************************************************************************************************************************************************
		onSurfaceChanged(), este método es llamado cuando la superficie cambia de alguna manera, por ejemplo al girar el móvil y colocarlo en posición de paisaje.
	  *********************************************************************************************************************************************************** */
	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height)
	{
		// Set the OpenGL viewport to fill the entire surface.
		glViewport(0, 0, width, height);
		MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 100f);
		//setLookAtM(float[] rm, int rmOffset, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ)
		//setLookAtM(camara.getVistaCamara(), 0, 0f, 0f, 5.2f, 0f, 0f, 0f, 0f, 1f, 0f);
	}


	/* *****************************************************************************************************************
		onDrawFrame(), este método en particular es el encargado de dibujar sobre la superficie (a.k.a. surface)
	   ***************************************************************************************************************** */
	@Override
	public void onDrawFrame(GL10 glUnused)
	{
		// Limpia la pantalla y el buffer de profundidad
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_DEPTH_TEST);
		//glEnable(GL_CULL_FACE);
		glLineWidth(2.0f);


		// Multiply the view and projection matrices together.
		multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, camara.getVistaCamara(), 0);


		// Dibujamos el mono extra

		glEnable(GL_CULL_FACE);

		mono.positionObjectInScene(3, 1, 3);
		mono.rotateObjectInScene(0, -90);
		mono.updateMVP(viewProjectionMatrix);
		tanqueProgram.useProgram();
		tanqueProgram.setUniforms(mono.modelViewProjectionMatrix, mono.modelMatrix, texturaMono);
		mono.bindData(tanqueProgram);
		mono.draw();

		mono.positionObjectInScene(-3, 1, 3);
		mono.rotateObjectInScene(0,90);
		mono.updateMVP(viewProjectionMatrix);
		tanqueProgram.useProgram();
		tanqueProgram.setUniforms(mono.modelViewProjectionMatrix, mono.modelMatrix, texturaMono);
		mono.bindData(tanqueProgram);
		mono.draw();

		glDisable(GL_CULL_FACE);

		// Dibuja el suelo
		suelo.updateMVP(viewProjectionMatrix);
		tanqueProgram.useProgram();
		tanqueProgram.setUniforms(suelo.modelViewProjectionMatrix, suelo.modelMatrix, texturaSuelo);
		suelo.bindData(tanqueProgram);
		suelo.draw();

		// Dibuja el cuerpo tanque
		tanqueBase.updateMVP(viewProjectionMatrix);
		tanqueProgram.useProgram();
		tanqueProgram.setUniforms(tanqueBase.modelViewProjectionMatrix, tanqueBase.modelMatrix, texturaTanque);
		tanqueBase.bindData(tanqueProgram);
		tanqueBase.draw();

		// Dibuja tanque - torreta
		tanqueTorreta.updateMVP(viewProjectionMatrix);
		tanqueProgram.useProgram();
		tanqueProgram.setUniforms(tanqueTorreta.modelViewProjectionMatrix, tanqueTorreta.modelMatrix, texturaTanque);
		tanqueTorreta.bindData(tanqueProgram);
		tanqueTorreta.draw();


		// Dibuja tanque - transparente

		glEnable(GL_BLEND);

		tanqueTransparente.updateMVP(viewProjectionMatrix);
		tanqueProgram.useProgram();
		tanqueProgram.setUniforms(tanqueTransparente.modelViewProjectionMatrix, tanqueTransparente.modelMatrix, texturaTanque);
		tanqueTransparente.bindData(tanqueProgram);
		tanqueTransparente.draw();

		glDisable(GL_BLEND);

		if (animacion)
		{
			// Dibuja tanque - ruedas
			tanqueRuedas.updateMVP(viewProjectionMatrix);
			tanqueProgram.useProgram();
			tanqueProgram.setUniforms(tanqueRuedas.modelViewProjectionMatrix, tanqueRuedas.modelMatrix, texturaTanque);
			tanqueRuedas.bindData(tanqueProgram);
			tanqueRuedas.draw();
		}
		else
		{
			// Dibuja tanque - ruedas
			tanqueRuedas2.updateMVP(viewProjectionMatrix);
			tanqueProgram.useProgram();
			tanqueProgram.setUniforms(tanqueRuedas2.modelViewProjectionMatrix, tanqueRuedas2.modelMatrix, texturaTanque);
			tanqueRuedas2.bindData(tanqueProgram);
			tanqueRuedas2.draw();
		}

		//glDisable(GL_CULL_FACE);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);

		//Dibujamos triangulos
		triangulosPantalla.positionTrianglesInScene();
		colorProgram.useProgram();
		colorProgram.setUniforms(triangulosPantalla.modelViewProjectionMatrix, 0f, 0f, 1f);
		triangulosPantalla.bindData(colorProgram);
		triangulosPantalla.draw();

		glDisable(GL_BLEND);

	}




	/*  ***********************************************
		MANEJADORES DE EVENTOS TOUCH, DRAG, PELLIZCO
	 ************************************************** */

	public void handleTouchPress(float normalizedX, float normalizedY)
	{
		if (LoggerConfig.ON)
		{
			Log.w(TAG, "Touch Press ["+normalizedX+", "+normalizedY+"]");
		}
		if (normalizedX<0.1 && normalizedX>-0.1)
		{
			//FLECHA ARRIBA
			if (normalizedY>0.8)
			{
				animacion=!animacion;
				Log.e("TOQUES", "ARRIBA");
				tanqueBase.moverTanque(1);

				float[] auxPosTanque = tanqueBase.getObjectPosition();

				tanqueRuedas.positionObjectInScene(auxPosTanque[0], auxPosTanque[1], auxPosTanque[2]);
				tanqueRuedas2.positionObjectInScene(auxPosTanque[0], auxPosTanque[1], auxPosTanque[2]);
				tanqueTransparente.positionObjectInScene(auxPosTanque[0], auxPosTanque[1], auxPosTanque[2]);
				tanqueTorreta.positionObjectInScene(auxPosTanque[0], auxPosTanque[1], auxPosTanque[2]);

				camara.setNewPositionCamera(auxPosTanque[0], auxPosTanque[1], auxPosTanque[2]);
			}
			//FLECHA ABAJO
			if (normalizedY<-0.8)
			{
				animacion=!animacion;
				Log.e("TOQUES", "ABAJO");
				tanqueBase.moverTanque(-1);

				float[] auxPosTanque = tanqueBase.getObjectPosition();


				tanqueRuedas.positionObjectInScene(auxPosTanque[0], auxPosTanque[1], auxPosTanque[2]);
				tanqueRuedas2.positionObjectInScene(auxPosTanque[0], auxPosTanque[1], auxPosTanque[2]);
				tanqueTransparente.positionObjectInScene(auxPosTanque[0], auxPosTanque[1], auxPosTanque[2]);
				tanqueTorreta.positionObjectInScene(auxPosTanque[0], auxPosTanque[1], auxPosTanque[2]);

				camara.setNewPositionCamera(auxPosTanque[0], auxPosTanque[1], auxPosTanque[2]);
			}
		}
		if (normalizedY<0.1 && normalizedY>-0.1)
		{
			//FLECHA DERECHA
			if (normalizedX>0.8)
			{
				animacion= !animacion;
				Log.e("TOQUES", "DERECHA");
				//tanque.moverTanque(-1, 0, 0);
				tanqueBase.addRotationToObject(0, -5);
				tanqueRuedas.addRotationToObject(0, -5);
				tanqueRuedas2.addRotationToObject(0, -5);
				tanqueTransparente.addRotationToObject(0, -5);
				tanqueTorreta.addRotationToObject(0, -5);
				camara.setNewRotation(5.f/90,0);
				//camara.setNewRotation(0,5);
			}
			//FLECHA IZQUIERDA
			if (normalizedX < -0.8)
			{
				animacion=!animacion;
				Log.e("TOQUES", "IZQUIERDA");
				//tanque.moverTanque(1, 0, 0);
				tanqueBase.addRotationToObject(0, 5);
				tanqueRuedas.addRotationToObject(0, 5);
				tanqueRuedas2.addRotationToObject(0, 5);
				tanqueTransparente.addRotationToObject(0, 5);
				tanqueTorreta.addRotationToObject(0, 5);
				camara.setNewRotation(-5.f/90,0);
				//camara.setNewRotation(0,-5);
			}

		}
		//FLECHA TORRETA DERECHA
		if (normalizedX>0.8 && normalizedY<-0.8)
		{
			Log.e("TOQUES", "TORRETA DERECHA");
			tanqueTorreta.addRotationToObject(0,-5);
		}
		//FLECHA TORRETA IZQUIERDA
		if (normalizedX<-0.8 && normalizedY<-0.8)
		{
			Log.e("TOQUES", "TORRETA IZQUIERDA");
			tanqueTorreta.addRotationToObject(0,5);
		}
	}
	
	public void handleTouchDrag(float normalizedX, float normalizedY)
	{
		if (LoggerConfig.ON)
		{
			Log.w(TAG, "Touch Drag ["+normalizedX+", "+normalizedY+"]");
		}
		camara.setNewRotation(normalizedX, normalizedY);
	}

	public void handleZoom(float escalado) {
		Log.e("GESTURE","ESTAMOS EN EL RENDERER");
		camara.setNewDistanceZoom(escalado);
	}


}