package it.apdev.weathermean.logic;

/**
 * This class contains many static useful methods
 * 
 * @author TEAM apdev
 * 
 */
public class Utils {

	private static final double ZERO_CELSIUS_IN_KELVIN = 273.15;
	private static final int DECIMAL_PLACES = 2;
	private static final double ONE_HPA_IN_INHG = 0.0296133971008484;

	/**
	 * Convert Fahrenheit degrees to Celsius degrees
	 * 
	 * @param fahrenehit
	 * @return
	 */
	public static double fromFahrenheitToCelsius(double fahrenehit) {
		double celsius;
		celsius = (fahrenehit - 32) / 1.8;

		return celsius;
	}

	/**
	 * Convert temperature in kelvin to Celsius degrees Metodo che converte la
	 * temperatura in Kelvin in gradi Celsius
	 * 
	 * @param kelvin
	 * @return
	 */
	public static double fromKelvinToCelsius(double kelvin) {
		double celsius;
		celsius = kelvin - ZERO_CELSIUS_IN_KELVIN;

		return celsius;
	}

	/**
	 * Convert miles per hour to kilometres per hour
	 * 
	 * @param mph
	 * @return
	 */
	public static double fromMphToKmh(double mph) {
		double kmh;
		kmh = (mph * 1.609344);

		return kmh;
	}

	/**
	 * Convert metres for second to kilometres for hour
	 * 
	 * @param ms
	 * @return
	 */
	public static double fromMsToKmh(double ms) {
		double kmh;
		kmh = ms * 3.6;
		return kmh;
	}

	/**
	 * Convert inch of mercury to hectopascal
	 * 
	 * @param inhg
	 * @return
	 */
	public static double fromInhgToHpa(double inhg) {
		double hpa = inhg / ONE_HPA_IN_INHG;
		return hpa;
	}

	/**
	 * Convert miles to kilometres
	 * 
	 * @param miles
	 * @return
	 */
	public static double fromMilesToKm(double miles) {
		double km = miles * 1.609;
		return km;
	}

	/**
	 * Round a measure to DECIMAL_PLACES number
	 * 
	 * @param measure
	 * @return
	 */

	public static double roundMeasure(double measure) {
		return Math.round(measure * Math.pow(10, DECIMAL_PLACES))
				/ Math.pow(10, DECIMAL_PLACES);
	}

}
