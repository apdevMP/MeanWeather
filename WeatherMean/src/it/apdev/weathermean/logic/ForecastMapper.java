package it.apdev.weathermean.logic;

import it.apdev.weathermean.R;

import java.util.Arrays;

/**
 * This class is a Singleton and its scope is to map forecast from website to
 * forecast of this application
 * 
 * @author Andrea
 * 
 */
public class ForecastMapper
{

	private static final String[]	forecastMap		= { "Not Available", "Sunny", "Cloudy", "Rain", "Storm", "Snow" };

	private static final int[]		idIconMap		= { R.drawable.not_available, R.drawable.sunny, R.drawable.cloudy, R.drawable.storm,
			R.drawable.snow						};

	private static ForecastMapper	forecastMapper	= null;

	/**
	 * Default constructor
	 */
	private ForecastMapper()
	{

	}

	/**
	 * Returns an istance of ForecastMapper
	 * 
	 * @return
	 */
	public static ForecastMapper getIstance()
	{
		if (forecastMapper == null)
		{
			forecastMapper = new ForecastMapper();
		}

		return forecastMapper;
	}

	/**
	 * Returns a code(index of array) related to description
	 * 
	 * @param description
	 * @return
	 */
	public int getForecastCode(String description)
	{
		int forecastCode;
		forecastCode = Arrays.asList(forecastMap).indexOf(description);
		return forecastCode;
	}

	/**
	 * Returns a description from forecast code(index of array)
	 * 
	 * @param forecastCode
	 * @return
	 */
	public String getForecastDescription(int forecastCode)
	{
		String forecastDescription;
		forecastDescription = forecastMap[forecastCode];
		return forecastDescription;
	}

	/**
	 * 
	 * @param forecastCode
	 * @return
	 */
	public int getIconId(int forecastCode)
	{
		return idIconMap[forecastCode];
	}
}
