package it.apdev.weathermean.logic;


import it.apdev.weathermean.R;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
	private static Drawable icon = null;
	
	private Context context;

	/**
	 * Costruttore che imposta la citt� e il codice della nazione, costruendo
	 * l'url
	 * 
	 * @param city
	 * @param codeNation
	 */
	public WorldWeatherOnlineHttpService(String city, String codeNation, Context context) {
		this.city = city;
		this.codeNation = codeNation;
		this.context = context;

		// Costruisce l'url
		this.urlString = "http://api.worldweatheronline.com/free/v2/weather.ashx?q="
				+ this.city
				+ "%2C"
				+ this.codeNation
				+ "&format=json&num_of_days=1&key=" + KEY;
	}

	@Override
	public Weather retrieveWeather() throws InterruptedException, ExecutionException, JSONException{

		// Istanzia l'asyncTask relativo al recupero del JSON dalla piattaforma
		// e lo avvia
		RetrieveJsonObject retrieve = new RetrieveJsonObject();
		retrieve.execute(urlString).toString();

		// Istanzia la classe weather e recupera il JSON dall'asyncTask
		Weather weather = new Weather();
		JSONObject result = retrieve.get();
		if(result == null){
			Log.v(TAG,"Error while retrieving weather");
			return null;
		}
		
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

	public static Drawable getIcon()
	{
		return icon;
	}

	public static void setIcon(Drawable icon)
	{
		WorldWeatherOnlineHttpService.icon = icon;
	}

	@Override
	public int getForecastCodeForService(String forecastDescription) {
		ForecastMapper mapper = ForecastMapper.getIstance();
		String desc = forecastDescription.toLowerCase();
		
		if(desc.contains("ice") || desc.contains("thunder") || desc.contains("torrential")){
			setIcon(context.getResources().getDrawable(R.drawable.storm));
			return mapper.getForecastCode("Storm");
		}
		else if(desc.contains("rain") || desc.contains("drizzle") || desc.contains("shower")){
			setIcon(context.getResources().getDrawable(R.drawable.rain));
			return mapper.getForecastCode("Rain");
		}
		else if (desc.contains("snow") || desc.contains("blizzard") || desc.contains("sleet")) {
			setIcon(context.getResources().getDrawable(R.drawable.snow));
			return mapper.getForecastCode("Snow");
		}
		else if(desc.contains("clear")||desc.contains("sunny")){
			setIcon(context.getResources().getDrawable(R.drawable.sunny));
			return mapper.getForecastCode("Sunny");
		}
		else if(desc.contains("cloudy") || desc.contains("overcast")){
			setIcon(context.getResources().getDrawable(R.drawable.cloudy));
			return mapper.getForecastCode("Cloudy");
		}
		return 0;
		
	}

}
