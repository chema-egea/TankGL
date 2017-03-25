package chema.egea.canales.util;

import static android.opengl.GLES20.*;
import static android.opengl.GLUtils.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class TextureHelper
{

	private static final String TAG = "TextureHelper";

	/*

		El metodo loadTexture will take in an Android context and a resource ID and will return
		the ID of the loaded OpenGL texture.
	 */
	public static int loadTexture(Context context, int resourceId)
	{
		//El proceso de creacion y carga de textura comienza con el mismo patron que al compilar shaders
		/*
			We generate one texture object by calling glGenTextures(1, textureObjectId, 0), passing
			in 1 as the first parameter. OpenGL will store the generated IDs in textureObjectIds.
			We also check that the call to glGenTextures() succeeded by continuing only
			if it’s not equal to zero; otherwise we log the error and return 0.
		 */
		final int[] textureObjectIds = new int[1];
		
		glGenTextures(1, textureObjectIds, 0); //glGenTextures para generar un objeto textura
		
		if (textureObjectIds[0] == 0) //si el objeto devuelto es 0, entonces es que no se pudo crear
		{
			if (LoggerConfig.ON)
			{
					Log.w(TAG, "No se pudo crear una textura de OpenGL.");
			}
			return 0;
		}

		//EL SIGUIENTE PASO ES UTILIZAR LA API DE ANDROID PARA LEER LOS DATOS DE UN ARCHIVO DE IMAGEN
		/*
			OpenGL can’t read data from a PNG or JPEG file directly because these files
			are encoded into specific compressed formats. OpenGL needs the raw data
			in an uncompressed form, so we’ll need to use Android’s built-in bitmap
			decoder to decompress our image files into a form that OpenGL understands.
		 */

		// ASI QUE EMPEZAMOS UN PROCEDIMIENTO PARA DESCOMPRIMIR NUESTRAS TEXTURAS DE IMAGEN EN UN FORMATO BITMAP (ACEPTADO POR OPENGL)

		final BitmapFactory.Options options = new BitmapFactory.Options(); // CREAMOS UNA NUEVA INSTANCIA DE BitmapFactory.Options LLAMADA options
		options.inScaled = false; // ESTABLECEMOS SU PROPIEDAD inScaled A FALSE. ESTO LE DICE A ANDROID QUE QUEREMOS LA IMAGEN ORIGINAL EN LUGAR DE UNA VERSION ESCALADA DE LOS DATOS

		/*
			We then call BitmapFactory.decodeResource() to do the actual decode, passing in
			the Android context, resource ID, and the decoding options that we’ve just
			defined. This call will decode the image into bitmap or will return null if it failed.
			We check against that failure and delete the OpenGL texture object if the
			bitmap is null. If the decode succeeded, we continue processing the texture.
		 */
		final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

		if (bitmap == null)
		{
			if (LoggerConfig.ON)
			{
				Log.w(TAG, "EL recurso con ID " + resourceId + " no se pudo decodificar.");
			}
			glDeleteTextures(1, textureObjectIds, 0);
			return 0;
		}

		/*
			Before we can do anything else with our newly generated texture object, we
			need to tell OpenGL that future texture calls should be applied to this texture
			object. We do that with a call to glBindTexture()

			The first parameter, GL_TEXTURE_2D, tells OpenGL that this should be treated
			as a two-dimensional texture, and the second parameter tells OpenGL which
			texture object ID to bind to.
		 */
		
		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

		/*
			ESTABLECEMOS FILTROS A LA TEXTURA PARA TEMAS DE ESCALADO

			We set each filter with a call to glTexParameteri(): GL_TEXTURE_MIN_FILTER refers to
			minification, while GL_TEXTURE_MAG_FILTER refers to magnification. For minification,
			we select GL_LINEAR_MIPMAP_LINEAR, which tells OpenGL to use trilinear
			filtering. We set the magnification filter to GL_LINEAR, which tells OpenGL to
			use bilinear filtering
		 */
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR); //PARA MINIFICATION: CUANDO SE TRATA DE ASIGNAR VARIOS TEXELS A UN MISMO FRAGMENT
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR); //PARA MAGNIFICATION: CUANDO SE TRATA DE ASIGNAR UN MISMO TEXEL A VARIOS FRAGMENTS

		/*
			We can now load the bitmap data into OpenGL with an easy call to GLUtils. texImage2D()
			This call tells OpenGL to read in the bitmap data defined by bitmap and copy
			it over into the texture object that is currently bound
		 */
		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

		/*
			Now that the data’s been loaded into OpenGL, we no longer need to keep the
			Android bitmap around. Under normal circumstances, it might take a few
			garbage collection cycles for Dalvik to release this bitmap data, so we should
			call recycle() on the bitmap object to release the data immediately:
		 */
		bitmap.recycle();

		/*
			Generating mipmaps is also a cinch. We can tell OpenGL to generate all of
			the necessary levels with a quick call to glGenerateMipmap()
		 */
		glGenerateMipmap(GL_TEXTURE_2D);

		/*
			Now that we’ve finished loading the texture, a good practice is to then unbind
			from the texture so that we don’t accidentally make further changes to this
			texture with other texture calls.
			Passing 0 to glBindTexture() unbinds from the current texture.
		 */
		glBindTexture(GL_TEXTURE_2D, 0);

		// The last step is to return the texture object ID
		return textureObjectIds[0];

		/*
			We have now a method that will be able to read in an image file from our
			resources folder and load the image data into OpenGL. We’ll get back a texture
			ID that we can use as a reference to this texture or get 0 if the load failed.
	 	*/
	}
}
