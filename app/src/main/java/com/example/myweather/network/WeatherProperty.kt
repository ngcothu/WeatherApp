package com.example.myweather.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherProperty(
    @SerializedName("current_weather")
    val currentWeather: CurrentWeatherData,
    @SerializedName("hourly")
    val hourlyWeather: HourlyData,
    @SerializedName("daily")
    val dailyWeather: DailyData
    ): Serializable