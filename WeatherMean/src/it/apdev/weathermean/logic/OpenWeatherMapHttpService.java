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
 * Tale classe serve per connettersi alla piattaforma OpenWeatherMap tramite connessione HTTP
 * @author Andrea
 *
 */
public class OpenWeatherMapHttpService extends HttpService{

	private static final String TAG = "OpenWeatherMapHttpService";
	private static final String SOURCE = "Open Weather Map";
	
	private static final String WIND = "wind";
	private static final String SPEED ="speed";
	private static final String MAIN = "main";
	private static final String HUMIDITY = "humidity";
	private static final String PRESSURE = "pressure";
	private static final String TEMP = "temp";
	private static final String WEATHER = "weather";
	private static Drawable icon = null;
	
	private Context context;
	
	/**
	 * Costruttore di default
	 */
	public OpenWeatherMapHttpService(){
		
	}
	/**
	 * Costruttore che imposta la citt� e il codice della nazione, costruendo l'url relativo alla piattaforma
	 * @param city
	 * @param codeNation
	 */
	public OpenWeatherMapHttpService(String city, String codeNation, Context context) {
		this.city = city;
		this.codeNation = codeNation;
		this.context = context;

		//costruisce l'url per openWeatherMap
		this.urlString = "http://api.openweathermap.org/data/2.5/weather?q="
				+ this.city
				+ ","
				+ this.codeNation
				+"&units=metric";
	}

	@Override
	public Weather retrieveWeather() throws InterruptedException, ExecutionException,JSONException {
		
		//Istanzia l'asyncTask relativo al recupero del JSON dalla piattaforma e lo avvia
		RetrieveJsonObject retrieve = new RetrieveJsonObject();
		retrieve.execute(urlString);

		//Istanzia la classe weather e recupera il JSON dall'asyncTask
		Weather weather = new Weather();
		JSONObject result = retrieve.get();
		if(result == null){
			Log.v(TAG,"Error while retrieving weather");
			return null;
		}
		
		//Imposta i campi della classe Weather con i valori recuperati dal JSON
		JSONObject wind = result.getJSONObject(WIND);
		double windKmh = Utils.fromMsToKmh(wind.getDouble(SPEED));
		weather.setWind(Utils.roundMeasure(windKmh));
		
		JSONObject main = result.getJSONObject(MAIN);
		
		weather.setHumidity(main.getDouble(HUMIDITY));
		weather.setPressure(main.getDouble(PRESSURE));
		
		//double tempKelvin = Utils.fromKelvinToCelsius(main.getDouble(TEMP));
		//weather.setTemperature(Utils.roundMeasure(tempKelvin));
		weather.setTemperature(main.getDouble(TEMP));
		
		JSONArray condition = result.getJSONArray(WEATHER);
		JSONObject description = condition.getJSONObject(0);
		String descrptionText = description.getString(MAIN); 
		weather.setDescription(descrptionText);
		weather.setForecastCode(getForecastCodeForService(descrptionText));
		
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
		OpenWeatherMapHttpService.icon = icon;
	}
	@Override
	public int getForecastCodeForService(String forecastDescription) {
		
		ForecastMapper mapper = ForecastMapper.getIstance();
		String desc = forecastDescription.toLowerCase(); 
		
		if(desc.contains("thunderstorm")){
			setIcon(context.getResources().getDrawable(R.drawable.storm));
			return mapper.getForecastCode("Storm");
		}
		else if(desc.contains("drizzle") || desc.contains("rain")){
			setIcon(context.getResources().getDrawable(R.drawable.rain));
			return mapper.getForecastCode("Rain");
		}
		else if(desc.contains("snow")){
			setIcon(context.getResources().getDrawable(R.drawable.snow));
			return mapper.getForecastCode("Snow");
		}
		else if (desc.contains("cloud")){
			setIcon(context.getResources().getDrawable(R.drawable.cloudy));
			return mapper.getForecastCode("Cloudy");
		}
		else if (desc.contains("clear")) {
			setIcon(context.getResources().getDrawable(R.drawable.sunny));
			return mapper.getForecastCode("Sunny");
		}
		return 0;
	}
}
