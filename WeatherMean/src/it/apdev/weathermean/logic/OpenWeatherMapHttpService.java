package it.apdev.weathermean.logic;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * Tale classe serve per connettersi alla piattaforma OpenWeatherMap tramite connessione HTTP
 * @author Andrea
 *
 */
public class OpenWeatherMapHttpService extends HttpService{

	private static final String TAG = "OpenWeatherMapHttpService";
	private static final String WIND = "wind";
	private static final String SPEED ="speed";
	private static final String MAIN = "main";
	private static final String HUMIDITY = "humidity";
	private static final String PRESSURE = "pressure";
	private static final String TEMP = "temp";
	private static final String WEATHER = "weather";
	
	/**
	 * Costruttore di default
	 */
	public OpenWeatherMapHttpService(){
		
	}
	/**
	 * Costruttore che imposta la città e il codice della nazione, costruendo l'url relativo alla piattaforma
	 * @param city
	 * @param codeNation
	 */
	public OpenWeatherMapHttpService(String city, String codeNation) {
		this.city = city;
		this.codeNation = codeNation;

		//costruisce l'url per openWeatherMap
		this.urlString = "http://api.openweathermap.org/data/2.5/weather?q="
				+ this.city
				+ ","
				+ this.codeNation;
	}

	@Override
	public Weather retrieveWeather() throws Exception {
		
		//Istanzia l'asyncTask relativo al recupero del JSON dalla piattaforma e lo avvia
		RetrieveJsonObject retrieve = new RetrieveJsonObject();
		retrieve.execute(urlString);

		//Istanzia la classe weather e recupera il JSON dall'asyncTask
		Weather weather = new Weather();
		JSONObject result = retrieve.get();
		
		//Imposta i campi della classe Weather con i valori recuperati dal JSON
		JSONObject wind = result.getJSONObject(WIND);
		weather.setWind(wind.getDouble(SPEED));
		
		JSONObject main = result.getJSONObject(MAIN);
		
		weather.setHumidity(main.getDouble(HUMIDITY));
		weather.setPressure(main.getDouble(PRESSURE));
		weather.setTemperature(main.getDouble(TEMP));
		
		JSONArray condition = result.getJSONArray(WEATHER);
		JSONObject description = condition.getJSONObject(0);

		weather.setDescription(description.getString(MAIN));
		
		Log.v(TAG, weather.toString());
		return weather;
	}
}