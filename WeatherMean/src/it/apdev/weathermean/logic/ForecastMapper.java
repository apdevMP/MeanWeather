package it.apdev.weathermean.logic;

import java.util.Arrays;

/**
 * ForecastMapper è una classe singleton
 * @author Andrea
 *
 */
public class ForecastMapper {
	
	private static final String[] forecastMap = {"Not Available","Sunny","Cloudy","Rain","Storm","Snow"};
	private static ForecastMapper forecastMapper= null;
	
	/**
	 * Default constructor
	 */
	private ForecastMapper(){
	
	}
	
	public static ForecastMapper getIstance(){
		if(forecastMapper == null){
			forecastMapper = new ForecastMapper();
		}
			
		return forecastMapper;
	}
	
	public int getForecastCode(String description){
		int forecastCode;
		forecastCode = Arrays.asList(forecastMap).indexOf(description);
		return forecastCode;
	}

	public String getForecastDescription(int forecastCode){
		String forecastDescription;
		forecastDescription = forecastMap[forecastCode];
		return forecastDescription;
	}
}
