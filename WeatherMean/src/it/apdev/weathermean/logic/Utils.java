package it.apdev.weathermean.logic;

/**
 * This class ...
 * @author Andrea
 *
 */
public class Utils {
	
	private static final double ZERO_CELSIUS_IN_KELVIN  = 273.15;
	private static final int DECIMAL_PLACES = 2;
	
	/**
	 * Metodo che converte i gradi Fahrenehit in gradi Celsius
	 * @param fahrenehit
	 * @return
	 */
	public static double fromFahrenheitToCelsius(double fahrenehit){
		double celsius;
		celsius = (fahrenehit - 32)/1.8;
		
		return celsius;
	}
	
	/**
	 * Metodo che converte la temperatura in Kelvin in gradi Celsius
	 * @param kelvin
	 * @return
	 */
	public static double fromKelvinToCelsius(double kelvin){
		double celsius;
		celsius = kelvin - ZERO_CELSIUS_IN_KELVIN;
		
		return celsius;
	}
	
	/**
	 * Converte le miglia orarie in chilometri orari
	 * @param mph
	 * @return
	 */
	public static double fromMphToKmh(double mph){
		double kmh;
		kmh = (mph * 1.609344);
		
		return kmh;
	}
	
	/**
	 * Converte i metri al secondo in chilometri orari
	 * @param ms
	 * @return
	 */
	public static double fromMsToKmh(double ms){
		double kmh;
		kmh = ms * 3.6;
		return kmh;
	}
	
	/*public static double roundMeasure(double measure){
		double round;
		String rString = ""+measure;
		for(int i=0;i<rString.length();i++){
			if(rString.charAt(i)=='.')
		}
		round = Double.parseDouble(rString);
		return round;
	}*/
	
	public static double roundMeasure( double measure){
	    return Math.round( measure * Math.pow( 10, DECIMAL_PLACES ) )/Math.pow( 10, DECIMAL_PLACES );
	}

}
