package it.apdev.weathermean.logic;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * It extends HttpService and serves to open an HTTP connection with Open
 * Weather Map.
 * 
 * @author TEAM apdev
 * 
 */
public class OpenWeatherMapHttpService extends HttpService {

	private static final String TAG = "OpenWeatherMapHttpService";
	private static final String SOURCE = "Open Weather\nMap";

	private static final String WIND = "wind";
	private static final String SPEED = "speed";
	private static final String MAIN = "main";
	private static final String HUMIDITY = "humidity";
	private static final String PRESSURE = "pressure";
	private static final String VISIBILITY = "visibility";
	private static final String TEMP = "temp";
	private static final String WEATHER = "weather";
	double null_value = -1.0;

	/**
	 * Default Constructor
	 */
	public OpenWeatherMapHttpService() {

	}

	/**
	 * Constructor that sets city and country code and constructs with them an
	 * url
	 * 
	 * @param city
	 * @param codeNation
	 */
	public OpenWeatherMapHttpService(String city, String codeNation) {
		this.city = city;
		this.codeNation = codeNation;

		String cityUrl = city.replaceAll(" ", "%20").replaceAll("'", "");

		// construct url for Open Weather Map
		this.urlString = "http://api.openweathermap.org/data/2.5/weather?q="
				+ cityUrl + "," + this.codeNation + "&units=metric";
	}

	@Override
	public Weather retrieveWeather() throws InterruptedException,
			ExecutionException, JSONException {

		// Istantiate the AsyncTask related to retrieve of JSONObject and start
		// it
		RetrieveJsonObject retrieve = new RetrieveJsonObject();
		retrieve.execute(urlString);

		// Instantiate weather and retrieve JSONObject from AsyncTask
		Weather weather = new Weather();
		JSONObject result = retrieve.get();
		if (result == null) {
			Log.v(TAG, "Error while retrieving weather");
			return null;
		}

		// Set field of weather with retrieved values
		JSONObject wind = result.getJSONObject(WIND);
		double windKmh = Utils.fromMsToKmh(wind.getDouble(SPEED));
		weather.setWind(Utils.roundMeasure(windKmh));

		JSONObject main = result.getJSONObject(MAIN);

		weather.setHumidity(main.getDouble(HUMIDITY));
		weather.setPressure(main.getDouble(PRESSURE));

		weather.setTemperature(main.getDouble(TEMP));

		//for the visibility it occurs to manage an JSONException
		double visibilityKm;
		try {
			visibilityKm = result.getDouble(VISIBILITY) / 1000;
		} catch (JSONException e) {
			visibilityKm = null_value;
		}
		weather.setVisibility(visibilityKm);

		JSONArray condition = result.getJSONArray(WEATHER);
		JSONObject description = condition.getJSONObject(0);
		String descrptionText = description.getString(MAIN);
		weather.setDescription(descrptionText);

		// for the forecast code it occurs to call getForecastCodeForService()
		int code = getForecastCodeForService(descrptionText);
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

		if (desc.contains("thunderstorm")) {
			return mapper.getForecastCode("Storm");
		} else if (desc.contains("drizzle") || desc.contains("rain")) {
			return mapper.getForecastCode("Rain");
		} else if (desc.contains("snow")) {
			return mapper.getForecastCode("Snow");
		} else if (desc.contains("cloud")) {
			return mapper.getForecastCode("Cloudy");
		} else if (desc.contains("clear")) {
			return mapper.getForecastCode("Sunny");
		}
		return 0;
	}
}
