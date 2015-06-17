package it.apdev.weathermean.logic;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * Classe che recupera i dati mediante una connessione HTTP alla piattaforma
 * WorldWeatherOnline. La formula "free" prevede un massimo di 250 query al
 * giorno
 * 
 * @author Andrea
 */

public class WorldWeatherOnlineHttpService extends HttpService {

	private static final String TAG = "WorldWeatherOnlineHttpService";
	private static final String KEY = "814fd964b982ee94f2aac331b673a";
	private static final String SOURCE = "World Weather Online";

	private static final String DATA = "data";
	private static final String CURRENT_CONDITION = "current_condition";
	private static final String HUMIDITY = "humidity";
	private static final String PRESSURE = "pressure";
	private static final String TEMP_C = "temp_C";
	private static final String WEATHER_DESC = "weatherDesc";
	private static final String WINDSPEED = "windspeedKmph";
	private static final String VALUE = "value";

	/**
	 * Costruttore che imposta la città e il codice della nazione, costruendo
	 * l'url
	 * 
	 * @param city
	 * @param codeNation
	 */
	public WorldWeatherOnlineHttpService(String city, String codeNation) {
		this.city = city;
		this.codeNation = codeNation;

		// Costruisce l'url
		this.urlString = "http://api.worldweatheronline.com/free/v2/weather.ashx?q="
				+ this.city
				+ "%2C"
				+ this.codeNation
				+ "&format=json&num_of_days=1&key=" + KEY;
	}

	@Override
	public Weather retrieveWeather() throws Exception {

		// Istanzia l'asyncTask relativo al recupero del JSON dalla piattaforma
		// e lo avvia
		RetrieveJsonObject retrieve = new RetrieveJsonObject();
		retrieve.execute(urlString).toString();

		// Istanzia la classe weather e recupera il JSON dall'asyncTask
		Weather weather = new Weather();
		JSONObject result = retrieve.get();

		// Imposta i campi della classe Weather con i valori recuperati dal JSON
		JSONObject currentCondition = result.getJSONObject(DATA)
				.getJSONArray(CURRENT_CONDITION).getJSONObject(0);

		weather.setHumidity(currentCondition.getDouble(HUMIDITY));
		weather.setPressure(currentCondition.getDouble(PRESSURE));
		weather.setTemperature(currentCondition.getDouble(TEMP_C));
		weather.setWind(currentCondition.getDouble(WINDSPEED));

		JSONArray condition = currentCondition.getJSONArray(WEATHER_DESC);
		JSONObject description = condition.getJSONObject(0);
		String descriptionText = description.getString(VALUE);
		weather.setDescription(descriptionText);
		weather.setForecastCode(getForecastCodeForService(descriptionText));

		weather.setSource(SOURCE);

		Log.v(TAG, weather.toString());
		return weather;
	}

	@Override
	public int getForecastCodeForService(String forecastDescription) {
		ForecastMapper mapper = ForecastMapper.getIstance();
		String desc = forecastDescription.toLowerCase();
		
		if(desc.contains("ice") || desc.contains("thunder") || desc.contains("torrential")){
			return mapper.getForecastCode("Storm");
		}
		else if(desc.contains("rain") || desc.contains("drizzle") || desc.contains("shower")){
			return mapper.getForecastCode("Rain");
		}
		else if (desc.contains("snow") || desc.contains("blizzard") || desc.contains("sleet")) {
			return mapper.getForecastCode("Snow");
		}
		else if(desc.contains("clear")||desc.contains("sunny")){
			return mapper.getForecastCode("Sunny");
		}
		else if(desc.contains("cloudy") || desc.contains("overcast")){
			return mapper.getForecastCode("Cloudy");
		}
		return 0;
		
	}

}
