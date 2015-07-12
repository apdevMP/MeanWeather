package it.apdev.weathermean.logic;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * This class manages main features of weather
 * 
 * @author Andrea
 * 
 */
public class Weather implements Parcelable {

	private static final String TAG = "Weather";

	private String source; // source of the weather(e.g Yahoo)
	private String description; // forecast
	private double temperature;
	private double pressure;
	private double wind;
	private double humidity;
	private double visibility;
	private int forecastCode;
	private int idIcon;

	/**
	 * Default constructor
	 */
	public Weather() {
		temperature = 0.0;
		pressure = 0.0;
		wind = 0.0;
		humidity = 0.0;
		visibility = 0.0;
		forecastCode = 0;
		idIcon = 0;
	}

	/**
	 * Get the source
	 * 
	 * @return
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Set the source
	 * 
	 * @param source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Get the description
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the temperature
	 * 
	 * @return
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * Set the temperature
	 * 
	 * @param temperature
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * Get the pressure
	 * 
	 * @return
	 */
	public double getPressure() {
		return pressure;
	}

	/**
	 * Set the pressure
	 * 
	 * @param pressure
	 */
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	/**
	 * Get the wind speed
	 * 
	 * @return
	 */
	public double getWind() {
		return wind;
	}

	/**
	 * Set the wind speed
	 * 
	 * @param wind
	 */
	public void setWind(double wind) {
		this.wind = wind;
	}

	/**
	 * Get the humidity
	 * 
	 * @return
	 */
	public double getHumidity() {
		return humidity;
	}

	/**
	 * Set the humidity
	 * 
	 * @param humidity
	 */
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	/**
	 * 	Return the visibility
	 * @return
	 */
	public double getVisibility()
	{
		return visibility;
	}

	/**
	 * Set the visibility 
	 * @param visibility
	 */
	public void setVisibility(double visibility)
	{
		this.visibility = visibility;
	}

	/**
	 * Get the code of forecast for this application
	 * 
	 * @return
	 */
	public int getForecastCode() {
		return forecastCode;
	}

	/**
	 * Set the code of forecast for this application
	 * 
	 * @param forecastCode
	 */
	public void setForecastCode(int forecastCode) {
		this.forecastCode = forecastCode;
	}

	public int getIdIcon() {
		return idIcon;
	}

	public void setIdIcon(int idIcon) {
		this.idIcon = idIcon;
	}


	@Override
	public String toString() {

		String weather = "Weather[Description: " + this.description
				+ " Temperature: " + this.temperature + " Wind: " + this.wind
				+ " Pressure: " + this.pressure + " Humidity: " + this.humidity + " Visibility: " + this.visibility
				+ " ForecastCode: " + this.forecastCode + "idIcon: "+ this.idIcon +" ]";
		return weather;
	}

	/**
	 * This method realize the merge of a list of weathers, it calls MeanAsyncTask
	 * 
	 * @param list
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	public void mergeWeather(List<Weather> list) throws InterruptedException, ExecutionException {

		Log.v(TAG, "Start to merge Weather");
		MeanAsyncTask task = new MeanAsyncTask();
		task.execute(list);
		
		Weather mean = task.get(); 
		this.copy(mean);
				
	}

	/**
	 * This private method copies a weather to another one, field by field
	 * @param mean
	 */
	private void copy(Weather mean) {
		this.setDescription(mean.getDescription());
		this.setForecastCode(mean.getForecastCode());
		this.setHumidity(mean.getHumidity());
		this.setIdIcon(mean.getIdIcon());
		this.setPressure(mean.getPressure());
		this.setTemperature(mean.getTemperature());
		this.setVisibility(mean.getVisibility());
		this.setWind(mean.getWind());
		
	}


	protected Weather(Parcel in) {
        source = in.readString();
        description = in.readString();
        temperature = in.readDouble();
        pressure = in.readDouble();
        wind = in.readDouble();
        humidity = in.readDouble();
        visibility = in.readDouble();
        forecastCode = in.readInt();
        idIcon = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(source);
        dest.writeString(description);
        dest.writeDouble(temperature);
        dest.writeDouble(pressure);
        dest.writeDouble(wind);
        dest.writeDouble(humidity);
        dest.writeDouble(visibility);
        dest.writeInt(forecastCode);
        dest.writeInt(idIcon);
    }

    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };
}
