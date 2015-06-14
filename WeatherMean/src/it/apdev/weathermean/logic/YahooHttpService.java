package it.apdev.weathermean.logic;

import org.json.JSONObject;

import android.util.Log;

/**
 * Classe che recupera i dati mediante una connessione HTTP alla piattaforma
 * Yahoo.
 * 
 * @author Andrea
 */
public class YahooHttpService extends HttpService {

	private static final String TAG = "YahooHttpService";

	/**
	 * Costruttore che imposta la città e il codice della nazione, costruendo
	 * l'url
	 * 
	 * @param city
	 * @param codeNation
	 */
	public YahooHttpService(String city, String codeNation) {
		this.city = city;
		this.codeNation = codeNation;

		// Costruisce l'url
		this.urlString = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"
				+ this.city
				+ "%2C%20"
				+ this.codeNation
				+ "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
	}

	@Override
	public Weather retrieveWeather() throws Exception {

		// Istanzia l'asyncTask relativo al recupero del JSON dalla piattaforma
		// e lo avvia
		RetrieveJsonObject retrieve = new RetrieveJsonObject();
		retrieve.execute(urlString).toString();

		// Istanzia la classe weather e recupera il JSON dall'asyncTask
		Weather weather = new Weather();
		JSONObject object = retrieve.get();

		// Imposta i campi della classe Weather con i valori recuperati dal JSON
		JSONObject results = object.getJSONObject("query")
				.getJSONObject("results").getJSONObject("channel");

		JSONObject wind = results.getJSONObject("wind");
		double speedKmh = Utils.fromMphToKmh(wind.getDouble("speed"));
		weather.setWind(speedKmh);

		JSONObject atmosphere = results.getJSONObject("atmosphere");
		Log.v(TAG, atmosphere.toString());
		weather.setHumidity(atmosphere.getDouble("humidity"));
		weather.setPressure(atmosphere.getDouble("pressure"));

		JSONObject condition = results.getJSONObject("item").getJSONObject(
				"condition");
		double tempCelsius = Utils.fromFahrenheitToCelsius(condition.getDouble("temp"));
		weather.setTemperature(tempCelsius);
		
		weather.setDescription(condition.getString("text"));

		Log.v(TAG, weather.toString());
		return weather;
	}

}
