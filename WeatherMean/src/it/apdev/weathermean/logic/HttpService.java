package it.apdev.weathermean.logic;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;

/**
 * Abstract class related to service of HTTP request for retrieving weather from
 * given platform.To extend this class,it must provide the specific URL of the
 * given API and it must override retrieveWeather() and
 * getForecastCodeForService()
 * 
 * @author TEAM apdev
 * 
 */

public abstract class HttpService {

	protected String urlString; // specific url of given platform
	protected String city;
	protected String codeNation;

	/**
	 * Default Constructor
	 */
	public HttpService() {

	}

	/**
	 * Return the string contains url
	 * 
	 * @return
	 */
	public String getUrlString() {
		return urlString;
	}

	/**
	 * Set urlString with url of HTTP service
	 * 
	 * @param urlString
	 */
	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	/**
	 * Return the city
	 * 
	 * @return
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Set the city
	 * 
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Return the country code
	 * 
	 * @return
	 */
	public String getCodeNation() {
		return codeNation;
	}

	/**
	 * Set the country code
	 * 
	 * @param codeNation
	 */
	public void setCodeNation(String codeNation) {
		this.codeNation = codeNation;
	}

	/**
	 * Retrieve weather related to the given city through an HTTP connection. It
	 * must parse an JSONObject
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract Weather retrieveWeather() throws InterruptedException,
			ExecutionException, JSONException;

	/**
	 * Associate a forecast code to the string of description
	 * 
	 * @param forecastDescription
	 * @return
	 */
	public abstract int getForecastCodeForService(String forecastDescription);
}
