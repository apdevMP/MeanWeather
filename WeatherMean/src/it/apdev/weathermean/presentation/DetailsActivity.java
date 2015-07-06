package it.apdev.weathermean.presentation;

import java.util.ArrayList;

import it.apdev.weathermean.R;
import it.apdev.weathermean.logic.Weather;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends Activity {

	private TextView tvSource1, tvSource2, tvSource3;
	private TextView tvDesc1, tvDesc2, tvDesc3;
	private TextView tvTemp1, tvTemp2, tvTemp3;
	private TextView tvHumidity1, tvHumidity2, tvHumidity3;
	private TextView tvPressure1, tvPressure2, tvPressure3;
	private TextView tvWind1, tvWind2, tvWind3;
	private ImageView imgView1, imgView2, imgView3;

	private static final String TAG = "DetailsActivity";

	private ArrayList<Weather> list;
	private Weather meanWeather;
	private String city, countryCode, currentCity, currentCcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity);

		Intent intent = getIntent();
		list = intent.getParcelableArrayListExtra("weather_list");
		meanWeather = intent.getParcelableExtra("weather_mean");
		city = intent.getStringExtra("city_name");
		getActionBar().setTitle(
				getString(R.string.details_action_bar_title) + " " + city);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.action_bar_color));		
		countryCode = intent.getStringExtra("country_code");
		currentCity = intent.getStringExtra("current_city");
		currentCcode = intent.getStringExtra("current_ccode");

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

		imgView1 = (ImageView) findViewById(R.id.imageView1);
		imgView2 = (ImageView) findViewById(R.id.imageView2);
		imgView3 = (ImageView) findViewById(R.id.imageView3);

		tvSource1.setText(list.get(0).getSource());
		tvSource2.setText(list.get(1).getSource());
		tvSource3.setText(list.get(2).getSource());

		tvDesc1.setText(list.get(0).getDescription());
		tvDesc2.setText(list.get(1).getDescription());
		tvDesc3.setText(list.get(2).getDescription());

		tvTemp1.setText(list.get(0).getTemperature() + " �C");
		tvTemp2.setText(list.get(1).getTemperature() + " �C");
		tvTemp3.setText(list.get(2).getTemperature() + " �C");

		tvHumidity1.setText(list.get(0).getHumidity() + "%");
		tvHumidity2.setText(list.get(1).getHumidity() + "%");
		tvHumidity3.setText(list.get(2).getHumidity() + "%");

		tvPressure1.setText(list.get(0).getPressure() + " hPa");
		tvPressure2.setText(list.get(1).getPressure() + " hPa");
		tvPressure3.setText(list.get(2).getPressure() + " hPa");

		tvWind1.setText(list.get(0).getWind() + " Km/h");
		tvWind2.setText(list.get(1).getWind() + " Km/h");
		tvWind3.setText(list.get(2).getWind() + " Km/h");

		imgView1.setImageResource(list.get(0).getIdIcon());
		Log.v(TAG, "IconId1: " + list.get(0).getIdIcon());
		imgView2.setImageResource(list.get(1).getIdIcon());
		Log.v(TAG, "IconId2: " + list.get(1).getIdIcon());
		imgView3.setImageResource(list.get(2).getIdIcon());
		Log.v(TAG, "IconId3: " + list.get(2).getIdIcon());

	}

	@Override
	public void onBackPressed() {

		Log.v(TAG, "OnBackPressed");
		Intent intent = new Intent(DetailsActivity.this, MeanActivity.class);

		intent.putParcelableArrayListExtra("weather_list", list);
		intent.putExtra("weather_mean", meanWeather);
		intent.putExtra("city_name", city);
		intent.putExtra("country_code", countryCode);
		intent.putExtra("current_city", currentCity);
		intent.putExtra("current_ccode", currentCcode);

		startActivity(intent);
		finish();
	}

}
