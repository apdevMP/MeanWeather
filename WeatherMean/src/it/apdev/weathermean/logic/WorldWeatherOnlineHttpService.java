package it.apdev.weathermean.logic;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * It extends HttpService and serves to open an HTTP connection with World
 * Weather Online
 * 
 * @author TEAM apdev
 */

public class WorldWeatherOnlineHttpService extends HttpService {

	private static final String TAG = "WorldWeatherOnlineHttpService";
	private static final String KEY = "814fd964b982ee94f2aac331b673a";
	private static final String SOURCE = "World Weather \nOnline";

	private static final String DATA = "data";
	private static final String CURRENT_CONDITION = "current_condition";
	private static final String HUMIDITY = "humidity";
	private static final String PRESSURE = "pressure";
	private static final String TEMP_C = "temp_C";
	private static final String WEATHER_DESC = "weatherDesc";
	private static final String WINDSPEED = "windspeedKmph";
	private static final String VISIBILITY = "visibility";
	private static final String VALUE = "value";

	/**
	 * Constructor that sets city and country code and constructs with them an
	 * url
	 * 
	 * @param city
	 * @param codeNation
	 */
	public WorldWeatherOnlineHttpService(String city, String codeNation) {
		this.city = city;
		this.codeNation = codeNation;
		String cityUrl = city.replaceAll(" ", "%20").replaceAll("'","%20");

		// construct url for World Weather Online
		this.urlString = "http://api.worldweatheronline.com/free/v2/weather.ashx?q="
				+ cityUrl
				+ "%2C"
				+ this.codeNation
				+ "&format=json&num_of_days=1&key=" + KEY;
	}

	@Override
	public Weather retrieveWeather() throws InterruptedException,
			ExecutionException, JSONException {

		// Istantiate the AsyncTask related to retrieve of JSONObject and start
		// it
		RetrieveJsonObject retrieve = new RetrieveJsonObject();
		retrieve.execute(urlString).toString();

		// Instantiate weather and retrieve JSONObject from AsyncTask
		Weather weather = new Weather();
		JSONObject result = retrieve.get();
		if (result == null) {
			Log.v(TAG, "Error while retrieving weather");
			return null;
		}

		// Set field of weather with retrieved values
		JSONObject currentCondition = result.getJSONObject(DATA)
				.getJSONArray(CURRENT_CONDITION).getJSONObject(0);

		weather.setHumidity(currentCondition.getDouble(HUMIDITY));
		weather.setPressure(currentCondition.getDouble(PRESSURE));
		weather.setTemperature(currentCondition.getDouble(TEMP_C));
		weather.setWind(currentCondition.getDouble(WINDSPEED));
		weather.setVisibility(currentCondition.getDouble(VISIBILITY));

		JSONArray condition = currentCondition.getJSONArray(WEATHER_DESC);
		JSONObject description = condition.getJSONObject(0);
		String descriptionText = description.getString(VALUE);
		weather.setDescription(descriptionText);

		// for the forecast code it occurs to call getForecastCodeForService()
		int code = getForecastCodeForService(descriptionText);
		weather.setForecastCode(code);
		ForecastMapper mapper = ForecastMapper.getIstance();
		weather.setIdIcon(mapper.getIconId(code));

		weather.setSource(SOURCE);

		Log.v(TAG, weather.toString());
		return weather;
	}

	@Override
	public int getForecastCodeForService(String forecastDescription) {
		ForecastMapper mapper = ForecastMapper.getIstance();
		String desc = forecastDescription.toLowerCase();

		if (desc.contains("ice") || desc.contains("thunder")
				|| desc.contains("torrential")) {
			return mapper.getForecastCode("Storm");
		} else if (desc.contains("rain") || desc.contains("drizzle")
				|| desc.contains("shower")) {
			return mapper.getForecastCode("Rain");
		} else if (desc.contains("snow") || desc.contains("blizzard")
				|| desc.contains("sleet")) {
			return mapper.getForecastCode("Snow");
		} else if (desc.contains("clear") || desc.contains("sunny")) {
			return mapper.getForecastCode("Sunny");
		} else if (desc.contains("cloudy") || desc.contains("overcast")) {
			return mapper.getForecastCode("Cloudy");
		}
		return 0;

	}

}
