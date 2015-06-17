package it.apdev.weathermean.logic;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Classe weather che gestisce tutte le caratteristiche del meteo
 * 
 * @author Andrea
 * 
 */
public class Weather implements Parcelable {

	private static final String TAG = "Weather";

	private String source;
	private String description;
	private double temperature;
	private double pressure;
	private double wind;
	private double humidity;
	private int forecastCode;

	/**
	 * Costruttore di default
	 */
	public Weather() {
		temperature = 0.0;
		pressure = 0.0;
		wind = 0.0;
		humidity = 0.0;
		forecastCode = 0;

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
	 * Immposta la descrizionee
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Restituisce la temperatura
	 * 
	 * @return
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * Imposta la temperatura
	 * 
	 * @param temperature
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * Restuisce la pressione
	 * 
	 * @return
	 */
	public double getPressure() {
		return pressure;
	}

	/**
	 * Imposta la pressione
	 * 
	 * @param pressure
	 */
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	/**
	 * Restituisce la velocità del vento
	 * 
	 * @return
	 */
	public double getWind() {
		return wind;
	}

	/**
	 * Imposta la velocità del vento
	 * 
	 * @param wind
	 */
	public void setWind(double wind) {
		this.wind = wind;
	}

	/**
	 * Restituisce l'umidità
	 * 
	 * @return
	 */
	public double getHumidity() {
		return humidity;
	}

	/**
	 * Imposta l'umidità
	 * 
	 * @param humidity
	 */
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}
	/**
	 * 
	 * @return
	 */
	public int getForecastCode() {
		return forecastCode;
	}
	
	/**
	 * 
	 * @param forecastCode
	 */
	public void setForecastCode(int forecastCode) {
		this.forecastCode = forecastCode;
	}

	@Override
	public String toString() {

		String weather = "Weather[Description: " + this.description
				+ " Temperature: " + this.temperature + " Wind: " + this.wind
				+ " Pressure: " + this.pressure + " Humidity: " + this.humidity
				+ "]";
		return weather;
	}

	/**
	 * Questo metodo realizza
	 * 
	 * @param list
	 */
	public void mergeWeather(List<Weather> list) {
		Log.v(TAG, "Start to merge Weather");
		double sumHumidity = 0;
		double sumSpeed = 0;
		double sumTemp = 0;
		double sumPressure = 0;

		for (Weather i : list) {
			sumHumidity = sumHumidity + i.getHumidity();
			sumSpeed = sumSpeed + i.getWind();
			sumTemp = sumTemp + i.getTemperature();
			sumPressure = sumPressure + i.getPressure();
		}

		this.humidity = Utils.roundMeasure(sumHumidity / list.size());
		this.wind = Utils.roundMeasure(sumSpeed / list.size());
		this.temperature = Utils.roundMeasure(sumTemp / list.size());
		this.pressure = Utils.roundMeasure(sumPressure / list.size());

		this.description = mergeDescription(list);
	}

	private String mergeDescription(List<Weather> list) {
		String merge = "";

		String weather1 = list.get(0).getDescription();
		String weather2 = list.get(1).getDescription();
		String weather3 = list.get(2).getDescription();

		if (weather1.equalsIgnoreCase(weather2)) {
			merge = weather1;
		} else if (weather1.equalsIgnoreCase(weather3)) {
			merge = weather1;
		} else if (weather2.equalsIgnoreCase(weather3)) {
			merge = weather2;
		}

		if (merge.equalsIgnoreCase("")) {
			int count = 0;
			int sumDesc = 0;
			ForecastMapper mapper = ForecastMapper.getIstance();
			for (Weather w : list) {
				if (w.getForecastCode() != 0) {
					count++;
				}
				sumDesc = sumDesc + w.getForecastCode();
			}
			if (sumDesc == 0)
				merge = mapper.getForecastDescription(sumDesc);
			else {
				this.forecastCode = sumDesc / count;
				merge = mapper.getForecastDescription(forecastCode);
			}
		}

		return merge;
	}

	protected Weather(Parcel in) {
		source = in.readString();
		description = in.readString();
		temperature = in.readDouble();
		pressure = in.readDouble();
		wind = in.readDouble();
		humidity = in.readDouble();
		forecastCode = in.readInt();
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
		dest.writeInt(forecastCode);
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
