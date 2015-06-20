package it.apdev.weathermean.presentation;

import java.util.ArrayList;

import it.apdev.weathermean.R;
import it.apdev.weathermean.logic.ForecastMapper;
import it.apdev.weathermean.logic.Weather;
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
public class MeanActivity extends Activity {

	private static final int ADD_DRAWABLE_ID = 2130837503;
	private static final String	TAG	= "MeanActivity";
	
	
	private String				city, codeNation;
	private TextView			tvCity, tvTemp, tvForecast, tvWind, tvHumidity, tvPressure;
	private Button				btnDetails;
	private ImageView           ivMean;
	
	private Weather meanWeather;
	private ArrayList<Weather> list;
	
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
		ivMean = (ImageView) findViewById(R.id.imageViewWeather);
		
		Intent intent = getIntent();
		city = intent.getStringExtra("city_name");
		codeNation = intent.getStringExtra("country_code");
		list = intent.getParcelableArrayListExtra("weather_list");
		meanWeather = intent.getParcelableExtra("weather_mean");
		
		tvCity.setText(city + "," + codeNation);
		tvTemp.setText(meanWeather.getTemperature() + " °C");
		tvHumidity.setText(meanWeather.getHumidity() + "%");
		tvWind.setText(meanWeather.getWind() + " km/h");
		tvPressure.setText(meanWeather.getPressure() + " hPa");
		tvForecast.setText(meanWeather.getDescription());
		int code = meanWeather.getForecastCode() + ADD_DRAWABLE_ID;
		
		Log.v(TAG, "Sunny: "+R.drawable.aaa_sunny );
		Log.v(TAG, "Cloudy: "+R.drawable.aab_cloudy );
		Log.v(TAG, "Rain: "+R.drawable.aac_rain );
		Log.v(TAG, "Storm: "+R.drawable.aad_rain );
		Log.v(TAG, "Snow: "+R.drawable.aaf_snow );
		
		ivMean.setImageResource(code);
		
		btnDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MeanActivity.this, DetailsActivity.class);
				intent.putParcelableArrayListExtra("weather_list", list);
				intent.putExtra("weather_mean", meanWeather);
				intent.putExtra("city_name", city);
				intent.putExtra("country_code", codeNation);
				
				startActivity(intent);
				finish();
			}
		});

	}



}
