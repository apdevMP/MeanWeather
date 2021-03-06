package it.apdev.weathermean.logic;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * It extends HttpService and serves to open an HTTP connection with Yahoo
 * 
 * @author TEAM apdev
 */
public class YahooHttpService extends HttpService {

	private static final String TAG = "YahooHttpService";
	private static final String SOURCE = "Yahoo";

	private static final String QUERY = "query";
	private static final String RESULTS = "results";
	private static final String CHANNEL = "channel";
	private static final String WIND = "wind";
	private static final String SPEED = "speed";
	private static final String ATMOSPHERE = "atmosphere";
	private static final String HUMIDITY = "humidity";
	private static final String PRESSURE = "pressure";
	private static final String VISIBILITY = "visibility";
	private static final String ITEM = "item";
	private static final String CONDITION = "condition";
	private static final String TEMP = "temp";
	private static final String TEXT = "text";

	/**
	 * Constructor that sets city and country code and constructs with them an
	 * url
	 * 
	 * @param city
	 * @param codeNation
	 */
	public YahooHttpService(String city, String codeNation) {
		this.city = city;
		this.codeNation = codeNation;

		String cityUrl = city.replaceAll(" ", "%20").replaceAll("'", "");

		// construct url for Yahoo
		this.urlString = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"
				+ cityUrl
				+ "%2C%20"
				+ this.codeNation
				+ "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
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
		JSONObject object = retrieve.get();
		if (object == null) {
			Log.v(TAG, "Error while retrieving weather");
			return null;
		}

		// Set field of weather with retrieved values
		JSONObject results = object.getJSONObject(QUERY).getJSONObject(RESULTS)
				.getJSONObject(CHANNEL);

		JSONObject wind = results.getJSONObject(WIND);
		double speedKmh = Utils.fromMphToKmh(wind.getDouble(SPEED));
		weather.setWind(Utils.roundMeasure(speedKmh));

		JSONObject atmosphere = results.getJSONObject(ATMOSPHERE);
		weather.setHumidity(atmosphere.getDouble(HUMIDITY));
		double hpaPressure = Utils
				.fromInhgToHpa(atmosphere.getDouble(PRESSURE));
		weather.setPressure(Utils.roundMeasure(hpaPressure));
		double visibilityKm = Utils.fromMilesToKm(atmosphere
				.getDouble(VISIBILITY));
		weather.setVisibility(Utils.roundMeasure(visibilityKm));

		JSONObject condition = results.getJSONObject(ITEM).getJSONObject(
				CONDITION);
		double tempCelsius = Utils.fromFahrenheitToCelsius(condition
				.getDouble(TEMP));
		weather.setTemperature(Utils.roundMeasure(tempCelsius));

		String conditionText = condition.getString(TEXT);
		weather.setDescription(conditionText);

		// for the forecast code it occurs to call getForecastCodeForService()
		int code = getForecastCodeForService(conditionText);
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
		if (desc.contains("fair") || desc.contains("clear")
				|| desc.contains("sunny")) {
			return mapper.getForecastCode("Sunny");
		} else if (desc.contains("cloudy")) {
			return mapper.getForecastCode("Cloudy");
		} else if (desc.contains("rain") || desc.contains("shower")
				|| desc.contains("drizzle")) {
			return mapper.getForecastCode("Rain");
		} else if (desc.contains("thunder") || desc.contains("storm")
				|| desc.contains("hurricane") || desc.contains("tornado")) {
			return mapper.getForecastCode("Storm");
		} else if (desc.contains("snow")) {
			return mapper.getForecastCode("Snow");
		}

		return 0;

	}

}
