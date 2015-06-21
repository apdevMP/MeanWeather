package it.apdev.weathermean.presentation;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import it.apdev.weathermean.R;
import it.apdev.weathermean.logic.OpenWeatherMapHttpService;
import it.apdev.weathermean.logic.Weather;
import it.apdev.weathermean.logic.WorldWeatherOnlineHttpService;
import it.apdev.weathermean.logic.YahooHttpService;
import it.apdev.weathermean.storage.DBManager;
import it.apdev.weathermean.storage.DBStrings;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private EditText cityEditText, countryCodeEditText;
	private Button addCityButton;
	private ListView cityListView;
	private ImageButton deleteImageButton;

	private DBManager dbManager = null;
	private CursorAdapter adapter;

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		dbManager = new DBManager(this);
		dbManager.updateCurrentField();

		Intent intent = getIntent();
		String currentCityNameString = intent.getStringExtra("city_name");
		String currentCountryCodeString = intent.getStringExtra("country_code");

		// retrieve the views
		cityListView = (ListView) findViewById(R.id.listViewCities);
		cityEditText = (EditText) findViewById(R.id.editTextCity);
		countryCodeEditText = (EditText) findViewById(R.id.editTextCountry);
		addCityButton = (Button) findViewById(R.id.buttonAddCity);

		Cursor crs = dbManager.query();
		adapter = new CursorAdapter(this, crs, 0) {
			@Override
			public View newView(Context ctx, Cursor arg1, ViewGroup arg2) {
				View v = getLayoutInflater().inflate(R.layout.list_item, null);
				return v;
			}

			@Override
			public void bindView(View v, Context arg1, Cursor crs) {
				final String cityNameString = crs.getString(crs
						.getColumnIndex(DBStrings.FIELD_CITY));
				final String countryCodeString = crs.getString(crs
						.getColumnIndex(DBStrings.FIELD_COUNTRY));
				Integer isCurrentLocationInteger = crs.getInt(crs
						.getColumnIndex(DBStrings.FIELD_CURRENT));
				TextView cityTextView = (TextView) v
						.findViewById(R.id.textViewCity);
				cityTextView.setText(cityNameString);
				TextView currentTextView = (TextView) v
						.findViewById(R.id.textViewCurrentLocation);
				currentTextView
						.setText(isCurrentLocationInteger == 1 ? getString(R.string.current_location)
								: " ");
				TextView countryTextView = (TextView) v
						.findViewById(R.id.textViewCountry);
				countryTextView.setText(countryCodeString);

				deleteImageButton = (ImageButton) v
						.findViewById(R.id.imageButtonDelete);
				deleteImageButton
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								Log.v(TAG, "Deleting record");
								if (dbManager.delete(cityNameString,
										countryCodeString))
									adapter.changeCursor(dbManager.query());

							}
						});

			}

			@Override
			public long getItemId(int position) {
				Cursor crs = adapter.getCursor();
				crs.moveToPosition(position);
				return crs.getLong(crs.getColumnIndex(DBStrings.FIELD_ID));
			}

		};

		cityListView.setAdapter(adapter);
		addRecordToDB(currentCityNameString, currentCountryCodeString, 1, false);
		addRecordToDB("Milano", "IT", 0, false);
		addRecordToDB("Torino", "IT", 0, false);
		addRecordToDB("Bari", "IT", 0, false);
		addRecordToDB("Veroli", "IT", 0, false);
		addRecordToDB("Ripi", "IT", 0, false);

		// listener addCityButton
		addCityButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String cityString = cityEditText.getText().toString();
				String countryCodeString = countryCodeEditText.getText()
						.toString();
				Log.v(TAG, "length:" + countryCodeString.length());

				// verify that the edittext has been filled
				if (cityString.length() > 0 && countryCodeString.length() > 0) {
					// add city to db
					if (countryCodeString.length() == 2) {
						addRecordToDB(cityString, countryCodeString, 0, true);
					} else {
						Toast.makeText(MainActivity.this,
								R.string.warning_edit_contry_length,
								Toast.LENGTH_LONG).show();

					}
				} else {

					// display a toast to the user
					Toast.makeText(MainActivity.this,
							R.string.warning_edit_text, Toast.LENGTH_LONG)
							.show();
				}

			}
		});

		cityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.v(TAG, "Start onItemClick");
				TextView tvCity = (TextView) view
						.findViewById(R.id.textViewCity);
				String city = (String) tvCity.getText();
				TextView tvCountryCode = (TextView) view
						.findViewById(R.id.textViewCountry);
				String countryCode = (String) tvCountryCode.getText();

				ArrayList<Weather> list = startServices(city.trim(),
						countryCode.trim());
				Log.v(TAG, "" + list.size());
				if (list.size() == 3) {
					Weather meanWeather = new Weather();
					meanWeather.mergeWeather(list);

					Intent intent = new Intent(MainActivity.this,
							MeanActivity.class);
					intent.putExtra("city_name", city.trim());
					intent.putExtra("country_code", countryCode.trim());
					intent.putParcelableArrayListExtra("weather_list", list);
					intent.putExtra("weather_mean", meanWeather);

					startActivity(intent);
					finish();
				}
				else {
					Toast.makeText(MainActivity.this, "It's impossible to retrieve all weathers from websites", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public void addRecordToDB(String cityName, String countryCode,
			Integer isCurrent, boolean debug) {

		if (!(dbManager.isAlreadyPresent(cityName, countryCode, isCurrent))) {

			Log.v(TAG, "Adding record: " + cityName + " " + countryCode + " "
					+ isCurrent);
			dbManager.save(cityName, countryCode, isCurrent);

		}

		else {
			if (debug)
				Toast.makeText(MainActivity.this,
						R.string.toast_already_present, Toast.LENGTH_LONG)
						.show();
			System.out.println(cityName + " già presente");
		}

		adapter.changeCursor(dbManager.query());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.ActionBarActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		System.exit(0);
		super.onBackPressed();
	}

	/**
	 * 
	 * @param city
	 * @param codeNation
	 * @return
	 */
	private ArrayList<Weather> startServices(String city, String codeNation) {

		Log.v(TAG, "Start Services for" + city + "," + codeNation);
		ArrayList<Weather> list = new ArrayList<Weather>();

		YahooHttpService yahooService = new YahooHttpService(city, codeNation, MainActivity.this);
		OpenWeatherMapHttpService openWeatherService = new OpenWeatherMapHttpService(city, codeNation, MainActivity.this);
		WorldWeatherOnlineHttpService worldWeatherService = new WorldWeatherOnlineHttpService(city, codeNation, MainActivity.this);


		try {
			Weather fromYahoo = yahooService.retrieveWeather();
			if (fromYahoo != null) {
				list.add(fromYahoo);
			}
			Weather fromOpenWeather = openWeatherService.retrieveWeather();
			if (fromOpenWeather != null) {
				list.add(fromOpenWeather);
			}
			Weather worldWeather = worldWeatherService.retrieveWeather();
			if (worldWeather != null) {
				list.add(worldWeather);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
}
