/**
 * La classe SplashScreenActivity serve a gestire l'activity dello SplashScreen che recupera i dati 
 * della posizione attuale dalla quale calcolare il meteo e recupera i dati delle citt� salavate nel db delle preferenze
 */
package it.apdev.weathermean.presentation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import it.apdev.weathermean.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Vanessa
 * 
 */
public class SplashScreenActivity extends Activity implements LocationListener
{

	static LocationManager		locationManager;
	static LocationListener		locationListener;
	private Context				context;

	private static final String	TAG	= "SplashScreenActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Permette di eliminare la barra superiore 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		//retrieving of current position
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

	}

	@Override
	public void onLocationChanged(Location location)
	{
		// TODO Auto-generated method stub
		if (location != null)
		{
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			Log.v(TAG, "latitude: " + latitude + ",longitude: " + longitude);

			Geocoder gcd = new Geocoder(SplashScreenActivity.this, Locale.getDefault());
			List<Address> addresses = null;
			try
			{
				addresses = gcd.getFromLocation(latitude, longitude, 1);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (addresses != null && addresses.size() > 0)
			{
				String city_name = addresses.get(0).getLocality();
				String country_code = addresses.get(0).getCountryCode();
				Log.v(TAG, city_name + ", stato: " + country_code );
				locationManager.removeUpdates(this);
				Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
				intent.putExtra("city_name", city_name.trim());
				intent.putExtra("country_code", country_code.trim());
				startActivity(intent);

				//	SplashScreenActivity.locationManager.removeUpdates(SplashScreenActivity.locationListener);
				//insert the current city in the database
			}
		}

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "onStatusChanged");

	}

	@Override
	public void onProviderEnabled(String provider)
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "onProviderEnabled");
	}

	@Override
	public void onProviderDisabled(String provider)
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "onProviderDisabled");
	}

}
