package it.apdev.weathermean.presentation;

import java.util.ArrayList;
import java.util.List;

import it.apdev.weathermean.R;
import it.apdev.weathermean.logic.OpenWeatherMapHttpService;
import it.apdev.weathermean.logic.Weather;
import it.apdev.weathermean.logic.WorldWeatherOnlineHttpService;
import it.apdev.weathermean.logic.YahooHttpService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MeanActivity extends Activity {

	private String city, codeNation;
	private TextView tvCity, tvTemp, tvForecast, tvWind, tvHumidity,
			tvPressure;
	private Button btnDetails;
	private static final String TAG = "MeanActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mean_activity);

		Log.v(TAG, "Start MeanActivity");

		tvCity = (TextView) findViewById(R.id.textViewCity);
		tvForecast = (TextView) findViewById(R.id.textViewForecast);
		tvTemp = (TextView) findViewById(R.id.textViewTemperature);
		tvWind = (TextView) findViewById(R.id.textViewWindValue);
		tvHumidity = (TextView) findViewById(R.id.textViewHumidityValue);
		tvPressure = (TextView) findViewById(R.id.textViewPressureValue);
		btnDetails = (Button) findViewById(R.id.buttonDetails);

		Intent intent = getIntent();
		city = intent.getStringExtra("city_name");
		codeNation = intent.getStringExtra("country_code");

		final ArrayList<Weather> list;

		list = startServices();

		Weather meanWeather = new Weather();
		meanWeather.mergeWeather(list);
		tvCity.setText(city + "," + codeNation);
		tvTemp.setText("" + meanWeather.getTemperature() + " °C");
		tvHumidity.setText("" + meanWeather.getHumidity() + "%");
		tvWind.setText("" + meanWeather.getWind() + " km/h");

		// fare listener btnDetails
		btnDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MeanActivity.this, DetailsActivity.class);
				i.putExtra("weather_list", list);
				startActivity(i);
				finish();
			}
		});

	}

	private ArrayList<Weather> startServices() {

		ArrayList<Weather> list = new ArrayList<Weather>();

		YahooHttpService yahooService = new YahooHttpService(city, codeNation);
		OpenWeatherMapHttpService openWeatherService = new OpenWeatherMapHttpService(
				city, codeNation);
		WorldWeatherOnlineHttpService worldWeatherService = new WorldWeatherOnlineHttpService(
				city, codeNation);

		try {
			list.add(yahooService.retrieveWeather());
			list.add(openWeatherService.retrieveWeather());
			list.add(worldWeatherService.retrieveWeather());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

}
