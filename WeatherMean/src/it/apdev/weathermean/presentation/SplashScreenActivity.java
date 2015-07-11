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
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * @author TEAM apdev
 * 
 * First activity to be shown. It displays the name of the application and the
 * logo. Using the location manager, it also try to retrieve the current
 * location, in order to add the current city to the list of available choices.
 * 
 */
public class SplashScreenActivity extends Activity implements LocationListener
{

	static LocationManager		locationManager;
	static LocationListener		locationListener;
	private int					attemps;

	private static final String	TAG	= "SplashScreenActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// hide the ation bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		/*
		 * the retrieval of the current location takes a number of maximum two
		 * attemps. If after two attemps the current location has not been
		 * retrieved, the application will be closed.
		 */
		attemps = 0;

		/*
		 * if the network connection is available, try to get the current
		 * location, else show an alert dialog to the user and wait for the
		 * connection to be enabled
		 */
		if (isNetworkAvailable())
		{
			/*
			 * try to retrieve the position using the network provider
			 */
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

		} else
		{
			// show an alert dialog to the user
			new AlertDialog.Builder(this).setTitle(R.string.alert_dialog_title).setMessage(R.string.network_message)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.cancel();

							// create an intent filter to detect changes in network connection
							IntentFilter intentFilter = new IntentFilter();
							intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
							intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
							registerReceiver(broadcastReceiver, intentFilter);

						}
					}).setIcon(android.R.drawable.ic_dialog_alert).show();

		}

	}

	@Override
	public void onLocationChanged(Location location)
	{
		Log.v(TAG, "onLocationChanged");

		/*
		 * if the maximum number of attemps has not been already reached, try to
		 * get the coordinates of the current location
		 */
		if (location != null && attemps < 2)
		{
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			Log.v(TAG, "latitude: " + latitude + ",longitude: " + longitude);

			//increment the number of attemps
			attemps++;

			/*
			 * Starting from the coordinates retrieved, get the address of the
			 * position. if it is not possible to associate an address to the
			 * coordinates, show a toast to the user
			 */
			List<Address> addresses = null;
			try
			{
				Geocoder gcd = new Geocoder(SplashScreenActivity.this, Locale.getDefault());
				addresses = gcd.getFromLocation(latitude, longitude, 1);
			} catch (IOException e)
			{
				if (e.getMessage().equalsIgnoreCase("Service not Available"))
					Toast.makeText(SplashScreenActivity.this, getString(R.string.service_not_available), Toast.LENGTH_LONG).show();
				else
					Toast.makeText(SplashScreenActivity.this, getString(R.string.io_exception), Toast.LENGTH_LONG).show();

				e.printStackTrace();
			} catch (NullPointerException e)
			{
				Toast.makeText(SplashScreenActivity.this, getString(R.string.null_position), Toast.LENGTH_LONG).show();
			}
			/*
			 * If the list of the addresses associated to the coordinates is not
			 * empty, extract the city name and the country code from it. Then
			 * stop the update of the location manager, and go to the next
			 * activity.
			 */

			if (addresses != null && addresses.size() > 0)
			{
				String city_name = addresses.get(0).getLocality();
				String country_code = addresses.get(0).getCountryCode();
				Log.v(TAG, city_name + ", stato: " + country_code);

				//stop controlling the position
				locationManager.removeUpdates(this);

				//go to the actiivty with the list of the cities
				Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
				intent.putExtra("current_city", city_name.trim());
				intent.putExtra("current_ccode", country_code.trim());

				startActivity(intent);
				finish();

			}
		}

		/*
		 * max number of attemps reached: show an alert dialog to the user and
		 * close the application
		 */
		else
		{
			Log.v(TAG, "exit");

			new AlertDialog.Builder(this).setTitle(R.string.alert_dialog_title).setMessage(getString(R.string.max_attemps))
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which)
						{

							// kill the application
							finish();
							System.exit(0);

						}
					}).setIcon(android.R.drawable.ic_dialog_alert).show();
		}

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		Log.v(TAG, "onStatusChanged");
	}

	@Override
	public void onProviderEnabled(String provider)
	{
		Log.v(TAG, "onProviderEnabled");
	}

	@Override
	public void onProviderDisabled(String provider)
	{
		Log.v(TAG, "onProviderDisabled");

		/*
		 * if the location provider is not enabled, show an alert dialog to the
		 * user and close the application to allow him to enable it
		 */
		new AlertDialog.Builder(this).setTitle(R.string.alert_dialog_title).setMessage(R.string.alert_message)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which)
					{

						// kill the application
						finish();
						System.exit(0);

					}
				}).setIcon(android.R.drawable.ic_dialog_alert).show();
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

	/*
	 * create a new broadcast receiver to detect the changes in network connection
	 * 
	 */
	final BroadcastReceiver	broadcastReceiver	= new BroadcastReceiver() {

													@Override
													public void onReceive(Context context, Intent intent)
													{
														if (isNetworkAvailable())
														{

															/*
															 * once the internet
															 * connection has
															 * been enabled,
															 * restart the
															 * update of the
															 * location manager
															 */
															locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
															locationManager
																	.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, SplashScreenActivity.this);
														}

													}
												};

}
