package it.apdev.weathermean.presentation;

import java.util.ArrayList;

import it.apdev.weathermean.R;
import it.apdev.weathermean.logic.ForecastMapper;
import it.apdev.weathermean.logic.Weather;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Andrea
 *
 */

public class MeanActivity extends Activity
{

	private static final String	TAG	= "MeanActivity";

	private String				city, codeNation;
	private TextView			tvCity, tvTemp, tvForecast, tvWind, tvHumidity, tvPressure;
	private Button				btnDetails;

	private ImageView			imgViewIcon;

	private Weather				meanWeather;
	private ArrayList<Weather>	list;

	private String				currentCity;

	private String				currentCcode;

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
		btnDetails = (Button) findViewById(R.id.buttonDetails);

		imgViewIcon = (ImageView) findViewById(R.id.imageViewWeather);

		Intent intent = getIntent();
		city = intent.getStringExtra("city_name");
		getActionBar().setTitle(getString(R.string.mean_action_bar_title) + " " + city);
		codeNation = intent.getStringExtra("country_code");
		list = intent.getParcelableArrayListExtra("weather_list");
		meanWeather = intent.getParcelableExtra("weather_mean");

		currentCity = intent.getStringExtra("current_city");
		currentCcode = intent.getStringExtra("current_ccode");

		tvCity.setText(city + "," + codeNation);
		tvTemp.setText(meanWeather.getTemperature() + " �C");
		tvHumidity.setText(meanWeather.getHumidity() + "%");
		tvWind.setText(meanWeather.getWind() + " km/h");
		tvPressure.setText(meanWeather.getPressure() + " hPa");
		tvForecast.setText(meanWeather.getDescription());

		String meanForecastDescription = meanWeather.getDescription();
		tvForecast.setText(meanForecastDescription);

		//retrieve the right icon based on the forecast description
		imgViewIcon.setImageDrawable(retrieveIcon(meanWeather.getForecastCode()));

		btnDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
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
		Log.v(TAG, "onBackPressed");
		Intent intent = new Intent(MeanActivity.this, MainActivity.class);
		intent.putExtra("current_city", currentCity);
		intent.putExtra("current_ccode", currentCcode);

		Log.v(TAG, "current city:" + currentCity);
		Log.v(TAG, "current ccode:" + currentCcode);

		startActivity(intent);

		super.onBackPressed();
	}

	private Drawable retrieveIcon(int forecastCode)
	{
		
		if (forecastCode==4)
			return MeanActivity.this.getResources().getDrawable(R.drawable.storm);
		else if (forecastCode==1)
			return MeanActivity.this.getResources().getDrawable(R.drawable.sunny);
		else if (forecastCode==2)
			return MeanActivity.this.getResources().getDrawable(R.drawable.cloudy);
		else if (forecastCode==3)
			return MeanActivity.this.getResources().getDrawable(R.drawable.rain);
		else if (forecastCode==5)
			return MeanActivity.this.getResources().getDrawable(R.drawable.snow);
		else
			return MeanActivity.this.getResources().getDrawable(R.drawable.not_available);
	}

}
