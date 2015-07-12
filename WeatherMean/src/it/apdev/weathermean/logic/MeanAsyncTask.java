package it.apdev.weathermean.logic;

import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

/**
 * This class extends AsyncTask and execute a merge of a list of weather. It
 * returns a weather that it is a mean of others weather
 * 
 * @author TEAM apdev
 * 
 */
public class MeanAsyncTask extends AsyncTask<List<Weather>, Void, Weather> {

	private static final String TAG = "MeanAsyncTask";
	private List<Weather> list;

	@Override
	protected Weather doInBackground(List<Weather>... params) {
		if (params.length != 1 && params[0].size() != 3)
			return null;

		list = params[0];

		Weather meanWeather = new Weather();
		// Log.v(TAG, "Start to merge Weather");
		// Set the temporary variables for the sum to 0-value
		double sumHumidity = 0;
		double sumSpeed = 0;
		double sumTemp = 0;
		double sumPressure = 0;
		double sumVisibility = 0;
		int visibility_num = 0;

		// For each Weather in the list, sum the values
		for (Weather i : list) {

			sumHumidity = sumHumidity + i.getHumidity();
			sumSpeed = sumSpeed + i.getWind();
			sumTemp = sumTemp + i.getTemperature();
			sumPressure = sumPressure + i.getPressure();
			if (i.getVisibility() > 0) {
				sumVisibility = sumVisibility + i.getVisibility();
				visibility_num++;
			}
		}

		// Divide the sum for the size of list
		double humidity = Utils.roundMeasure(sumHumidity / list.size());
		meanWeather.setHumidity(humidity);

		double wind = Utils.roundMeasure(sumSpeed / list.size());
		meanWeather.setWind(wind);

		double temperature = Utils.roundMeasure(sumTemp / list.size());
		meanWeather.setTemperature(temperature);

		double pressure = Utils.roundMeasure(sumPressure / list.size());
		meanWeather.setPressure(pressure);

		Log.v(TAG, "" + visibility_num);
		double visibility = Utils.roundMeasure(sumVisibility / visibility_num);
		meanWeather.setVisibility(visibility);

		// For the description it used a private method
		mergeDescription(meanWeather);

		return meanWeather;
	}

	/**
	 * Return the description that is the merge of description of the list. The
	 * policies are: 1) if 2 descriptions of 3 are the same,so these strings are
	 * the merge description; 2) it does the mean of forecast code of this
	 * application and the result number corresponds to the merge description
	 * 
	 * @param list
	 * @return
	 */
	private void mergeDescription(Weather mean) {

		ForecastMapper mapper = ForecastMapper.getIstance();
		// Initialize merge string
		String merge = "";

		String weather1 = list.get(0).getDescription();
		String weather2 = list.get(1).getDescription();
		String weather3 = list.get(2).getDescription();

		// if 2/3 description are the same,choose these
		if (weather1.equalsIgnoreCase(weather2)) {
			mean.setDescription(weather1);
			int forecastCode = list.get(0).getForecastCode();
			mean.setForecastCode(forecastCode);
			mean.setIdIcon(mapper.getIconId(forecastCode));

		} else if (weather1.equalsIgnoreCase(weather3)) {
			mean.setDescription(weather1);
			int forecastCode = list.get(0).getForecastCode();
			mean.setForecastCode(forecastCode);
			mean.setIdIcon(mapper.getIconId(forecastCode));
		} else if (weather2.equalsIgnoreCase(weather3)) {
			mean.setDescription(weather2);
			int forecastCode = list.get(1).getForecastCode();
			mean.setForecastCode(forecastCode);
			mean.setIdIcon(mapper.getIconId(forecastCode));
		}

		// mean of the forecast code
		if (merge.equalsIgnoreCase("")) {
			int count = 0;
			int sumDesc = 0;

			for (Weather w : list) {
				// not sum if forecast code corresponds to "Not Available"
				if (w.getForecastCode() != 0) {
					count++;
				}
				sumDesc = sumDesc + w.getForecastCode();
			}

			if (sumDesc == 0) {
				// if sumDesc is zero,it means that merge description is not
				// avaliable
				int forecastCode = 0;
				mean.setForecastCode(forecastCode);
				merge = mapper.getForecastDescription(sumDesc);
				mean.setDescription(merge);
				mean.setIdIcon(mapper.getIconId(forecastCode));
			} else {
				// mean of the forecast code
				double meanCode = (double) sumDesc / (double) count;
				Log.v(TAG, "mean: " + meanCode);
				int forecastCode = (int) Math.round(meanCode);
				Log.v(TAG, "round: " + forecastCode);
				merge = mapper.getForecastDescription(forecastCode);
				mean.setDescription(merge);
				mean.setIdIcon(mapper.getIconId(forecastCode));
			}
		}
	}

}
