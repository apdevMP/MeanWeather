/**
 * 
 */
package it.apdev.weathermean.presentation;

import it.apdev.weathermean.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

/**
 * @author Vanessa
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
		
		
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00a2ff")));

		Intent intent = getIntent();
		currentCityNameString = intent.getStringExtra("current_city");
		currentCountryCodeString = intent.getStringExtra("current_ccode");
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(AboutActivity.this, MainActivity.class);
		intent.putExtra("current_city", currentCityNameString);
		intent.putExtra("current_ccode", currentCountryCodeString);
		startActivity(intent);

		//super.onBackPressed();
	}

}
