package it.apdev.weathermean.logic;

import it.apdev.weathermean.R;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Classe che recupera i dati mediante una connessione HTTP alla piattaforma
 * Yahoo.
 * 
 * @author Andrea
 */
public class YahooHttpService extends HttpService {

	private static final String TAG = "YahooHttpService";
	private static final String SOURCE ="Yahoo";
	
	private static final String QUERY ="query";
	private static final String RESULTS = "results";
	private static final String CHANNEL = "channel";
	private static final String WIND = "wind";
	private static final String SPEED = "speed";
	private static final String ATMOSPHERE = "atmosphere";
	private static final String HUMIDITY = "humidity";
	private static final String PRESSURE = "pressure";
	private static final String ITEM = "item";
	private static final String CONDITION = "condition";
	private static final String TEMP = "temp";
	private static final String TEXT = "text";
	private static Drawable icon = null;
	private Context context;
	
	/**
	 * Costruttore che imposta la citt� e il codice della nazione, costruendo
	 * l'url
	 * 
	 * @param city
	 * @param codeNation
	 */
	public YahooHttpService(String city, String codeNation, Context context) {
		this.city = city;
		this.codeNation = codeNation;
		this.context = context;

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
		JSONObject results = object.getJSONObject(QUERY)
				.getJSONObject(RESULTS).getJSONObject(CHANNEL);

		JSONObject wind = results.getJSONObject(WIND);
		double speedKmh = Utils.fromMphToKmh(wind.getDouble(SPEED));
		weather.setWind(Utils.roundMeasure(speedKmh));

		JSONObject atmosphere = results.getJSONObject(ATMOSPHERE);
		weather.setHumidity(atmosphere.getDouble(HUMIDITY));
		double hpaPressure = Utils.fromInhgToHpa(atmosphere.getDouble(PRESSURE));
		weather.setPressure(Utils.roundMeasure(hpaPressure));

		JSONObject condition = results.getJSONObject(ITEM).getJSONObject(CONDITION);
		double tempCelsius = Utils.fromFahrenheitToCelsius(condition.getDouble(TEMP));
		weather.setTemperature(Utils.roundMeasure(tempCelsius));
		
		String conditionText = condition.getString(TEXT);
		weather.setDescription(conditionText);
		weather.setForecastCode(getForecastCodeForService(conditionText));
		Log.v(TAG, "icona="+getIcon().toString());
		weather.setIcon(getIcon());
		weather.setSource(SOURCE);
		
		Log.v(TAG, weather.toString());
		return weather;
	}
	
	@Override
	public int getForecastCodeForService(String forecastDescription){
		ForecastMapper mapper = ForecastMapper.getIstance();
		
		String desc = forecastDescription.toLowerCase();
		if(desc.contains("fair") || desc.contains("clear") || desc.contains("sunny")){
			setIcon(context.getResources().getDrawable(R.drawable.sunny));
			return mapper.getForecastCode("Sunny");
		}
		else if(desc.contains("cloudy")){
			setIcon(context.getResources().getDrawable(R.drawable.cloudy));
			return mapper.getForecastCode("Cloudy");
		}
		else if (desc.contains("rain")  || desc.contains("showers")|| desc.contains("drizzle")) {
			setIcon(context.getResources().getDrawable(R.drawable.rain));
			return mapper.getForecastCode("Rain");
		}
		else if(desc.contains("thunder") || desc.contains("storm") || desc.contains("hurricane") || desc.contains("tornado")){
			setIcon(context.getResources().getDrawable(R.drawable.storm));
			return mapper.getForecastCode("Storm");
		}
		else if(desc.contains("snow")){
			setIcon(context.getResources().getDrawable(R.drawable.snow));
			return mapper.getForecastCode("Snow");
		}
		
		return 0;
		
	}

	/**
	 * @return the icon
	 */
	public static Drawable getIcon()
	{
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(Drawable icon)
	{
		this.icon = icon;
	}

}
