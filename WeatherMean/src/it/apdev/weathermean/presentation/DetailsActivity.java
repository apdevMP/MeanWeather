package it.apdev.weathermean.presentation;

import java.util.ArrayList;

import it.apdev.weathermean.R;
import it.apdev.weathermean.logic.Weather;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends Activity {

	private TextView tvSource1,tvSource2,tvSource3;
	private TextView tvDesc1,tvDesc2,tvDesc3;
	private TextView tvTemp1,tvTemp2,tvTemp3;
	private TextView tvHumidity1,tvHumidity2,tvHumidity3;
	private TextView tvPressure1,tvPressure2,tvPressure3;
	private TextView tvWind1,tvWind2,tvWind3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity);
		
		Intent intent = getIntent();
		ArrayList<Weather> weatherList = intent.getParcelableArrayListExtra("weather_list");
		
		tvSource1 = (TextView) findViewById(R.id.textViewSource1);
		tvSource2 = (TextView) findViewById(R.id.textViewSource2);
		tvSource3 = (TextView) findViewById(R.id.textViewSource3);
		
		tvDesc1 = (TextView) findViewById(R.id.textViewDescr1);
		tvDesc2 = (TextView) findViewById(R.id.textViewDescr2);
		tvDesc3 = (TextView) findViewById(R.id.textViewDescr3);
		
		tvTemp1 = (TextView) findViewById(R.id.textViewTemp1);
		tvTemp2 = (TextView) findViewById(R.id.textViewTemp2);
		tvTemp3 = (TextView) findViewById(R.id.textViewTemp3);
		
		tvHumidity1 = (TextView) findViewById(R.id.textViewHumidity1);
		tvHumidity2 = (TextView) findViewById(R.id.textViewHumidity2);
		tvHumidity3 = (TextView) findViewById(R.id.textViewHumidity3);
		
		tvPressure1 = (TextView) findViewById(R.id.textViewPressure1);
		tvPressure2 = (TextView) findViewById(R.id.textViewPressure2);
		tvPressure3 = (TextView) findViewById(R.id.textViewPressure3);

		tvWind1 = (TextView) findViewById(R.id.textViewWind1);
		tvWind2 = (TextView) findViewById(R.id.textViewWind2);
		tvWind3 = (TextView) findViewById(R.id.textViewWind3);
		
		tvSource1.setText(weatherList.get(0).getSource());
		tvSource2.setText(weatherList.get(1).getSource());
		tvSource3.setText(weatherList.get(2).getSource());
		
		tvDesc1.setText(weatherList.get(0).getDescription());
		tvDesc2.setText(weatherList.get(1).getDescription());
		tvDesc3.setText(weatherList.get(2).getDescription());
		
		tvTemp1.setText(weatherList.get(0).getTemperature()+" °C");
		tvTemp2.setText(weatherList.get(1).getTemperature()+" °C");
		tvTemp3.setText(weatherList.get(2).getTemperature()+" °C");
		
		tvHumidity1.setText(weatherList.get(0).getHumidity()+"%");
		tvHumidity2.setText(weatherList.get(1).getHumidity()+"%");
		tvHumidity3.setText(weatherList.get(2).getHumidity()+"%");
		
		tvPressure1.setText(weatherList.get(0).getPressure()+"");
		tvPressure2.setText(weatherList.get(1).getPressure()+"");
		tvPressure3.setText(weatherList.get(2).getPressure()+"");
		
		tvWind1.setText(weatherList.get(0).getWind()+" Km/h");
		tvWind2.setText(weatherList.get(1).getWind()+" Km/h");
		tvWind3.setText(weatherList.get(2).getWind()+" Km/h");
	}

}
