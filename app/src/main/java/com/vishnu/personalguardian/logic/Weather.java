package com.vishnu.personalguardian.logic;

import java.util.Date;

import com.google.android.gms.internal.bu;

public class Weather {
	private String weatherStation;

	private String sunRise;
	private String sunSet;

	private String temprature;
	private String tempratureMin;
	private String tempratureMax;
	private String tempUnit;

	private String humidity;
	private String humidityUnit;

	private String pressure;
	private String pressureUnit;

	private String windSpeed;
	private String windName;
	private String windDirection;
	private String windCode;

	private String clouds;

	private String precipitation;
	private String precipitationUnit;

	private String weather;

	private String lastUpdate;

	private String[] data = new String[15];
	
	public String getWeatherStation() {
		return "Weather Station: " + weatherStation;
	}

	public void setWeatherStation(String weatherStation) {
		this.weatherStation = weatherStation;
		if (weatherStation != null) {
			data[0] = "Weather Station: " + weatherStation;
		}
	}

	public String getSunRise() {
		return "Sun Rise: " + sunRise;
	}

	public void setSunRise(String sunRise) {
		this.sunRise = sunRise;
		if (sunRise != null) {
			data[1] = "Sun Rise: " + sunRise.substring(0, 16).replaceAll("T", ", ") + " GMT";
		}
	}

	public String getSunSet() {
		return "Sun Set: " + sunSet;
	}

	public void setSunSet(String sunSet) {
		this.sunSet = sunSet;
		if (sunSet != null) {
			data[2] = "Sun Set: " + sunSet.substring(0, 16).replaceAll("T", ", ") + " GMT";
		}
	}

	public String getTemprature() {
		return "Temprature: " + temprature + " " + tempUnit;
	}

	public void setTemprature(String temprature) {
		this.temprature = temprature;
	}

	public String getTempratureMin() {
		return "Min.Temprature: " + tempratureMin + " " + tempUnit;
	}

	public void setTempratureMin(String tempratureMin) {
		this.tempratureMin = tempratureMin;
	}

	public String getTempratureMax() {
		return "Max.Temprature: " + tempratureMax + " " + tempUnit;
	}

	public void setTempratureMax(String tempratureMax) {
		this.tempratureMax = tempratureMax;
	}

	public String getTempUnit() {
		return tempUnit;
	}

	public void setTempUnit(String tempUnit) {
		this.tempUnit = tempUnit;
		if (temprature != null && tempUnit != null) {
			data[3] = "Temprature: " + temprature + " " + tempUnit;
		}
		if (tempratureMin != null && tempUnit != null) {
			data[4] = "Min.Temprature: " + tempratureMin + " " + tempUnit;
		}
		if (tempratureMax != null && tempUnit != null) {
			data[5] = "Max.Temprature: " + tempratureMax + " " + tempUnit;
		}
	}

	public String getHumidity() {
		return "Humidity: " + humidity + "" + humidityUnit;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getHumidityUnit() {
		return humidityUnit;
	}

	public void setHumidityUnit(String humidityUnit) {
		this.humidityUnit = humidityUnit;
		if (humidity != null && humidityUnit != null) {
			data[6] = "Humidity: " + humidity + "" + humidityUnit;
		}
	}

	public String getPressure() {
		return "Pressure: " + pressure + " " + pressureUnit;
	}

	public void setPressure(String pressure) {
		this.pressure = pressure;
	}

	public String getPressureUnit() {
		return pressureUnit;
	}

	public void setPressureUnit(String pressureUnit) {
		this.pressureUnit = pressureUnit;
		if (pressure != null && pressureUnit != null) {
			data[7] = "Pressure: " + pressure + " " + pressureUnit;
		}
	}

	public String getWindSpeed() {
		return "Wind Speed: " + windSpeed;
	}

	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
		if (windSpeed != null) {
			data[9] = "Wind Speed: " + windSpeed;
		}
	}

	public String getWindName() {
		return "Wind Type: " + windName;
	}

	public void setWindName(String windName) {
		this.windName = windName;
		if (windName != null) {
			data[8] = "Wind Type: " + windName;
		}
	}

	public String getWindDirection() {
		return "Wind Direction: " + windDirection + " " + windCode;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public String getWindCode() {
		return windCode;
	}

	public void setWindCode(String windCode) {
		this.windCode = windCode;
		if (windDirection != null && windCode != null) {
			data[10] = "Wind Direction: " + windDirection + " " + windCode;
		}
	}

	public String getClouds() {
		return "Clouds: " + clouds;
	}

	public void setClouds(String clouds) {
		this.clouds = clouds;
		if (clouds != null) {
			data[11] = "Clouds: " + clouds;
		}
	}

	public String getPrecipitation() {
		return "Precipitation: " + precipitation + ", " + precipitationUnit;
	}

	public void setPrecipitation(String precipitation) {
		this.precipitation = precipitation;
	}

	public String getPrecipitationUnit() {
		return precipitationUnit;
	}

	public void setPrecipitationUnit(String precipitationUnit) {
		this.precipitationUnit = precipitationUnit;
		if (precipitation != null && precipitationUnit != null) {
			data[12] = "Precipitation: " + precipitation + ", "
					+ precipitationUnit;
		}
	}

	public String getWeather() {
		return "Weather: " + weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
		if (weather != null) {
			data[13] = "Weather: " + weather;
		}
	}

	public String getLastUpdate() {
		return "Last Weather Update: " + lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
		if (lastUpdate != null) {
			data[14] = "Last Weather Update: " + lastUpdate.substring(0, 16).replaceAll("T", ", ") + " GMT";
		}
	}

	public String getWeatherData() {
		StringBuilder builder = new StringBuilder();
		String lineBreaker = "\n";
		for (String str : data) {
			if (str != null) {
				builder.append(str);
				builder.append(lineBreaker);
			}
		}
		return builder.toString();
	}
}
