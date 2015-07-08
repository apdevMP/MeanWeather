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
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

/**
 * @author TEAM apdev
 * 
 * Shows the list of the cities contained within the database. It also allows
 * the user to delete the cities from the DB and to add new ones. In the list
 * appears also the current location. Clicking on an item of the list, it is
 * possible to see the mean weather forecast for that city.
 *
 */
public class MainActivity extends Activity
{

	private EditText			cityEditText, countryCodeEditText;
	private Button				addCityButton;
	private ListView			cityListView;
	private ImageButton			deleteImageButton;

	private DBManager			dbManager	= null;
	private CursorAdapter		adapter;
	private String				currentCityNameString;
	private String				currentCountryCodeString;
	private ProgressDialog		progressDialog;

	private static final String	TAG			= "MainActivity";
	private static final int N_PROVIDER = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");

		setContentView(R.layout.activity_main);

		//set the color of the action bar
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.action_bar_color));

		// create an instance for the database manager
		dbManager = new DBManager(this);

		//TODO
		dbManager.updateCurrentField();

		// get extras from the intent
		Intent intent = getIntent();
		currentCityNameString = intent.getStringExtra("current_city");
		currentCountryCodeString = intent.getStringExtra("current_ccode");

		// retrieve the views
		cityListView = (ListView) findViewById(R.id.listViewCities);
		cityEditText = (EditText) findViewById(R.id.editTextCity);
		countryCodeEditText = (EditText) findViewById(R.id.editTextCountry);
		addCityButton = (Button) findViewById(R.id.buttonAddCity);

		// populate the city list retrieving cities from the database
		Cursor crs = dbManager.getCityList();
		adapter = new CursorAdapter(this, crs, 0) {
			@Override
			public View newView(Context ctx, Cursor arg1, ViewGroup arg2)
			{
				// set the view
				View v = getLayoutInflater().inflate(R.layout.list_item, null);
				return v;
			}

			/*
			 * binds the view for the single item of the list
			 */
			
			@Override
			public void bindView(View v, Context arg1, Cursor crs)
			{
				final String cityNameString = crs.getString(crs.getColumnIndex(DBStrings.FIELD_CITY));
				final String countryCodeString = crs.getString(crs.getColumnIndex(DBStrings.FIELD_COUNTRY));
				Integer isCurrentLocationInteger = crs.getInt(crs.getColumnIndex(DBStrings.FIELD_CURRENT));
				TextView cityTextView = (TextView) v.findViewById(R.id.textViewCity);
				cityTextView.setText(cityNameString);
				TextView currentTextView = (TextView) v.findViewById(R.id.textViewCurrentLocation);
				currentTextView.setText(isCurrentLocationInteger == 1 ? getString(R.string.current_location) : " ");
				TextView countryTextView = (TextView) v.findViewById(R.id.textViewCountry);
				countryTextView.setText(countryCodeString);

				deleteImageButton = (ImageButton) v.findViewById(R.id.imageButtonDelete);

				// listener for the deleting button
				deleteImageButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v)
					{
						Log.v(TAG, "Deleting record");

						//delete the selected city from the database and updates the displayed list 
						if (dbManager.delete(cityNameString, countryCodeString))
							adapter.changeCursor(dbManager.getCityList());

					}
				});

			}

		};

		//set the adapter for the list, to show all the cities stored in the database
		cityListView.setAdapter(adapter);

		//add some test cities to the list, including the current one
		addRecordToDB(currentCityNameString, currentCountryCodeString, 1, false);
		addRecordToDB("Milano", "IT", 0, false);
		addRecordToDB("Torino", "IT", 0, false);
		addRecordToDB("Bari", "IT", 0, false);
		addRecordToDB("Veroli", "IT", 0, false);
		addRecordToDB("Ripi", "IT", 0, false);

		// listener addCityButton, to add a new city in the database
		addCityButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				String cityString = cityEditText.getText().toString();
				String countryCodeString = countryCodeEditText.getText().toString();

				/*
				 * verify that the edittext has been filled. If not, show a
				 * toast to the user with a warning message
				 */
				if (cityString.length() > 0 && countryCodeString.length() > 0)
				{
					/*
					 * check if the country code lenght is 2 and add the new
					 * city to the db; otherwise show a warning toast to the
					 * user
					 */
					if (countryCodeString.length() == 2)
					{
						addRecordToDB(cityString, countryCodeString, 0, true);
					} else
					{
						Toast.makeText(MainActivity.this, R.string.warning_edit_contry_length, Toast.LENGTH_LONG).show();

					}
				} else
				{
					Toast.makeText(MainActivity.this, R.string.warning_edit_text, Toast.LENGTH_LONG).show();
				}

			}
		});

		// listener for the listview of the cities
		cityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{

				Log.v(TAG, "onItemClick");
				TextView tvCity = (TextView) view.findViewById(R.id.textViewCity);
				final String city = (String) tvCity.getText();
				TextView tvCountryCode = (TextView) view.findViewById(R.id.textViewCountry);
				final String countryCode = (String) tvCountryCode.getText();

				/*
				 * check if internet connection is available. If yes, start
				 * retrieving the information from the data sources, otherwise
				 * show an alert dialog to the user and wait for the enabling of
				 * the network connection
				 */
				if (isNetworkAvailable())
				{

					//show a progress dialog while connecting and parsing
					progressDialog = createProgressDialog(getString(R.string.progress_msg));

					new Thread(new Runnable() {

						@Override
						public void run()
						{

							/*
							 * create a list of Weather objects starting
							 * services by the city name and the country code,
							 * necessary for the APIs
							 */
							ArrayList<Weather> list = startServices(city.trim(), countryCode.trim());

							/*
							 * if the list has 3 Weather objects, the retrieval
							 * has been successful and it is created a new
							 * Weather object with the mean of the information;
							 * otherwise, in case of connection problems or
							 * parsing problems, a warning toast is shown to the
							 * user
							 */
							if (list.size() == N_PROVIDER)
							{
								Weather meanWeather = new Weather();
								meanWeather.mergeWeather(list);

								//go to the MeanActivity
								Intent intent = new Intent(MainActivity.this, MeanActivity.class);
								intent.putExtra("city_name", city.trim());
								intent.putExtra("country_code", countryCode.trim());
								intent.putExtra("current_city", currentCityNameString);
								intent.putExtra("current_ccode", currentCountryCodeString);
								intent.putParcelableArrayListExtra("weather_list", list);
								intent.putExtra("weather_mean", meanWeather);

								progressDialog.cancel();
								startActivity(intent);
								finish();
							} else
							{
								runOnUiThread(new Runnable() {

									@Override
									public void run()
									{

										Toast.makeText(MainActivity.this, getString(R.string.json_error), Toast.LENGTH_LONG).show();
										progressDialog.cancel();
									}
								});
							}
						}
					}).start();
				} else
				{
					new AlertDialog.Builder(MainActivity.this).setTitle(R.string.alert_dialog_title).setMessage(R.string.network_message)
							.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which)
								{
									dialog.cancel();

								}
							}).setIcon(android.R.drawable.ic_dialog_alert).show();
				}
			}
		});
	}

	/**
	 * Add a new city to the database if it is not already present.
	 * 
	 * @param cityName the name of the city
	 * @param countryCode the code of its nation
	 * @param isCurrent 1 if the city is the current city
	 * @param debug 1 for new cities added
	 */
	public void addRecordToDB(String cityName, String countryCode, Integer isCurrent, boolean debug)
	{

		/*
		 * check if the city is already present in the DB. If it is not, save it
		 * within the db, otherwise show a warning toast to the user
		 */
		if (!(dbManager.isAlreadyPresent(cityName, countryCode, isCurrent)))
		{
			Log.v(TAG, "Adding record: " + cityName + " " + countryCode + " " + isCurrent);
			dbManager.save(cityName, countryCode, isCurrent);
		} else
		{
			if (debug)
				Toast.makeText(MainActivity.this, R.string.toast_already_present, Toast.LENGTH_LONG).show();
		}

		// update the adapter to show the eventual new city in the list
		adapter.changeCursor(dbManager.getCityList());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		/*
		 * the menu contains only one item, which allows the user to go to the
		 * AboutActivity, where some information about the application is
		 * displayed.
		 */
		int id = item.getItemId();
		if (id == R.id.action_about)
		{

			Intent intent = new Intent(MainActivity.this, AboutActivity.class);
			intent.putExtra("current_city", currentCityNameString);
			intent.putExtra("current_ccode", currentCountryCodeString);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed()
	{
		//close the application
		finish();
		System.exit(0);
		super.onBackPressed();
	}

	/**
	 * Start the connection to the providers, based on the city and the country
	 * code of the item selected, in order to retrieve the weather forecast fot
	 * it.
	 * 
	 * @param city
	 * @param codeNation
	 * @return a list of Weather objects
	 */
	private ArrayList<Weather> startServices(String city, String codeNation)
	{

		Log.v(TAG, "Starting Services for " + city + "," + codeNation);
		ArrayList<Weather> list = new ArrayList<Weather>();

		//create the instances for the providers
		YahooHttpService yahooService = new YahooHttpService(city, codeNation);
		OpenWeatherMapHttpService openWeatherService = new OpenWeatherMapHttpService(city, codeNation);
		WorldWeatherOnlineHttpService worldWeatherService = new WorldWeatherOnlineHttpService(city, codeNation);

		/*
		 * start the retrieval of the information, adding the new object to the
		 * list only if the connection has been succesful
		 */
		try
		{
			Weather fromYahoo = yahooService.retrieveWeather();
			if (fromYahoo != null)
			{
				list.add(fromYahoo);
			}
			Weather fromOpenWeather = openWeatherService.retrieveWeather();
			if (fromOpenWeather != null)
			{
				list.add(fromOpenWeather);
			}
			Weather worldWeather = worldWeatherService.retrieveWeather();
			if (worldWeather != null)
			{
				list.add(worldWeather);
			}
		} catch (InterruptedException e)
		{
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (ExecutionException e)
		{
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (JSONException e)
		{
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * Creates a new custom progress dialog
	 * 
	 * @param msg the message to be shown in the dialog
	 * @return
	 */
	private ProgressDialog createProgressDialog(String msg)
	{

		ProgressDialog pd = new ProgressDialog(MainActivity.this);

		pd.requestWindowFeature(Window.FEATURE_PROGRESS);
		pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		pd.show();

		//set the custom layout for the dialog
		pd.setContentView(R.layout.custom_pd);
		pd.setTitle(null);
		TextView text = (TextView) pd.findViewById(R.id.progress_msg);
		text.setText(msg);
		pd.setIndeterminate(true);
		pd.setCancelable(false);

		return pd;
	}

	/**
	 * Checks if the network connection is available
	 * 
	 * @return true if the connection is available
	 */
	private boolean isNetworkAvailable()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
