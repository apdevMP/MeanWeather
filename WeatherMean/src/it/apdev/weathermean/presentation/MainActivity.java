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
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity
{

	private EditText		cityEditText;
	private ListView		cityListView;
	private ImageButton		deleteImageButton;

	private DBManager		dbManager	= null;
	private CursorAdapter	adapter;

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
		Log.v("dmanager", "="+dbManager);
		
		
		Intent intent = getIntent();
		String currentCityNameString = intent.getStringExtra("city_name");
		String currentCountryCodeString = intent.getStringExtra("country_code");

		//retrieve the views 
		cityListView = (ListView) findViewById(R.id.listViewCities);
		
		
		//stop location manager
	//	SplashScreenActivity.locationManager.removeUpdates(SplashScreenActivity.locationListener);
	//	SplashScreenActivity.locationManager = null;
		
		

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
				String cityNameString = crs.getString(crs.getColumnIndex(DBStrings.FIELD_CITY));
				String countryCodeString = crs.getString(crs.getColumnIndex(DBStrings.FIELD_COUNTRY));
				TextView cityTextView = (TextView) v.findViewById(R.id.textViewCity);
				cityTextView.setText(cityNameString);
				TextView countryTextView = (TextView) v.findViewById(R.id.textViewCountry);
				countryTextView.setText(countryCodeString);

				deleteImageButton = (ImageButton) v.findViewById(R.id.imageButtonDelete);
				deleteImageButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						int position = cityListView.getPositionForView(v);
						long id = adapter.getItemId(position);
						if (dbManager.delete(id))
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
		addRecordToDB(currentCityNameString, currentCountryCodeString);
		addRecordToDB("Rome", "IT");

	}

	public void addRecordToDB(String cityName, String countryCode)
	{

		dbManager.save(cityName, countryCode);
		adapter.changeCursor(dbManager.query());
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
}
