package it.apdev.weathermean.presentation;

import it.apdev.weathermean.R;
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

public class MainActivity extends ActionBarActivity
{

	private EditText			cityEditText, countryCodeEditText;
	private Button				addCityButton;
	private ListView			cityListView;
	private ImageButton			deleteImageButton;

	private DBManager			dbManager	= null;
	private CursorAdapter		adapter;

	private static final String	TAG			= "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}

		dbManager = new DBManager(this);
		dbManager.updateCurrentField();

		Intent intent = getIntent();
		String currentCityNameString = intent.getStringExtra("city_name");
		String currentCountryCodeString = intent.getStringExtra("country_code");

		//retrieve the views 
		cityListView = (ListView) findViewById(R.id.listViewCities);
		cityEditText = (EditText) findViewById(R.id.editTextCity);
		countryCodeEditText = (EditText) findViewById(R.id.editTextCountry);
		addCityButton = (Button) findViewById(R.id.buttonAddCity);

		Cursor crs = dbManager.query();
		adapter = new CursorAdapter(this, crs, 0) {
			@Override
			public View newView(Context ctx, Cursor arg1, ViewGroup arg2)
			{
				View v = getLayoutInflater().inflate(R.layout.list_item, null);
				return v;
			}

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
				deleteImageButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						Log.v(TAG, "Deleting record");
//						int position = cityListView.getPositionForView(v);
//						TextView tvCity = (TextView) v.findViewById(R.id.textViewCity);
//						String city = (String) tvCity.getText();
//						TextView tvCountryCode = (TextView) v.findViewById(R.id.textViewCountry);
//						String countryCode = (String) tvCountryCode.getText();
//						long id = adapter.getItemId(position);
						if (dbManager.delete(cityNameString, countryCodeString))
							adapter.changeCursor(dbManager.query());

					}
				});

			}

			@Override
			public long getItemId(int position)
			{
				Cursor crs = adapter.getCursor();
				crs.moveToPosition(position);
				return crs.getLong(crs.getColumnIndex(DBStrings.FIELD_ID));
			}
			
			
		};

		cityListView.setAdapter(adapter);
		addRecordToDB(currentCityNameString, currentCountryCodeString, 1);
		addRecordToDB("Milano", "IT", 0);
		addRecordToDB("Torino", "IT", 0);
		addRecordToDB("Bari", "IT", 0);
		addRecordToDB("Veroli", "IT", 0);

		//listener addCityButton 
		addCityButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				String cityString = cityEditText.getText().toString();
				String countryCodeString = countryCodeEditText.getText().toString();
				Log.v(TAG, "length:" + countryCodeString.length());

				//verify that the edittext have been filled
				if (cityString.length() > 0 && countryCodeString.length() > 0)
				{
					//add city to db
					if (countryCodeString.length() == 2)
					{
						addRecordToDB(cityString, countryCodeString, 0);
					} else
					{
						Toast.makeText(MainActivity.this, R.string.warning_edit_contry_length, Toast.LENGTH_LONG).show();
					}
				} else
				{

					//display a toast to the user
					Toast.makeText(MainActivity.this, R.string.warning_edit_text, Toast.LENGTH_LONG).show();
				}

			}
		});

		cityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Log.v(TAG, "Start onItemClick");
				TextView tvCity = (TextView) view.findViewById(R.id.textViewCity);
				String city = (String) tvCity.getText();
				TextView tvCountryCode = (TextView) view.findViewById(R.id.textViewCountry);
				String countryCode = (String) tvCountryCode.getText();

				Intent intent = new Intent(MainActivity.this, MeanActivity.class);
				intent.putExtra("city_name", city.trim());
				intent.putExtra("country_code", countryCode.trim());

				startActivity(intent);
				finish();

			}
		});
	}

	public void addRecordToDB(String cityName, String countryCode, Integer isCurrent)
	{

		if (!(dbManager.isDuplicate(cityName, countryCode, isCurrent)))
		{

			Log.v(TAG, "Adding record: "+cityName+" "+countryCode+" "+isCurrent);
			dbManager.save(cityName, countryCode, isCurrent);
			adapter.changeCursor(dbManager.query());
		}
		else System.out.println(cityName +" già presente");
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment
	{

		public PlaceholderFragment()
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.ActionBarActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		finish();
		System.exit(0);
		super.onBackPressed();
	}
}
