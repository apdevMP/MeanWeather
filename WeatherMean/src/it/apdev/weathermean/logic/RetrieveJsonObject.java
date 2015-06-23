package it.apdev.weathermean.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.util.Log;

/**
 * This class extends AsyncTask and it retrieve a JSONObject from an url
 * 
 * @author Andrea
 * 
 */
public class RetrieveJsonObject extends AsyncTask<String, Void, JSONObject> {

	private static final String TAG = "RetrieveJsonObject";

	@Override
	protected JSONObject doInBackground(String... params) {
		//Log.v(TAG, "Star retrieve JSONObject " + params[0]);
		// Occurs one string(url)
		if (params.length != 1) {
			return null;
		}

		JSONObject object;

		try {
			// Open connection to URL specified from string passed
			URL url = new URL(params[0]);
			URLConnection connection = url.openConnection();
			InputStream inputStream = connection.getInputStream();

			// Read from the buffer
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));

			// Build string related to JSON
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}

			// Build JSONObject from stringBuilder
			object = new JSONObject(stringBuilder.toString());

			return object;
			
		} catch (MalformedURLException e) {
			Log.v(TAG, "Malformed URL" + params[0]);
			//e.printStackTrace();
			return null;
		}
		 catch (IOException e) {
			Log.v(TAG, "Error while opening connection");
			//e.printStackTrace();
			return null;
		} catch (JSONException e) {
			Log.v(TAG, "Error while parsing JSONObject");
			//e.printStackTrace();
			return null;
		}

		

	}

}
