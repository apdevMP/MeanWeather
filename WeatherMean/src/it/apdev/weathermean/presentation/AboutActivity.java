/**
 * 
 */
package it.apdev.weathermean.presentation;

import it.apdev.weathermean.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * @author TEAM apdev
 * 
 * Shows some information about the developers and the version of the application.
 *
 */
public class AboutActivity extends Activity
{
	private static final String	TAG	= "AboutActivity";
	private String				currentCityNameString;
	private String				currentCountryCodeString;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");

		setContentView(R.layout.about_activity);

		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.action_bar_color));
		
		// get the extras from intent to pass them back to the MainActivity
		Intent intent = getIntent();
		currentCityNameString = intent.getStringExtra("current_city");
		currentCountryCodeString = intent.getStringExtra("current_ccode");
	}

	@Override
	public void onBackPressed()
	{
		// go to the MainActivity
		Intent intent = new Intent(AboutActivity.this, MainActivity.class);
		intent.putExtra("current_city", currentCityNameString);
		intent.putExtra("current_ccode", currentCountryCodeString);
		startActivity(intent);
		finish();
	}

}
