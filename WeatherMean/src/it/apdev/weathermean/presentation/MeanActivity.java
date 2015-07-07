package it.apdev.weathermean.presentation;

import java.util.ArrayList;

import it.apdev.weathermean.R;
import it.apdev.weathermean.logic.Weather;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author TEAM apdev
 * 
 * Shows the mean weather forecast for the chosen city.
 * Through a button it is also possible to view the details of the forecasts.
 *
 */

public class MeanActivity extends Activity
{

	private static final String	TAG	= "MeanActivity";

	private String				city, codeNation;
	private TextView			tvCity, tvTemp, tvForecast, tvWind, tvHumidity, tvPressure, tvVisibility;
	private Button				btnDetails;

	private ImageView			imgViewIcon;

	private Weather				meanWeather;
	private ArrayList<Weather>	list;

	private String				currentCity, currentCcode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mean_activity);

		Log.v(TAG, "Start MeanActivity");

		tvCity = (TextView) findViewById(R.id.textViewCity);
		tvForecast = (TextView) findViewById(R.id.textViewForecast);
		tvTemp = (TextView) findViewById(R.id.textViewTemperature);
		tvWind = (TextView) findViewById(R.id.textViewWindValue);
		tvHumidity = (TextView) findViewById(R.id.textViewHumidityValue);
		tvPressure = (TextView) findViewById(R.id.textViewPressureValue);
		tvVisibility = (TextView) findViewById(R.id.textViewVisibilityValue);
		btnDetails = (Button) findViewById(R.id.buttonDetails);
		imgViewIcon = (ImageView) findViewById(R.id.imageViewWeather);

		//get extras from the intent
		Intent intent = getIntent();
		city = intent.getStringExtra("city_name");
		codeNation = intent.getStringExtra("country_code");
		list = intent.getParcelableArrayListExtra("weather_list");
		meanWeather = intent.getParcelableExtra("weather_mean");
		currentCity = intent.getStringExtra("current_city");
		currentCcode = intent.getStringExtra("current_ccode");

		//set the title and the color of the action bar
		getActionBar().setTitle(getString(R.string.mean_action_bar_title) + " " + city);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.action_bar_color));
		
		tvCity.setText(city + "," + codeNation);
		tvTemp.setText(meanWeather.getTemperature() + " °C");
		tvHumidity.setText(meanWeather.getHumidity() + "%");
		tvWind.setText(meanWeather.getWind() + " km/h");
		tvPressure.setText(meanWeather.getPressure() + " hPa");
		tvVisibility.setText(meanWeather.getVisibility() + " km");
		tvForecast.setText(meanWeather.getDescription());

		//get the weather description
		String meanForecastDescription = meanWeather.getDescription();
		tvForecast.setText(meanForecastDescription);

		//retrieve the right icon based on the forecast description
		Log.v(TAG, "ForecastCode: " + meanWeather.getForecastCode() + " Icon id: "+meanWeather.getIdIcon() );
		imgViewIcon.setImageResource(meanWeather.getIdIcon());

		
		//listener fot the details button
		btnDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				
				//go to the DetailsActivity to see the details for each data source
				Intent intent = new Intent(MeanActivity.this, DetailsActivity.class);
				intent.putParcelableArrayListExtra("weather_list", list);
				intent.putExtra("weather_mean", meanWeather);
				intent.putExtra("city_name", city);
				intent.putExtra("country_code", codeNation);
				intent.putExtra("current_city", currentCity);
				intent.putExtra("current_ccode", currentCcode);

				startActivity(intent);
				finish();
			}
		});

	}

	@Override
	public void onBackPressed()
	{
		//go back to the MainActivity
		Log.v(TAG, "onBackPressed");
		Intent intent = new Intent(MeanActivity.this, MainActivity.class);
		intent.putExtra("current_city", currentCity);
		intent.putExtra("current_ccode", currentCcode);

		startActivity(intent);

		super.onBackPressed();
	}

}
