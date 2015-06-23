package it.apdev.weathermean.logic;

import java.util.List;

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
				+ " Pressure: " + this.pressure + " Humidity: " + this.humidity
				+ " ForecastCode: " + this.forecastCode + "idIcon: "+ this.idIcon +" ]";
		return weather;
	}

	/**
	 * This method realize the merge of a list of weather
	 * 
	 * @param list
	 */
	public void mergeWeather(List<Weather> list) {

		Log.v(TAG, "Start to merge Weather");
		// Set the temporary variables for the sum to 0-value
		double sumHumidity = 0;
		double sumSpeed = 0;
		double sumTemp = 0;
		double sumPressure = 0;

		// For each Weather in the list, sum the values
		for (Weather i : list) {
			sumHumidity = sumHumidity + i.getHumidity();
			sumSpeed = sumSpeed + i.getWind();
			sumTemp = sumTemp + i.getTemperature();
			sumPressure = sumPressure + i.getPressure();
		}

		// Divide the sum for the size of list
		this.humidity = Utils.roundMeasure(sumHumidity / list.size());
		this.wind = Utils.roundMeasure(sumSpeed / list.size());
		this.temperature = Utils.roundMeasure(sumTemp / list.size());
		this.pressure = Utils.roundMeasure(sumPressure / list.size());

		// For the description it is used a private method
		this.description = mergeDescription(list);
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
	private String mergeDescription(List<Weather> list) {

		ForecastMapper mapper = ForecastMapper.getIstance();
		// Initialize merge string
		String merge = "";

		String weather1 = list.get(0).getDescription();
		String weather2 = list.get(1).getDescription();
		String weather3 = list.get(2).getDescription();

		// if 2/3 description are the same,choose these
		if (weather1.equalsIgnoreCase(weather2)) {
			merge = weather1;
			this.forecastCode = list.get(0).getForecastCode();
			this.idIcon = mapper.getIconId(this.forecastCode);
			
		} else if (weather1.equalsIgnoreCase(weather3)) {
			merge = weather1;
			this.forecastCode = list.get(0).getForecastCode();
			this.idIcon = mapper.getIconId(this.forecastCode);
		} else if (weather2.equalsIgnoreCase(weather3)) {
			merge = weather2;
			this.forecastCode = list.get(1).getForecastCode();
			this.idIcon = mapper.getIconId(this.forecastCode);
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
				this.forecastCode = 0;
				merge = mapper.getForecastDescription(sumDesc);
				this.idIcon = mapper.getIconId(this.forecastCode);
			} else {
				// mean of the forecast code
				double mean = (double) sumDesc / (double) count;
				Log.v(TAG, "mean: " + mean);
				this.forecastCode = (int) Math.round(mean);
				Log.v(TAG, "round: " + this.forecastCode);
				merge = mapper.getForecastDescription(this.forecastCode);
				this.idIcon = mapper.getIconId(this.forecastCode);
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
