package it.apdev.weathermean.logic;

/**
 * Classe astratta relativa al servizio di richiesta HTTP per il recupero del
 * meteo da una data piattaforma Per estendere tale classe occorre indicare un
 * url specifico in base alla piattaforma scelta e occorre effettuare l'override
 * del metodo retireveWeather()
 * 
 * @author Andrea
 * 
 */

public abstract class HttpService {

	protected String urlString; 	// Url specifico della piattaforma dalla quale
									// prelevare i dati
	protected String city; 			// Città da monitorare
	protected String codeNation; 	// Codice della nazione

	/**
	 * Costruttore di default
	 */
	public HttpService() {

	}

	/**
	 * Restituisce l'url del servizio HTTP
	 * 
	 * @return
	 */
	public String getUrlString() {
		return urlString;
	}

	/**
	 * Imposta con urlString l'url del servizio HTTP
	 * 
	 * @param urlString
	 */
	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	/**
	 * Restituisce la città da monitorare
	 * 
	 * @return
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Imposta la città da monitorare
	 * 
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Restituisce il codice della nazione da monitorare
	 * 
	 * @return
	 */
	public String getCodeNation() {
		return codeNation;
	}

	/**
	 * Imposta il codice della nazione da monitorare
	 * 
	 * @param codeNation
	 */
	public void setCodeNation(String codeNation) {
		this.codeNation = codeNation;
	}

	/**
	 * Recupera il meteo relativo alla città voluta tramite l'utilizzo di una
	 * connessione HTTP all'url desiderato
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract Weather retrieveWeather() throws Exception;
	public abstract int getForecastCodeForService(String forecastDescription);
}
