package it.apdev.weathermean.logic;

/**
 * Classe weather che gestisce tutte le caratteristiche del meteo
 * @author Andrea
 *
 */
public class Weather {
	
	private String description;
	private double temperature;
	private double pressure;
	private double wind;
	private double humidity;
	
	/**
	 * Costruttore di default
	 */
	public Weather(){
		
	}
	/**
	 * Restituisce la descrizione
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Immposta la descrizionee
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Restituisce la temperatura
	 * @return
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * Imposta la temperatura
	 * @param temperature
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * Restuisce la pressione
	 * @return
	 */
	public double getPressure() {
		return pressure;
	}

	/**
	 * Imposta la pressione
	 * @param pressure
	 */
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	/**
	 * Restituisce la velocità del vento
	 * @return
	 */
	public double getWind() {
		return wind;
	}

	/**
	 * Imposta la velocità del vento
	 * @param wind
	 */
	public void setWind(double wind) {
		this.wind = wind;
	}

	/**
	 * Restituisce l'umidità
	 * @return
	 */
	public double getHumidity() {
		return humidity;
	}

	/**
	 * Imposta l'umidità
	 * @param humidity
	 */
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String weather = "Weather[Description: "+ this.description +" Temperature: " + 
				this.temperature +" Wind: "+ this.wind +" Pressure: "+ this.pressure +" Humidity: "+this.humidity+"]";
		return weather;
	}
	
	
	

}
