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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Vanessa
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
		// Permette di eliminare la barra superiore
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		attemps = 0;

		// retrieving of current position
		if (isNetworkAvailable())
		{
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

							// create an intent filter to detect changes
							// in network connection
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

		if (location != null && attemps < 1)
		{
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			Log.v(TAG, "latitude: " + latitude + ",longitude: " + longitude);

			attemps++;
			Log.v(TAG, "attemps vale " + attemps);

			List<Address> addresses = null;
			try
			{
				Geocoder gcd = new Geocoder(SplashScreenActivity.this, Locale.getDefault());
				addresses = gcd.getFromLocation(latitude, longitude, 1);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				Toast.makeText(SplashScreenActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (NullPointerException e)
			{
				Toast.makeText(SplashScreenActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
			if (addresses != null && addresses.size() > 0)
			{
				String city_name = addresses.get(0).getLocality();
				String country_code = addresses.get(0).getCountryCode();
				Log.v(TAG, city_name + ", stato: " + country_code);
				locationManager.removeUpdates(this);
				Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
				intent.putExtra("current_city", city_name.trim());
				intent.putExtra("current_ccode", country_code.trim());
				startActivity(intent);
				finish();

				// SplashScreenActivity.locationManager.removeUpdates(SplashScreenActivity.locationListener);
				// insert the current city in the database
			}
		} else
		{
			Log.v(TAG, "exit");

			new AlertDialog.Builder(this).setTitle(R.string.alert_dialog_title)
					.setMessage("Max number of attemps reached.Please control your connection settings")
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
		// TODO aggiungere visualizzazione alert dialog
		Log.v(TAG, "onProviderDisabled");

		// show an alert dialog to the user
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

	private boolean isNetworkAvailable()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	final BroadcastReceiver	broadcastReceiver	= new BroadcastReceiver() {

													@Override
													public void onReceive(Context context, Intent intent)
													{
														// TODO Auto-generated method stub
														if (isNetworkAvailable())
														{
															// Toast.makeText(context, "Network Available Do operations",
															// Toast.LENGTH_LONG).show();
															locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
															locationManager
																	.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, SplashScreenActivity.this);
														}

													}
												};

}
