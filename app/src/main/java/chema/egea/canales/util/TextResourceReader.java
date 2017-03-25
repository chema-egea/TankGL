package chema.egea.canales.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.Resources;

/*
	We’ve defined a method to read in text from a resource, readTextFileFromResource().
 */
public class TextResourceReader
{

	/*
		Llamaremos a readTextFileFromResource desde nuestro codigo, y le pasaremos el contexto y el ID del recurso a leer.
		El contexto de android lo necesitamos para poder acceder a los recursos.
		Por ejemplo, para leer en el vertex shader, llamaremos a este metodo de la siguiente forma:
		readTextFileFromResource(this.context, R.raw.simple_fragment_shader)
	 */
	public static String readTextFileFromResource(Context context, int resourceId)
	{
		StringBuilder body = new StringBuilder();
		try
		{
			InputStream inputStream = context.getResources().openRawResource(resourceId);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String nextLine;
			
			while ((nextLine = bufferedReader.readLine()) != null)
			{
				body.append(nextLine);
				body.append('\n');
			}
		} catch (IOException e)
		{
			throw new RuntimeException("Could not open resource: " + resourceId, e);
		} catch (Resources.NotFoundException nfe)
		{
			throw new RuntimeException("Resource not found: " + resourceId, nfe);
		}
		return body.toString();
	}
}
