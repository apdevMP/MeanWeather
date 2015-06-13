package it.apdev.weathermean.logic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Classe che estende AsyncTask che, dato un url, recupera il JSON relativo
 * @author Andrea
 *
 */
public class RetrieveJsonObject extends AsyncTask<String, Void, JSONObject> {

	private static final String TAG = "RetrieveJsonObject";

	@Override
	protected JSONObject doInBackground(String... params) {
		
		//Occorre una sola stringa
		if (params.length != 1) {
			return null;
		}
		
		Log.v(TAG, params[0]);
		JSONObject object;

		try {
			//Apro una connessione all'url specificato dalla stringa
			URL url = new URL(params[0]);
			URLConnection connection = url.openConnection();

			InputStream inputStream = connection.getInputStream();

			//Leggo in un buffer
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));

			//Costruisco la stringa relativa al JSON
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			
			//Dalla stringa costruisco un JSONObject
			object = new JSONObject(stringBuilder.toString());
			Log.v(TAG, object.toString());
			
			return object;

		} catch (Exception e) {
			Log.v(TAG, "Excption: " + e.toString());
			e.printStackTrace();
			return null;
		}

	}


}
