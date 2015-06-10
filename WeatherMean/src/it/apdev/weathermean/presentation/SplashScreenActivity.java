/**
 * La classe SplashScreenActivity serve a gestire l'activity dello SplashScreen che recupera i dati 
 * della posizione attuale dalla quale calcolare il meteo e recupera i dati delle citt� salavate nel db delle preferenze
 */
package it.apdev.weathermean.presentation;

import it.apdev.weathermean.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Vanessa
 * 
 */
public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Permette di eliminare la barra superiore 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		
		/* commento di prova*/
	}

}
